package com.gordeok.post.service;

import com.gordeok.bookmark.repository.BookmarkRepository;
import com.gordeok.post.dto.*;
import com.gordeok.post.entity.MemberItem;
import com.gordeok.post.entity.Post;
import com.gordeok.post.repository.MemberItemRepository;
import com.gordeok.post.repository.PostRepository;
import com.gordeok.user.entity.User;
import com.gordeok.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;
    private final UserRepository userRepository;

    // 분철 글 작성
    @Transactional
    public CreatePostResponseDto createPost(Long userId, CreatePostRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .idolName(request.getIdolName())
                .albumName(request.getAlbumName())
                .selectionType(request.getSelectionType())
                .albumIncluded(request.getAlbumIncluded())
                .shippingFeeType(request.getShippingFeeType())
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

        return new CreatePostResponseDto(savedPost.getId(), "분철 게시글이 등록되었습니다.");
    }

    // 게시글 목록 조회 (검색, 아이돌 필터, 정렬 포함)
    public Page<PostResponseDto> getPostList(int page, int size, String keyword, String idolName, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts;

        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasIdolName = idolName != null && !idolName.isBlank();
        boolean isLatest = !"oldest".equals(sort);

        if (hasIdolName && hasKeyword) {
            posts = isLatest
                    ? postRepository.searchByIdolNameAndKeywordDesc(idolName, keyword, pageable)
                    : postRepository.searchByIdolNameAndKeywordAsc(idolName, keyword, pageable);
        } else if (hasIdolName) {
            posts = isLatest
                    ? postRepository.findByIdolNameOrderByCreatedAtDesc(idolName, pageable)
                    : postRepository.findByIdolNameOrderByCreatedAtAsc(idolName, pageable);
        } else if (hasKeyword) {
            posts = isLatest
                    ? postRepository.searchByKeywordDesc(keyword, pageable)
                    : postRepository.searchByKeywordAsc(keyword, pageable);
        } else {
            posts = isLatest
                    ? postRepository.findAllByOrderByCreatedAtDesc(pageable)
                    : postRepository.findAllByOrderByCreatedAtAsc(pageable);
        }

        return posts.map(this::buildPostResponseDto);
    }

    // 게시글 상세 조회 (북마크 여부 포함)
    public PostDetailResponseDto getPostDetail(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        List<MemberItemResponseDto> memberItems = memberItemRepository.findByPostId(postId)
                .stream()
                .map(MemberItemResponseDto::new)
                .collect(Collectors.toList());

        boolean bookmarked = userId != null && bookmarkRepository.existsByUserIdAndPostId(userId, postId);

        return new PostDetailResponseDto(
                post.getId(),
                post.getTitle(),
                post.getDescription(),
                post.getImageUrl(),
                post.getIdolName(),
                post.getAlbumName(),
                post.getSelectionType(),
                post.getShippingFeeType(),
                post.getStatus(),
                post.getScrapCount(),
                new SellerInfoDto(post.getUser()),
                memberItems,
                bookmarked
        );
    }

    // Post → PostResponseDto 변환 (공통 로직)
    private PostResponseDto buildPostResponseDto(Post post) {
        List<MemberItemResponseDto> memberItems = memberItemRepository.findByPostId(post.getId())
                .stream()
                .map(MemberItemResponseDto::new)
                .collect(Collectors.toList());
        return new PostResponseDto(post, memberItems);
    }
}
