package com.gordeok.post.service;

import com.gordeok.post.dto.MemberItemResponseDto;
import com.gordeok.post.dto.PostResponseDto;
import com.gordeok.post.entity.Post;
import com.gordeok.post.repository.MemberItemRepository;
import com.gordeok.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;

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

    // 게시글 상세 조회
    public PostResponseDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return buildPostResponseDto(post);
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
