package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.PostDto;
import com.volosinzena.barolab.service.model.Post;
import com.volosinzena.barolab.service.model.Status;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {


    public Post toDomain(PostDto dto) {
        if (dto == null) {
            return null;
        }
        Post post = new Post();
        post.setId(dto.getId());
        post.setUserId(dto.getUserId());
        post.setRating(dto.getRating());
        post.setStatus((Status.valueOf(dto.getStatus().name())));
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCreatedAt(dto.getCreatedAt());
        post.setUpdatedAt(dto.getUpdatedAt());
        return post;
    }

    public PostDto toDto(Post post) {
        if (post == null) {
            return null;
        }
        PostDto dto = new PostDto();

                dto.setId(post.getId());
                dto.setUserId(post.getUserId());
                dto.setRating(post.getRating());
                dto.setStatus(com.volosinzena.barolab.controller.dto.Status.valueOf(String.valueOf(Status.valueOf(post.getStatus().name()))));
                dto.setTitle(post.getTitle());
                dto.setTitle(post.getTitle());
                dto.setContent(post.getContent());
                dto.setCreatedAt(post.getCreatedAt());
                dto.setUpdatedAt(post.getUpdatedAt());

                return dto;
    }
}
