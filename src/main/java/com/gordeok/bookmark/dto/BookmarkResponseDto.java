package com.gordeok.bookmark.dto;

import com.gordeok.post.dto.MemberItemResponseDto;
import com.gordeok.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BookmarkResponseDto {

    private Long bookmarkId;
    private Long postId;
    private String title;
    private String idolName;
    private String albumName;
    private String imageUrl;
    private String nickname;       // 판매자 닉네임
    private String status;
    private LocalDateTime createdAt;
    private List<MemberItemResponseDto> memberItems;

    public BookmarkResponseDto(Long bookmarkId, Post post, List<MemberItemResponseDto> memberItems) {
        this.bookmarkId = bookmarkId;
        this.postId = post.getId();
        this.title = post.getTitle();
        this.idolName = post.getIdolName();
        this.albumName = post.getAlbumName();
        this.imageUrl = post.getImageUrl();
        this.nickname = post.getUser() != null ? post.getUser().getNickname() : "알 수 없음";
        this.status = post.getStatus();
        this.createdAt = post.getCreatedAt();
        this.memberItems = memberItems;
    }
}
