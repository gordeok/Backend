package com.gordeok.post.service;

import com.gordeok.bookmark.repository.BookmarkRepository;
import com.gordeok.global.AiImageClient;
import com.gordeok.idol.dto.IdolMemberResponseDto;
import com.gordeok.idol.entity.Idol;
import com.gordeok.idol.repository.IdolMemberRepository;
import com.gordeok.idol.repository.IdolRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;
    private final UserRepository userRepository;
    private final AiImageClient aiImageClient;
    private final IdolRepository idolRepository;
    private final IdolMemberRepository idolMemberRepository;

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
                .components(request.getComponents())
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
                post.getComponents(),
                post.getShippingFeeType(),
                post.getStatus(),
                post.getScrapCount(),
                new SellerInfoDto(post.getUser()),
                memberItems,
                bookmarked
        );
    }

    private PostResponseDto buildPostResponseDto(Post post) {
        List<MemberItemResponseDto> memberItems = memberItemRepository.findByPostId(post.getId())
                .stream()
                .map(MemberItemResponseDto::new)
                .collect(Collectors.toList());
        return new PostResponseDto(post, memberItems);
    }

    public ImageAnalyzeResponseDto analyzeImage(MultipartFile image) throws IOException {
        AiPredictResponse aiResult = aiImageClient.predict(image);

        List<Idol> idols = idolRepository.findByNameContainingOrderByNameAsc(aiResult.getGroup());

        if (idols.isEmpty()) {
            return new ImageAnalyzeResponseDto(
                    aiResult.getGroup(),
                    aiResult.getProduct(),
                    List.of(),
                    aiResult.isConfident()
            );
        }

        Long idolId = idols.get(0).getId();
        List<IdolMemberResponseDto> members = idolMemberRepository.findByIdolId(idolId)
                .stream()
                .map(IdolMemberResponseDto::new)
                .toList();

        return new ImageAnalyzeResponseDto(
                aiResult.getGroup(),
                aiResult.getProduct(),
                members,
                aiResult.isConfident()
        );
    }
}