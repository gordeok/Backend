package com.gordeok.post.service;

import com.gordeok.post.dto.MemberItemResponseDto;
import com.gordeok.post.dto.PostResponseDto;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;
    private final UserRepository userRepository;

    // 게시글 목록 조회 (검색, 아이돌 필터, 정렬 포함)
    public Page<PostResponseDto> getPostList(int page, int size, String keyword, Long idolId, String sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts;

        boolean hasKeyword = keyword != null && !keyword.isBlank();
        boolean hasIdolId = idolId != null;
        boolean isLatest = !"oldest".equals(sort);

        if (hasIdolId && hasKeyword) {
            posts = isLatest
                    ? postRepository.findByIdolIdAndTitleContainingOrderByCreatedAtDesc(idolId, keyword, pageable)
                    : postRepository.findByIdolIdAndTitleContainingOrderByCreatedAtAsc(idolId, keyword, pageable);
        } else if (hasIdolId) {
            posts = isLatest
                    ? postRepository.findByIdolIdOrderByCreatedAtDesc(idolId, pageable)
                    : postRepository.findByIdolIdOrderByCreatedAtAsc(idolId, pageable);
        } else if (hasKeyword) {
            posts = isLatest
                    ? postRepository.findByTitleContainingOrderByCreatedAtDesc(keyword, pageable)
                    : postRepository.findByTitleContainingOrderByCreatedAtAsc(keyword, pageable);
        } else {
            posts = isLatest
                    ? postRepository.findAllByOrderByCreatedAtDesc(pageable)
                    : postRepository.findAllByOrderByCreatedAtAsc(pageable);
        }

        return posts.map(post -> buildPostResponseDto(post));
    }

    // 게시글 상세 조회
    public PostResponseDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return buildPostResponseDto(post);
    }

    // Post -> PostResponseDto 변환 (공통 로직)
    private PostResponseDto buildPostResponseDto(Post post) {
        User user = userRepository.findById(post.getUserId()).orElse(null);
        String nickname = user != null ? user.getNickname() : "알 수 없음";
        String profileImage = user != null ? user.getProfileImage() : null;

        List<MemberItemResponseDto> memberItems = memberItemRepository.findByPostId(post.getId())
                .stream()
                .map(MemberItemResponseDto::new)
                .collect(Collectors.toList());

        return new PostResponseDto(post, nickname, profileImage, memberItems);
    }
}
