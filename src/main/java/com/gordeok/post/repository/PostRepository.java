package com.gordeok.post.repository;

import com.gordeok.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ── 기존 홈화면 조회 ──
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Post> findAllByOrderByCreatedAtAsc(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.idolName LIKE %:keyword% OR p.albumName LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Post> searchByKeywordDesc(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.idolName LIKE %:keyword% OR p.albumName LIKE %:keyword% ORDER BY p.createdAt ASC")
    Page<Post> searchByKeywordAsc(@Param("keyword") String keyword, Pageable pageable);

    Page<Post> findByIdolNameOrderByCreatedAtDesc(String idolName, Pageable pageable);
    Page<Post> findByIdolNameOrderByCreatedAtAsc(String idolName, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.idolName = :idolName AND (p.title LIKE %:keyword% OR p.albumName LIKE %:keyword%) ORDER BY p.createdAt DESC")
    Page<Post> searchByIdolNameAndKeywordDesc(@Param("idolName") String idolName, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.idolName = :idolName AND (p.title LIKE %:keyword% OR p.albumName LIKE %:keyword%) ORDER BY p.createdAt ASC")
    Page<Post> searchByIdolNameAndKeywordAsc(@Param("idolName") String idolName, @Param("keyword") String keyword, Pageable pageable);

    // ── 마이페이지: 판매 목록 (내가 작성한 Post, status 필터) ──
    Page<Post> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, String status, Pageable pageable);

    // ── 판매자 프로필: 최근 게시글 썸네일 (최대 4개) ──
    List<Post> findTop4ByUserIdOrderByCreatedAtDesc(Long userId);

    // ── 판매 완료된 Post의 참여자 수 (후기 보기용 보조) ──
    @Query("SELECT COUNT(p) FROM Participation p WHERE p.post.id = :postId")
    Long countParticipationsByPostId(@Param("postId") Long postId);
}
