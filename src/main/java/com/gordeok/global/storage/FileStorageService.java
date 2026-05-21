package com.gordeok.global.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Value("${file.upload.url-prefix}")
    private String urlPrefix;

    /**
     * 파일을 로컬 디스크에 저장하고 접근 URL을 반환한다.
     *
     * @param file      업로드된 MultipartFile
     * @param subDir    하위 디렉토리 (예: "profiles", "reports")
     * @return          외부에서 접근 가능한 URL (예: /uploads/profiles/uuid_filename.jpg)
     */
    public String store(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        try {
            // 저장 디렉토리 생성
            Path targetDir = Paths.get(uploadDir, subDir).toAbsolutePath().normalize();
            Files.createDirectories(targetDir);

            // 파일명 충돌 방지: UUID 접두사
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String storedFilename = UUID.randomUUID() + extension;

            // 파일 저장
            Path targetPath = targetDir.resolve(storedFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // 접근 URL 반환
            return urlPrefix + "/" + subDir + "/" + storedFilename;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다: " + e.getMessage(), e);
        }
    }

    /**
     * 저장된 파일 삭제 (URL로 역산)
     */
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        try {
            // "/uploads/profiles/uuid.jpg" → "./uploads/profiles/uuid.jpg"
            String relativePath = fileUrl.replaceFirst(urlPrefix, uploadDir);
            Path filePath = Paths.get(relativePath).toAbsolutePath().normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // 삭제 실패는 무시 (파일이 없을 수도 있음)
        }
    }
}
