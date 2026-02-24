package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.ModNotFoundException;
import com.volosinzena.barolab.mapper.ModPostMapper;
import com.volosinzena.barolab.repository.ModPostRepository;
import com.volosinzena.barolab.repository.PostRepository;
import com.volosinzena.barolab.repository.PostVoteRepository;
import com.volosinzena.barolab.repository.UserRepository;
import com.volosinzena.barolab.repository.entity.ModPostEntity;
import com.volosinzena.barolab.repository.entity.PostEntity;
import com.volosinzena.barolab.repository.entity.PostVoteEntity;
import com.volosinzena.barolab.repository.entity.UserEntity;
import com.volosinzena.barolab.service.model.ModPost;
import com.volosinzena.barolab.service.model.VoteValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class ModPostServiceImpl implements ModPostService {

    private final ModPostRepository modPostRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostVoteRepository postVoteRepository;
    private final ModPostMapper modPostMapper;

    @Autowired
    public ModPostServiceImpl(ModPostRepository modPostRepository, PostRepository postRepository,
            UserRepository userRepository, PostVoteRepository postVoteRepository, ModPostMapper modPostMapper) {
        this.modPostRepository = modPostRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postVoteRepository = postVoteRepository;
        this.modPostMapper = modPostMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModPost> getAllMods() {
        List<ModPostEntity> entities = modPostRepository.findAllWithPost();
        List<ModPost> mods = entities.stream().map(modPostMapper::toDomain).toList();

        if (!mods.isEmpty()) {
            String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
            if (currentUserId != null && !currentUserId.equals("anonymousUser")) {
                UserEntity userEntity = getCurrentUser();
                List<UUID> postIds = entities.stream().map(ModPostEntity::getPostId).toList();
                applyUserVotes(mods, postIds, userEntity.getId());
            }
        }

        return mods;
    }

    @Override
    @Transactional(readOnly = true)
    public ModPost getModByExternalId(Long externalId) {
        ModPostEntity entity = modPostRepository.findByExternalIdWithPost(externalId)
                .orElseThrow(() -> new ModNotFoundException(externalId));
        ModPost modPost = modPostMapper.toDomain(entity);

        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (currentUserId != null && !currentUserId.equals("anonymousUser")) {
            UserEntity userEntity = getCurrentUser();
            postVoteRepository.findByPost_IdAndUser_Id(entity.getPostId(), userEntity.getId())
                    .ifPresent(vote -> modPost.setMyVote(VoteValue.fromValue(vote.getValue())));
        }
        return modPost;
    }

    @Override
    @Transactional
    public ModPost createModPost(String title, String content, String externalUrl) {
        log.info("Create Mod Post Request");

        long externalId = parseExternalId(externalUrl);
        if (modPostRepository.existsByExternalId(externalId)) {
            throw new IllegalArgumentException("Mod with external id " + externalId + " already exists");
        }

        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity userEntity = userRepository
                .findById(UUID.fromString(currentUserId))
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        PostEntity postEntity = new PostEntity();
        postEntity.setUser(userEntity);
        postEntity.setTitle(title);
        postEntity.setContent(content);

        Instant now = Instant.now();
        postEntity.setCreatedAt(now);
        postEntity.setUpdatedAt(now);
        postEntity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        postEntity.setRating(0);

        PostEntity savedPost = postRepository.save(postEntity);

        ModPostEntity modPostEntity = new ModPostEntity();
        modPostEntity.setPost(savedPost);
        modPostEntity.setExternalId(externalId);
        modPostEntity.setPopularity(0);

        ModPostEntity savedModPost = modPostRepository.save(modPostEntity);
        log.info("Successfully created mod post externalId={}", externalId);
        return modPostMapper.toDomain(savedModPost);
    }

    @Override
    @Transactional
    public ModPost activateMod(Long externalId) {
        ModPostEntity entity = modPostRepository.findByExternalIdWithPost(externalId)
                .orElseThrow(() -> new ModNotFoundException(externalId));
        PostEntity postEntity = entity.getPost();
        postEntity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        postEntity.setUpdatedAt(Instant.now());
        PostEntity savedPost = postRepository.save(postEntity);
        entity.setPost(savedPost);
        ModPostEntity savedMod = modPostRepository.save(entity);
        return modPostMapper.toDomain(savedMod);
    }

    @Override
    @Transactional
    public ModPost blockMod(Long externalId) {
        ModPostEntity entity = modPostRepository.findByExternalIdWithPost(externalId)
                .orElseThrow(() -> new ModNotFoundException(externalId));
        PostEntity postEntity = entity.getPost();
        postEntity.setStatus(com.volosinzena.barolab.repository.entity.Status.BLOCKED);
        postEntity.setUpdatedAt(Instant.now());
        PostEntity savedPost = postRepository.save(postEntity);
        entity.setPost(savedPost);
        ModPostEntity savedMod = modPostRepository.save(entity);
        return modPostMapper.toDomain(savedMod);
    }

    private UserEntity getCurrentUser() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findById(UUID.fromString(currentUserId))
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    private void applyUserVotes(List<ModPost> mods, List<UUID> postIds, UUID userId) {
        List<PostVoteEntity> votes = postVoteRepository.findByUser_IdAndPost_IdIn(userId, postIds);
        Map<UUID, VoteValue> voteMap = new HashMap<>();
        for (PostVoteEntity vote : votes) {
            UUID postId = vote.getPost() != null ? vote.getPost().getId() : null;
            if (postId != null) {
                voteMap.put(postId, VoteValue.fromValue(vote.getValue()));
            }
        }
        for (ModPost modPost : mods) {
            modPost.setMyVote(voteMap.get(modPost.getId()));
        }
    }

    private long parseExternalId(String externalUrl) {
        if (externalUrl == null || externalUrl.isBlank()) {
            throw new IllegalArgumentException("external_url is required");
        }
        String trimmed = externalUrl.trim();
        if (trimmed.matches("^\\d+$")) {
            return Long.parseLong(trimmed);
        }

        URI uri;
        try {
            uri = URI.create(trimmed);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("external_url is invalid");
        }

        String host = uri.getHost();
        if (host == null || !host.contains("steamcommunity.com")) {
            throw new IllegalArgumentException("external_url must be a steamcommunity.com link");
        }
        String path = uri.getPath();
        if (path == null || !path.startsWith("/sharedfiles/filedetails")) {
            throw new IllegalArgumentException("external_url must be a workshop filedetails link");
        }

        String query = uri.getQuery();
        if (query == null) {
            throw new IllegalArgumentException("external_url must include id parameter");
        }
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && kv[0].equals("id") && kv[1].matches("^\\d+$")) {
                return Long.parseLong(kv[1]);
            }
        }
        throw new IllegalArgumentException("external_url must include numeric id parameter");
    }
}
