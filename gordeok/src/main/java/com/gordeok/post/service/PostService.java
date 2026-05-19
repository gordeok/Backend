package com.gordeok.post.service;

import com.gordeok.bookmark.repository.BookmarkRepository;
import com.gordeok.post.dto.MemberItemResponseDto;
import com.gordeok.post.dto.PostDetailResponseDto;
import com.gordeok.post.dto.SellerInfoDto;
import com.gordeok.post.dto.CreatePostRequestDto;
import com.gordeok.post.dto.CreatePostResponseDto;
import com.gordeok.post.dto.MemberItemRequestDto;
import com.gordeok.post.entity.MemberItem;
import com.gordeok.post.entity.Post;
import com.gordeok.post.repository.MemberItemRepository;
import com.gordeok.post.repository.PostRepository;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 수정: 트랜잭션 import 추가
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;
    private final UserRepository userRepository;

    @Transactional // 수정: 게시글 저장 + 멤버 가격 저장을 하나의 작업으로 처리
    public CreatePostResponseDto createPost(
            Long userId,
            CreatePostRequestDto request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("사용자를 찾을 수 없습니다.")
                );

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .idolName(request.getIdolName())
                .albumName(request.getAlbumName())
                .components(request.getComponents())
                .deliveryMethods(request.getDeliveryMethods())
                .build();

        Post savedPost = postRepository.save(post);

        for (MemberItemRequestDto itemDto : request.getMemberItems()) {

            MemberItem memberItem = MemberItem.builder()
                    .post(savedPost)
                    .memberName(itemDto.getMemberName())
                    .price(itemDto.getPrice())
                    .build();

            memberItemRepository.save(memberItem);
        }

        return new CreatePostResponseDto(
                savedPost.getId(),
                "분철 게시글이 등록되었습니다."
        );
    }

    // 추가 메서드
    public PostDetailResponseDto getPostDetail(Long postId, Long userId) {

        Post post = postRepository.findById(postId)
                .orElseThrow(() ->
                        new IllegalArgumentException("게시글을 찾을 수 없습니다.")
                );

        List<MemberItemResponseDto> memberItems = memberItemRepository.findByPostId(postId)
                .stream()
                .map(MemberItemResponseDto::new)
                .collect(Collectors.toList());

        boolean bookmarked = false;

        if (userId != null) {
            bookmarked = bookmarkRepository.existsByUserIdAndPostId(userId, postId);
        }

        return new PostDetailResponseDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getImageUrl(),
                post.getIdolName(),
                post.getAlbumName(),
                post.getComponents(),
                post.getDeliveryMethods(),
                post.getStatus(),
                // post.getViewCount(),
                post.getScrapCount(),
                new SellerInfoDto(post.getUser()),
                memberItems,
                bookmarked
        );
    }
}