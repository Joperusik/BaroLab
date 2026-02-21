package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.PostNotFoundException;
import com.volosinzena.barolab.mapper.PostMapper;
import com.volosinzena.barolab.repository.PostRepository;
import com.volosinzena.barolab.repository.PostVoteRepository;
import com.volosinzena.barolab.repository.UserRepository;
import com.volosinzena.barolab.repository.entity.PostEntity;
import com.volosinzena.barolab.repository.entity.PostVoteEntity;
import com.volosinzena.barolab.repository.entity.UserEntity;
import com.volosinzena.barolab.service.model.Post;
import com.volosinzena.barolab.service.model.VoteValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostVoteRepository postVoteRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PostVoteRepository postVoteRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postVoteRepository = postVoteRepository;
        this.postMapper = postMapper;
    }

    @Override
    public List<Post> getAllPosts() {
        List<PostEntity> entities = postRepository.findAll();
        List<Post> posts = entities.stream()
                .map(postMapper::toDomain)
                .toList();

        if (!posts.isEmpty()) {
            UserEntity userEntity = getCurrentUser();
            List<UUID> postIds = entities.stream().map(PostEntity::getId).toList();
            applyUserVotes(posts, postIds, userEntity.getId());
        }

        return posts;
    }

    @Override
    public Post createPost(String title, String content) {
        log.info("Create Post Request");

        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        com.volosinzena.barolab.repository.entity.UserEntity userEntity = userRepository
                .findById(UUID.fromString(currentUserId))
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        com.volosinzena.barolab.repository.entity.PostEntity entity = new com.volosinzena.barolab.repository.entity.PostEntity();
        entity.setUser(userEntity);
        entity.setTitle(title);
        entity.setContent(content);

        Instant now = Instant.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        entity.setRating(0);

        com.volosinzena.barolab.repository.entity.PostEntity savedEntity = postRepository.save(entity);

        log.info("Successfully created post");

        return postMapper.toDomain(savedEntity);
    }

    @Override
    public Post getPostById(UUID postId) {
        Optional<com.volosinzena.barolab.repository.entity.PostEntity> optionalEntity = postRepository.findById(postId);
        if (optionalEntity.isEmpty()) {
            throw new PostNotFoundException(postId);
        }
        Post post = postMapper.toDomain(optionalEntity.get());
        UserEntity userEntity = getCurrentUser();
        postVoteRepository.findByPost_IdAndUser_Id(postId, userEntity.getId())
                .ifPresent(vote -> post.setMyVote(VoteValue.fromValue(vote.getValue())));
        return post;
    }

    @Override
    public Post activatePost(UUID postId) {
        Optional<com.volosinzena.barolab.repository.entity.PostEntity> optionalEntity = postRepository.findById(postId);
        if (optionalEntity.isEmpty()) {
            throw new PostNotFoundException(postId);
        }
        com.volosinzena.barolab.repository.entity.PostEntity entity = optionalEntity.get();
        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        entity.setUpdatedAt(Instant.now());
        com.volosinzena.barolab.repository.entity.PostEntity savedEntity = postRepository.save(entity);
        return postMapper.toDomain(savedEntity);
    }

    @Override
    public Post blockPost(UUID postId) {
        Optional<com.volosinzena.barolab.repository.entity.PostEntity> optionalEntity = postRepository.findById(postId);
        if (optionalEntity.isEmpty()) {
            throw new PostNotFoundException(postId);
        }
        com.volosinzena.barolab.repository.entity.PostEntity entity = optionalEntity.get();
        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.BLOCKED);
        entity.setUpdatedAt(Instant.now());
        com.volosinzena.barolab.repository.entity.PostEntity savedEntity = postRepository.save(entity);
        return postMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public Post likePost(UUID postId) {
        return vote(postId, 1);
    }

    @Override
    @Transactional
    public Post dislikePost(UUID postId) {
        return vote(postId, -1);
    }

    private Post vote(UUID postId, int value) {
        if (value != 1 && value != -1) {
            throw new IllegalArgumentException("Vote value must be +1 or -1");
        }

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        UserEntity userEntity = getCurrentUser();
        Optional<PostVoteEntity> optionalVote = postVoteRepository.findByPost_IdAndUser_Id(postId, userEntity.getId());

        int rating = postEntity.getRating() == null ? 0 : postEntity.getRating();
        Instant now = Instant.now();

        VoteValue finalVote;
        if (optionalVote.isEmpty()) {
            PostVoteEntity newVote = new PostVoteEntity();
            newVote.setPost(postEntity);
            newVote.setUser(userEntity);
            newVote.setValue(value);
            newVote.setCreatedAt(now);
            newVote.setUpdatedAt(now);
            postVoteRepository.save(newVote);
            rating += value;
            finalVote = VoteValue.fromValue(value);
        } else {
            PostVoteEntity existingVote = optionalVote.get();
            if (existingVote.getValue() != null && existingVote.getValue() == value) {
                postVoteRepository.delete(existingVote);
                rating -= value;
                finalVote = null;
            } else {
                existingVote.setValue(value);
                existingVote.setUpdatedAt(now);
                postVoteRepository.save(existingVote);
                rating += 2 * value;
                finalVote = VoteValue.fromValue(value);
            }
        }

        postEntity.setRating(rating);
        postEntity.setUpdatedAt(now);
        PostEntity savedEntity = postRepository.save(postEntity);
        Post result = postMapper.toDomain(savedEntity);
        result.setMyVote(finalVote);
        return result;
    }

    private UserEntity getCurrentUser() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository
                .findById(UUID.fromString(currentUserId))
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    private void applyUserVotes(List<Post> posts, List<UUID> postIds, UUID userId) {
        List<PostVoteEntity> votes = postVoteRepository.findByUser_IdAndPost_IdIn(userId, postIds);
        java.util.Map<UUID, VoteValue> voteMap = new java.util.HashMap<>();
        for (PostVoteEntity vote : votes) {
            UUID postId = vote.getPost() != null ? vote.getPost().getId() : null;
            if (postId != null) {
                voteMap.put(postId, VoteValue.fromValue(vote.getValue()));
            }
        }
        for (Post post : posts) {
            post.setMyVote(voteMap.get(post.getId()));
        }
    }
}
