package com.volosinzena.barolab.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "mod_posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModPostEntity {
    @Id
    private UUID postId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @Column(nullable = false, unique = true)
    private Long externalId;

    @Column(nullable = false)
    private Integer popularity;
}
