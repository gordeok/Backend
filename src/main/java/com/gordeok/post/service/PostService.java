package com.gordeok.post.service;

import com.gordeok.post.dto.MemberItemResponseDto;
import com.gordeok.post.dto.PostResponseDto;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;
    private final UserRepository userRepository;

    // 게시글 목록 조회 (홈화면)
    public Page<PostResponseDto> getPostList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);

        return posts.map(post -> {
            String nickname = userRepository.findById(post.getUserId())
                    .map(User::getNickname)
                    .orElse("알 수 없음");

            List<MemberItemResponseDto> memberItems = memberItemRepository.findByPostId(post.getId())
                    .stream()
                    .map(MemberItemResponseDto::new)
                    .collect(Collectors.toList());

            return new PostResponseDto(post, nickname, memberItems);
        });
    }

    // 게시글 상세 조회
    public PostResponseDto getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        String nickname = userRepository.findById(post.getUserId())
                .map(User::getNickname)
                .orElse("알 수 없음");

        List<MemberItemResponseDto> memberItems = memberItemRepository.findByPostId(post.getId())
                .stream()
                .map(MemberItemResponseDto::new)
                .collect(Collectors.toList());

        return new PostResponseDto(post, nickname, memberItems);
    }
}
