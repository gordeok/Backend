package com.gordeok.user.entity;
// users 테이블 역할

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 화면의 '아이디'
    @Column(nullable = false, unique = true)
    private String email;

    // 암호화된 비밀번호 저장
    @Column(nullable = false)
    private String password;

    // 화면의 '이름'
    @Column(nullable = false)
    private String nickname;

    private String profileImage;

    @Builder.Default
    private Integer trustScore = 0;
}