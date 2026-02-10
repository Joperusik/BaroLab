package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.PostNotFoundException;
import com.volosinzena.barolab.service.model.Post;
import com.volosinzena.barolab.service.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final HashMap<UUID, Post> postHashMap = new HashMap<>();

    @Override
    public List<Post> getAllPosts() {
        return postHashMap.values().stream().toList();
    }

    @Override
    public Post createPost(Post post) {
        log.info("Create Post Request");

        post.setId(UUID.randomUUID());
        Instant now = Instant.now();
        post.setCreatedAt(now);
        post.setUpdatedAt(now);
        post.setStatus(Status.ACTIVE);

        if (post.getRating() == null) {
            post.setRating(0);
        }

        postHashMap.put(post.getId(), post);

        log.info("Successfully created post");

        return post;
    }

    @Override
    public Post getPostById(UUID postId) {
        Post post = postHashMap.get(postId);
        if (post == null) {
            throw new PostNotFoundException(postId);
        }
        return post;
    }

    @Override
    public Post activatePost(UUID postId) {
        Post post = getPostById(postId);
        post.setStatus(Status.ACTIVE);
        post.setUpdatedAt(Instant.now());
        postHashMap.put(post.getId(), post);
        return post;
    }

    @Override
    public Post blockPost(UUID postId) {
        Post post = getPostById(postId);
        post.setStatus(Status.BLOCKED);
        post.setUpdatedAt(Instant.now());
        postHashMap.put(post.getId(), post);
        return post;
    }
}
