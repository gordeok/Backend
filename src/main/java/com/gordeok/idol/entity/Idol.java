package com.gordeok.idol.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "idols")
@Getter
@NoArgsConstructor
public class Idol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // 프론트엔드 string ID와 호환 (예: "boynextdoor", "bts", "newjeans")
    private String code;

    private LocalDateTime createdAt;
}
