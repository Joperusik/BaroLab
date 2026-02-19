package com.volosinzena.barolab.service;

import com.volosinzena.barolab.service.model.Comment;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    Comment createComment(UUID postId, String body);

    List<Comment> getCommentsByPostId(UUID postId);

    Comment getCommentById(UUID commentId);

    Comment activateComment(UUID commentId);

    Comment blockComment(UUID commentId);
}
