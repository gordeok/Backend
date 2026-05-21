package com.gordeok.idol.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_favorite_idols")
@Getter
@NoArgsConstructor
public class UserFavoriteIdol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long idolId;

    public UserFavoriteIdol(Long userId, Long idolId) {
        this.userId = userId;
        this.idolId = idolId;
    }
}
