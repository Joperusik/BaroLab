package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.ModPostDto;
import com.volosinzena.barolab.controller.dto.VoteValue;
import com.volosinzena.barolab.repository.entity.ModPostEntity;
import com.volosinzena.barolab.repository.entity.PostEntity;
import com.volosinzena.barolab.service.model.ModPost;
import com.volosinzena.barolab.service.model.Status;
import org.springframework.stereotype.Component;

@Component
public class ModPostMapper {

    public ModPost toDomain(ModPostEntity entity) {
        if (entity == null) {
            return null;
        }
        PostEntity postEntity = entity.getPost();
        if (postEntity == null) {
            return null;
        }
        ModPost modPost = new ModPost();
        modPost.setId(postEntity.getId());
        modPost.setUserId(postEntity.getUser() != null ? postEntity.getUser().getId() : null);
        modPost.setAuthorUsername(postEntity.getUser() != null ? postEntity.getUser().getUsername() : null);
        modPost.setRating(postEntity.getRating());
        if (postEntity.getStatus() != null) {
            modPost.setStatus(Status.valueOf(postEntity.getStatus().name()));
        }
        modPost.setTitle(postEntity.getTitle());
        modPost.setContent(postEntity.getContent());
        modPost.setCreatedAt(postEntity.getCreatedAt());
        modPost.setUpdatedAt(postEntity.getUpdatedAt());
        modPost.setExternalId(entity.getExternalId());
        modPost.setPopularity(entity.getPopularity());
        return modPost;
    }

    public ModPostDto toDto(ModPost modPost) {
        if (modPost == null) {
            return null;
        }
        ModPostDto dto = new ModPostDto();
        dto.setId(modPost.getId());
        dto.setUserId(modPost.getUserId());
        dto.setAuthorUsername(modPost.getAuthorUsername());
        dto.setRating(modPost.getRating());
        if (modPost.getMyVote() != null) {
            dto.setMyVote(VoteValue.valueOf(modPost.getMyVote().name()));
        }
        if (modPost.getStatus() != null) {
            dto.setStatus(com.volosinzena.barolab.controller.dto.Status.valueOf(modPost.getStatus().name()));
        }
        dto.setTitle(modPost.getTitle());
        dto.setContent(modPost.getContent());
        dto.setCreatedAt(modPost.getCreatedAt());
        dto.setUpdatedAt(modPost.getUpdatedAt());
        dto.setExternalId(modPost.getExternalId());
        dto.setPopularity(modPost.getPopularity());
        return dto;
    }
}
