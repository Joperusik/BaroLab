package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.PostDto;
import com.volosinzena.barolab.controller.dto.VoteValue;
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
        if (dto.getMyVote() != null) {
            post.setMyVote(com.volosinzena.barolab.service.model.VoteValue.valueOf(dto.getMyVote().name()));
        }
        post.setAuthorUsername(dto.getAuthorUsername());
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
        if (post.getMyVote() != null) {
            dto.setMyVote(VoteValue.valueOf(post.getMyVote().name()));
        }
        dto.setAuthorUsername(post.getAuthorUsername());
        dto.setStatus(com.volosinzena.barolab.controller.dto.Status
                .valueOf(String.valueOf(Status.valueOf(post.getStatus().name()))));
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());

        return dto;
    }

    public Post toDomain(com.volosinzena.barolab.repository.entity.PostEntity entity) {
        if (entity == null) {
            return null;
        }
        Post post = new Post();
        post.setId(entity.getId());
        post.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        post.setAuthorUsername(entity.getUser() != null ? entity.getUser().getUsername() : null);
        post.setRating(entity.getRating());
        if (entity.getStatus() != null) {
            post.setStatus(Status.valueOf(entity.getStatus().name()));
        }
        post.setTitle(entity.getTitle());
        post.setContent(entity.getContent());
        post.setCreatedAt(entity.getCreatedAt());
        post.setUpdatedAt(entity.getUpdatedAt());
        return post;
    }

    public com.volosinzena.barolab.repository.entity.PostEntity toEntity(Post post) {
        if (post == null) {
            return null;
        }
        com.volosinzena.barolab.repository.entity.PostEntity entity = new com.volosinzena.barolab.repository.entity.PostEntity();
        entity.setId(post.getId());
        // Note: user relationship must be set separately by the caller
        entity.setRating(post.getRating());
        if (post.getStatus() != null) {
            entity.setStatus(com.volosinzena.barolab.repository.entity.Status.valueOf(post.getStatus().name()));
        }
        entity.setTitle(post.getTitle());
        entity.setContent(post.getContent());
        entity.setCreatedAt(post.getCreatedAt());
        entity.setUpdatedAt(post.getUpdatedAt());
        return entity;
    }
}
