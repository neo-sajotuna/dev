package com.sparta.newneoboardbuddy.domain.file.entity;

import com.sparta.newneoboardbuddy.domain.file.dto.request.FileUploadDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class S3File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;  // 파일 고유 ID

    @Column(nullable = false)
    private Long targetId;  // 연관된 테이블의 레코드 ID

    @Column(nullable = false, length = 50)
    private String targetTable;  // 연관된 테이블 이름 (예: "board", "user", "notification")

    @Column(nullable = false, length = 500)
    private String fileUrl;  // 파일 URL (예: S3 경로)

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;  // 소프트 삭제 여부

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // 파일 생성 시간

    // 생성자 (편의상 모든 필드를 받는 생성자 추가)
    public S3File(String fileUrl, FileUploadDto fileUploadDto) {
        this.targetId = fileUploadDto.getTargetId();
        this.targetTable = fileUploadDto.getTargetTable();
        this.fileUrl = fileUrl;
    }
}
