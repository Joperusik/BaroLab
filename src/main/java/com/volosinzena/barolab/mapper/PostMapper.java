package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.PostDto;
import com.volosinzena.barolab.service.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public PostDto toDto(Post post) {
        if (post == null) {
            return null;
        }
        return new PostDto(
                post.getId(),
                post.getUserId(),
                post.getRating(),
                post.getStatus(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt());
    }

    public Post toDomain(PostDto dto) {
        if (dto == null) {
            return null;
        }
        Post post = new Post();
        post.setId(dto.getId());
        post.setUserId(dto.getUserId());
        post.setRating(dto.getRating());
        post.setStatus(dto.getStatus());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCreatedAt(dto.getCreatedAt());
        post.setUpdatedAt(dto.getUpdatedAt());
        return post;
    }
}
