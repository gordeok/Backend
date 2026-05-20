package com.gordeok.bookmark.service;

import com.gordeok.bookmark.dto.BookmarkResponseDto;
import com.gordeok.bookmark.entity.Bookmark;
import com.gordeok.bookmark.repository.BookmarkRepository;
import com.gordeok.post.dto.MemberItemResponseDto;
import com.gordeok.post.entity.Post;
import com.gordeok.post.repository.MemberItemRepository;
import com.gordeok.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final PostRepository postRepository;
    private final MemberItemRepository memberItemRepository;

    // 북마크 추가/제거 (토글)
    @Transactional
    public boolean toggleBookmark(Long userId, Long postId) {
        Optional<Bookmark> existing = bookmarkRepository.findByUserIdAndPostId(userId, postId);

        if (existing.isPresent()) {
            // 이미 북마크 → 제거
            bookmarkRepository.delete(existing.get());
            return false; // 북마크 해제
        } else {
            // 북마크 없음 → 추가
            bookmarkRepository.save(new Bookmark(userId, postId));
            return true; // 북마크 추가
        }
    }

    // 북마크 여부 조회
    public boolean isBookmarked(Long userId, Long postId) {
        return bookmarkRepository.existsByUserIdAndPostId(userId, postId);
    }

    // 북마크 목록 조회
    public List<BookmarkResponseDto> getBookmarkList(Long userId) {
        return bookmarkRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(bookmark -> {
                    Post post = postRepository.findById(bookmark.getPostId())
                            .orElse(null);
                    if (post == null) return null;

                    List<MemberItemResponseDto> memberItems = memberItemRepository
                            .findByPostId(post.getId())
                            .stream()
                            .map(MemberItemResponseDto::new)
                            .collect(Collectors.toList());

                    return new BookmarkResponseDto(bookmark.getId(), post, memberItems);
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }
}
