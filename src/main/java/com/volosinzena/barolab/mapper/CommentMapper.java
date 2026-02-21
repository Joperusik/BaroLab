package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.CommentDto;
import com.volosinzena.barolab.service.model.Comment;
import com.volosinzena.barolab.service.model.Status;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public Comment toDomain(CommentDto dto) {
        if (dto == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(java.util.UUID.fromString(dto.getId()));
        comment.setPostId(java.util.UUID.fromString(dto.getPostId()));
        comment.setUserId(java.util.UUID.fromString(dto.getUserId()));
        comment.setAuthorUsername(dto.getAuthorUsername());
        comment.setBody(dto.getBody());
        if (dto.getStatus() != null) {
            comment.setStatus(Status.valueOf(dto.getStatus().name()));
        }
        comment.setCreatedAt(dto.getCreatedAt());
        comment.setUpdatedAt(dto.getUpdatedAt());
        return comment;
    }

    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId() != null ? comment.getId().toString() : null);
        dto.setPostId(comment.getPostId() != null ? comment.getPostId().toString() : null);
        dto.setUserId(comment.getUserId() != null ? comment.getUserId().toString() : null);
        dto.setAuthorUsername(comment.getAuthorUsername());
        dto.setBody(comment.getBody());
        if (comment.getStatus() != null) {
            dto.setStatus(com.volosinzena.barolab.controller.dto.Status
                    .valueOf(comment.getStatus().name()));
        }
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUpdatedAt(comment.getUpdatedAt());
        return dto;
    }

    public Comment toDomain(com.volosinzena.barolab.repository.entity.CommentEntity entity) {
        if (entity == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(entity.getId());
        comment.setPostId(entity.getPost() != null ? entity.getPost().getId() : null);
        comment.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        comment.setAuthorUsername(entity.getUser() != null ? entity.getUser().getUsername() : null);
        comment.setBody(entity.getBody());
        if (entity.getStatus() != null) {
            comment.setStatus(Status.valueOf(entity.getStatus().name()));
        }
        comment.setCreatedAt(entity.getCreatedAt());
        comment.setUpdatedAt(entity.getUpdatedAt());
        return comment;
    }

    public com.volosinzena.barolab.repository.entity.CommentEntity toEntity(Comment comment) {
        if (comment == null) {
            return null;
        }
        com.volosinzena.barolab.repository.entity.CommentEntity entity = new com.volosinzena.barolab.repository.entity.CommentEntity();
        entity.setId(comment.getId());
        // Note: post and user relationships must be set separately by the caller
        entity.setBody(comment.getBody());
        if (comment.getStatus() != null) {
            entity.setStatus(com.volosinzena.barolab.repository.entity.Status.valueOf(comment.getStatus().name()));
        }
        entity.setCreatedAt(comment.getCreatedAt());
        entity.setUpdatedAt(comment.getUpdatedAt());
        return entity;
    }
}
