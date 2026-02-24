package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.ModNotFoundException;
import com.volosinzena.barolab.repository.ModPostRepository;
import com.volosinzena.barolab.repository.ModTransitionClickRepository;
import com.volosinzena.barolab.repository.entity.ModPostEntity;
import com.volosinzena.barolab.repository.entity.ModTransitionClickEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
public class ModTransitionServiceImpl implements ModTransitionService {

    private static final Duration COOLDOWN = Duration.ofHours(1);

    private final ModPostRepository modPostRepository;
    private final ModTransitionClickRepository modTransitionClickRepository;

    @Autowired
    public ModTransitionServiceImpl(ModPostRepository modPostRepository,
            ModTransitionClickRepository modTransitionClickRepository) {
        this.modPostRepository = modPostRepository;
        this.modTransitionClickRepository = modTransitionClickRepository;
    }

    @Override
    @Transactional
    public boolean registerClick(Long externalId, String subjectKey) {
        if (subjectKey == null || subjectKey.isBlank()) {
            throw new IllegalArgumentException("subject key is required");
        }
        ModPostEntity modPost = modPostRepository.findByExternalId(externalId)
                .orElseThrow(() -> new ModNotFoundException(externalId));

        Instant now = Instant.now();
        ModTransitionClickEntity clickEntity = modTransitionClickRepository
                .findByModPostAndSubjectKey(modPost, subjectKey)
                .orElse(null);

        if (clickEntity != null) {
            Instant lastClickAt = clickEntity.getLastClickAt();
            if (lastClickAt != null && lastClickAt.isAfter(now.minus(COOLDOWN))) {
                return false;
            }
            clickEntity.setLastClickAt(now);
            clickEntity.setClicksCount(clickEntity.getClicksCount() == null ? 1 : clickEntity.getClicksCount() + 1);
            modTransitionClickRepository.save(clickEntity);
        } else {
            ModTransitionClickEntity newClick = new ModTransitionClickEntity();
            newClick.setModPost(modPost);
            newClick.setSubjectKey(subjectKey);
            newClick.setClicksCount(1);
            newClick.setCreatedAt(now);
            newClick.setLastClickAt(now);
            modTransitionClickRepository.save(newClick);
        }

        Integer currentCount = modPost.getPopularity();
        modPost.setPopularity(currentCount == null ? 1 : currentCount + 1);
        modPostRepository.save(modPost);
        log.info("Mod transition click registered externalId={} subjectKey={}", externalId, subjectKey);
        return true;
    }
}
