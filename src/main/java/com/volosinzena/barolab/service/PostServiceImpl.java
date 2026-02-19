package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.PostNotFoundException;
import com.volosinzena.barolab.mapper.PostMapper;
import com.volosinzena.barolab.repository.PostRepository;
import com.volosinzena.barolab.repository.UserRepository;
import com.volosinzena.barolab.service.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toDomain)
                .toList();
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
        return postMapper.toDomain(optionalEntity.get());
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
}
