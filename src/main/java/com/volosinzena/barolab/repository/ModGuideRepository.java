package com.volosinzena.barolab.repository;

import com.volosinzena.barolab.repository.entity.ModGuideEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModGuideRepository extends JpaRepository<ModGuideEntity, UUID> {
    List<ModGuideEntity> findAllByModPost_ExternalId(Long modId);
}
