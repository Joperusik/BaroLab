package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.GuideNotFoundException;
import com.volosinzena.barolab.exception.ModNotFoundException;
import com.volosinzena.barolab.mapper.ModGuideMapper;
import com.volosinzena.barolab.repository.ModGuideRepository;
import com.volosinzena.barolab.repository.ModPostRepository;
import com.volosinzena.barolab.repository.UserRepository;
import com.volosinzena.barolab.repository.entity.ModGuideEntity;
import com.volosinzena.barolab.repository.entity.ModPostEntity;
import com.volosinzena.barolab.repository.entity.UserEntity;
import com.volosinzena.barolab.service.model.ModGuide;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModGuideServiceImpl implements ModGuideService {

    private final ModGuideRepository modGuideRepository;
    private final ModPostRepository modPostRepository;
    private final UserRepository userRepository;
    private final ModGuideMapper modGuideMapper;

    @Override
    public List<ModGuide> getGuidesByModId(Long modId) {
        return modGuideRepository.findAllByModPost_ExternalId(modId)
                .stream()
                .map(modGuideMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public ModGuide getGuideById(UUID guideId) {
        ModGuideEntity entity = modGuideRepository.findById(guideId)
                .orElseThrow(() -> new GuideNotFoundException(guideId));
        return modGuideMapper.toDomain(entity);
    }

    @Override
    @Transactional
    public ModGuide createGuide(Long modId, String title, String content) {
        log.info("Creating guide for modId: {}", modId);

        ModPostEntity modPost = modPostRepository.findByExternalId(modId)
                .orElseThrow(() -> new ModNotFoundException(modId));

        UserEntity currentUser = getCurrentUser();
        Instant now = Instant.now();

        ModGuideEntity entity = new ModGuideEntity();
        entity.setModPost(modPost);
        entity.setTitle(title);
        entity.setContent(content);
        entity.setAuthor(currentUser);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        ModGuideEntity savedEntity = modGuideRepository.save(entity);
        return modGuideMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public ModGuide updateGuide(UUID guideId, String title, String content) {
        log.info("Updating guide with id: {}", guideId);

        ModGuideEntity entity = modGuideRepository.findById(guideId)
                .orElseThrow(() -> new GuideNotFoundException(guideId));

        UserEntity currentUser = getCurrentUser();
        Instant now = Instant.now();

        entity.setTitle(title);
        entity.setContent(content);
        entity.setUpdatedAt(now);
        entity.setAuthor(currentUser);

        ModGuideEntity savedEntity = modGuideRepository.save(entity);
        return modGuideMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void deleteGuide(UUID guideId) {
        log.info("Deleting guide with id: {}", guideId);
        if (!modGuideRepository.existsById(guideId)) {
            throw new GuideNotFoundException(guideId);
        }
        modGuideRepository.deleteById(guideId);
    }

    @Override
    public List<ModGuide> getAllGuides() {
        return modGuideRepository.findAll()
                .stream()
                .map(modGuideMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ModGuide activateGuide(UUID guideId) {
        log.info("Activating guide with id: {}", guideId);
        ModGuideEntity entity = modGuideRepository.findById(guideId)
                .orElseThrow(() -> new GuideNotFoundException(guideId));

        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        entity.setUpdatedAt(Instant.now());

        ModGuideEntity savedEntity = modGuideRepository.save(entity);
        return modGuideMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public ModGuide blockGuide(UUID guideId) {
        log.info("Blocking guide with id: {}", guideId);
        ModGuideEntity entity = modGuideRepository.findById(guideId)
                .orElseThrow(() -> new GuideNotFoundException(guideId));

        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.BLOCKED);
        entity.setUpdatedAt(Instant.now());

        ModGuideEntity savedEntity = modGuideRepository.save(entity);
        return modGuideMapper.toDomain(savedEntity);
    }

    private UserEntity getCurrentUser() {
        String currentUserId = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(UUID.fromString(currentUserId))
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }
}
