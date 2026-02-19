package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.CommentNotFoundException;
import com.volosinzena.barolab.exception.PostNotFoundException;
import com.volosinzena.barolab.mapper.CommentMapper;
import com.volosinzena.barolab.repository.CommentRepository;
import com.volosinzena.barolab.repository.PostRepository;
import com.volosinzena.barolab.repository.UserRepository;
import com.volosinzena.barolab.service.model.Comment;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public Comment createComment(UUID postId, String body) {
        log.info("Create Comment Request for post {}", postId);

        com.volosinzena.barolab.repository.entity.PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        com.volosinzena.barolab.repository.entity.UserEntity userEntity = userRepository
                .findById(UUID.fromString(currentUserId))
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        com.volosinzena.barolab.repository.entity.CommentEntity entity = new com.volosinzena.barolab.repository.entity.CommentEntity();
        entity.setPost(postEntity);
        entity.setUser(userEntity);
        entity.setBody(body);

        Instant now = Instant.now();
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);

        com.volosinzena.barolab.repository.entity.CommentEntity savedEntity = commentRepository.save(entity);

        log.info("Successfully created comment");

        return commentMapper.toDomain(savedEntity);
    }

    @Override
    public List<Comment> getCommentsByPostId(UUID postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(commentMapper::toDomain)
                .toList();
    }

    @Override
    public Comment getCommentById(UUID commentId) {
        Optional<com.volosinzena.barolab.repository.entity.CommentEntity> optionalEntity = commentRepository
                .findById(commentId);
        if (optionalEntity.isEmpty()) {
            throw new CommentNotFoundException(commentId);
        }
        return commentMapper.toDomain(optionalEntity.get());
    }

    @Override
    public Comment activateComment(UUID commentId) {
        Optional<com.volosinzena.barolab.repository.entity.CommentEntity> optionalEntity = commentRepository
                .findById(commentId);
        if (optionalEntity.isEmpty()) {
            throw new CommentNotFoundException(commentId);
        }
        com.volosinzena.barolab.repository.entity.CommentEntity entity = optionalEntity.get();
        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        entity.setUpdatedAt(Instant.now());
        com.volosinzena.barolab.repository.entity.CommentEntity savedEntity = commentRepository.save(entity);
        return commentMapper.toDomain(savedEntity);
    }

    @Override
    public Comment blockComment(UUID commentId) {
        Optional<com.volosinzena.barolab.repository.entity.CommentEntity> optionalEntity = commentRepository
                .findById(commentId);
        if (optionalEntity.isEmpty()) {
            throw new CommentNotFoundException(commentId);
        }
        com.volosinzena.barolab.repository.entity.CommentEntity entity = optionalEntity.get();
        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.BLOCKED);
        entity.setUpdatedAt(Instant.now());
        com.volosinzena.barolab.repository.entity.CommentEntity savedEntity = commentRepository.save(entity);
        return commentMapper.toDomain(savedEntity);
    }
}
