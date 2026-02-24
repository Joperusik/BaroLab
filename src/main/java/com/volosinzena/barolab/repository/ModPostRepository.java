package com.volosinzena.barolab.repository;

import com.volosinzena.barolab.repository.entity.ModPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModPostRepository extends JpaRepository<ModPostEntity, UUID> {

    @Query("select mp from ModPostEntity mp join fetch mp.post")
    List<ModPostEntity> findAllWithPost();

    @Query("select mp from ModPostEntity mp join fetch mp.post where mp.externalId = :externalId")
    Optional<ModPostEntity> findByExternalIdWithPost(Long externalId);

    Optional<ModPostEntity> findByExternalId(Long externalId);

    boolean existsByExternalId(Long externalId);
}
