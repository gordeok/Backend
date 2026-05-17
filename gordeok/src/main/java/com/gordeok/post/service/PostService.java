package com.gordeok.post.service;

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

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;
    private final UserRepository userRepository;

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

        return new CreatePostResponseDto(
                savedPost.getId(),
                "분철 게시글이 등록되었습니다."
        );
    }
}