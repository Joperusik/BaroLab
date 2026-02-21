package com.volosinzena.barolab.service;

import com.volosinzena.barolab.service.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {

    List<Post> getAllPosts();

    Post createPost(String title, String content);

    Post getPostById(UUID postId);

    Post activatePost(UUID postId);

    Post blockPost(UUID postId);

    Post likePost(UUID postId);

    Post dislikePost(UUID postId);
}
