package com.volosinzena.barolab.repository;

import com.volosinzena.barolab.repository.entity.PostVoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostVoteRepository extends JpaRepository<PostVoteEntity, UUID> {

    Optional<PostVoteEntity> findByPost_IdAndUser_Id(UUID postId, UUID userId);

    List<PostVoteEntity> findByUser_IdAndPost_IdIn(UUID userId, Collection<UUID> postIds);
}
