package com.gordeok.idol.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "idol_members")
@Getter
@NoArgsConstructor
public class IdolMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idolId;

    @Column(nullable = false)
    private String name;

    private LocalDateTime createdAt;
}
