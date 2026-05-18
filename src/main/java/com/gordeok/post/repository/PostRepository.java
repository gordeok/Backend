package com.gordeok.post.repository;

import com.gordeok.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 최신순
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 전체 오래된순
    Page<Post> findAllByOrderByCreatedAtAsc(Pageable pageable);

    // 통합 검색 (제목 + 아이돌명 + 앨범명) 최신순
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.idolName LIKE %:keyword% OR p.albumName LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Post> searchByKeywordDesc(@Param("keyword") String keyword, Pageable pageable);

    // 통합 검색 (제목 + 아이돌명 + 앨범명) 오래된순
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.idolName LIKE %:keyword% OR p.albumName LIKE %:keyword% ORDER BY p.createdAt ASC")
    Page<Post> searchByKeywordAsc(@Param("keyword") String keyword, Pageable pageable);

    // 아이돌별 최신순
    Page<Post> findByIdolNameOrderByCreatedAtDesc(String idolName, Pageable pageable);

    // 아이돌별 오래된순
    Page<Post> findByIdolNameOrderByCreatedAtAsc(String idolName, Pageable pageable);

    // 아이돌별 + 통합 검색 최신순
    @Query("SELECT p FROM Post p WHERE p.idolName = :idolName AND (p.title LIKE %:keyword% OR p.albumName LIKE %:keyword%) ORDER BY p.createdAt DESC")
    Page<Post> searchByIdolNameAndKeywordDesc(@Param("idolName") String idolName, @Param("keyword") String keyword, Pageable pageable);

    // 아이돌별 + 통합 검색 오래된순
    @Query("SELECT p FROM Post p WHERE p.idolName = :idolName AND (p.title LIKE %:keyword% OR p.albumName LIKE %:keyword%) ORDER BY p.createdAt ASC")
    Page<Post> searchByIdolNameAndKeywordAsc(@Param("idolName") String idolName, @Param("keyword") String keyword, Pageable pageable);
}
