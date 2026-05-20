package com.gordeok.idol.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_favorite_members")
@Getter
@NoArgsConstructor
public class UserFavoriteMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long memberId;

    public UserFavoriteMember(Long userId, Long memberId) {
        this.userId = userId;
        this.memberId = memberId;
    }
}
