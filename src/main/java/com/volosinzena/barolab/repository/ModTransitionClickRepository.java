package com.volosinzena.barolab.repository;

import com.volosinzena.barolab.repository.entity.ModPostEntity;
import com.volosinzena.barolab.repository.entity.ModTransitionClickEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ModTransitionClickRepository extends JpaRepository<ModTransitionClickEntity, UUID> {
    Optional<ModTransitionClickEntity> findByModPostAndSubjectKey(ModPostEntity modPost, String subjectKey);
}
