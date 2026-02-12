package com.volosinzena.barolab.repository.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private UUID userId;
    @Column
    private Integer rating;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private Instant createdAt;
    @Column
    private Instant updatedAt;
}
