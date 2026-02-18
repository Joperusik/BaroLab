package com.volosinzena.barolab.controller;

import com.volosinzena.barolab.controller.dto.CreatePostDto;
import com.volosinzena.barolab.controller.dto.PostDto;
import com.volosinzena.barolab.mapper.PostMapper;
import com.volosinzena.barolab.service.PostService;
import com.volosinzena.barolab.service.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;

    @Autowired
    public PostController(PostService postService, PostMapper postMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
    }

    @PostMapping("/posts")
    public ResponseEntity<PostDto> createPost(@RequestBody CreatePostDto createPostDto) {
        Post post = postService.createPost(
                createPostDto.getTitle(),
                createPostDto.getContent()
        );

        PostDto responseDto = postMapper.toDto(post);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getPosts() {
        List<Post> postList = postService.getAllPosts();
        List<PostDto> postDtoList = postList.stream().map(postMapper::toDto).toList();
        return ResponseEntity.ok(postDtoList);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable UUID postId) {
        Post post = postService.getPostById(postId);
        PostDto responseDto = postMapper.toDto(post);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/post/{postId}/activate")
    public ResponseEntity<PostDto> activatePost(@PathVariable UUID postId) {
        Post post = postService.activatePost(postId);
        PostDto responseDto = postMapper.toDto(post);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/post/{postId}/block")
    public ResponseEntity<PostDto> blockPost(@PathVariable UUID postId) {
        Post post = postService.blockPost(postId);
        PostDto responseDto = postMapper.toDto(post);
        return ResponseEntity.ok(responseDto);
    }
}
