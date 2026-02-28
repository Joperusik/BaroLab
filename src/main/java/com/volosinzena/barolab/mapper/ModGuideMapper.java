package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.ModGuideResponse;
import com.volosinzena.barolab.repository.entity.ModGuideEntity;
import com.volosinzena.barolab.service.model.ModGuide;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModGuideMapper {

    private final UserMapper userMapper;

    public ModGuide toDomain(ModGuideEntity entity) {
        if (entity == null) {
            return null;
        }

        ModGuide modGuide = new ModGuide();
        modGuide.setId(entity.getId());
        if (entity.getModPost() != null) {
            modGuide.setModId(entity.getModPost().getExternalId());
        }
        modGuide.setTitle(entity.getTitle());
        modGuide.setContent(entity.getContent());
        modGuide.setAuthor(userMapper.toDomain(entity.getAuthor()));
        modGuide.setStatus(com.volosinzena.barolab.service.model.Status.valueOf(entity.getStatus().name()));
        modGuide.setCreatedAt(entity.getCreatedAt());
        modGuide.setUpdatedAt(entity.getUpdatedAt());

        return modGuide;
    }

    public ModGuideResponse toDto(ModGuide modGuide) {
        if (modGuide == null) {
            return null;
        }

        ModGuideResponse response = new ModGuideResponse();
        response.setId(modGuide.getId());
        response.setModId(modGuide.getModId());
        response.setTitle(modGuide.getTitle());
        response.setContent(modGuide.getContent());
        response.setAuthor(userMapper.toDto(modGuide.getAuthor()));
        response.setStatus(com.volosinzena.barolab.controller.dto.Status.valueOf(modGuide.getStatus().name()));
        response.setCreatedAt(modGuide.getCreatedAt());
        response.setUpdatedAt(modGuide.getUpdatedAt());

        return response;
    }
}
