package com.volosinzena.barolab.controller;

import com.volosinzena.barolab.controller.dto.CommentDto;
import com.volosinzena.barolab.controller.dto.CreateCommentDto;
import com.volosinzena.barolab.mapper.CommentMapper;
import com.volosinzena.barolab.service.CommentService;
import com.volosinzena.barolab.service.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable UUID postId,
            @RequestBody CreateCommentDto createCommentDto) {
        Comment comment = commentService.createComment(postId, createCommentDto.getBody());
        CommentDto responseDto = commentMapper.toDto(comment);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable UUID postId) {
        List<Comment> commentList = commentService.getCommentsByPostId(postId);
        List<CommentDto> commentDtoList = commentList.stream().map(commentMapper::toDto).toList();
        return ResponseEntity.ok(commentDtoList);
    }

    @GetMapping("/post/{postId}/comment/{commentId}")
    public ResponseEntity<CommentDto> getComment(@PathVariable UUID postId,
            @PathVariable UUID commentId) {
        Comment comment = commentService.getCommentById(commentId);
        CommentDto responseDto = commentMapper.toDto(comment);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/post/{postId}/comment/{commentId}/activate")
    public ResponseEntity<CommentDto> activateComment(@PathVariable UUID postId,
            @PathVariable UUID commentId) {
        Comment comment = commentService.activateComment(commentId);
        CommentDto responseDto = commentMapper.toDto(comment);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/post/{postId}/comment/{commentId}/block")
    public ResponseEntity<CommentDto> blockComment(@PathVariable UUID postId,
            @PathVariable UUID commentId) {
        Comment comment = commentService.blockComment(commentId);
        CommentDto responseDto = commentMapper.toDto(comment);
        return ResponseEntity.ok(responseDto);
    }
}
