package com.volosinzena.barolab.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "mod_transition_clicks",
        uniqueConstraints = @UniqueConstraint(name = "uk_mod_transitions_mod_subject", columnNames = {"mod_post_id", "subject_key"})
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModTransitionClickEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_post_id", nullable = false)
    private ModPostEntity modPost;

    @Column(nullable = false)
    private String subjectKey;

    @Column(nullable = false)
    private Integer clicksCount;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant lastClickAt;
}
