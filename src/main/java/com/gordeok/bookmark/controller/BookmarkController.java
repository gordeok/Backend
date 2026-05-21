package com.gordeok.bookmark.controller;

import com.gordeok.bookmark.dto.BookmarkResponseDto;
import com.gordeok.bookmark.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 북마크 추가/제거 (토글)
    // POST /api/bookmarks/toggle?userId=1&postId=1
    @PostMapping("/toggle")
    public ResponseEntity<Map<String, Object>> toggleBookmark(
            @RequestParam Long userId,
            @RequestParam Long postId) {
        boolean bookmarked = bookmarkService.toggleBookmark(userId, postId);
        return ResponseEntity.ok(Map.of(
                "bookmarked", bookmarked,
                "message", bookmarked ? "북마크가 추가되었습니다." : "북마크가 해제되었습니다."
        ));
    }

    // 북마크 여부 확인
    // GET /api/bookmarks/check?userId=1&postId=1
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkBookmark(
            @RequestParam Long userId,
            @RequestParam Long postId) {
        boolean bookmarked = bookmarkService.isBookmarked(userId, postId);
        return ResponseEntity.ok(Map.of("bookmarked", bookmarked));
    }

    // 북마크 목록 조회
    // GET /api/bookmarks?userId=1
    @GetMapping
    public ResponseEntity<List<BookmarkResponseDto>> getBookmarkList(@RequestParam Long userId) {
        return ResponseEntity.ok(bookmarkService.getBookmarkList(userId));
    }
}
