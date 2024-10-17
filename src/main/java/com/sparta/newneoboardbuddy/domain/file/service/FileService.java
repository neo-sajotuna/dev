package com.sparta.newneoboardbuddy.domain.file.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.newneoboardbuddy.common.dto.AuthUser;
import com.sparta.newneoboardbuddy.domain.file.dto.request.FileDeleteDto;
import com.sparta.newneoboardbuddy.domain.file.dto.request.FileUploadDto;
import com.sparta.newneoboardbuddy.domain.file.entity.S3File;
import com.sparta.newneoboardbuddy.domain.file.exception.FileSizeExceededException;
import com.sparta.newneoboardbuddy.domain.file.exception.ImpossibilityExtensionException;
import com.sparta.newneoboardbuddy.domain.file.exception.NotFoundFileException;
import com.sparta.newneoboardbuddy.domain.file.repository.S3FileRepository;
import com.sparta.newneoboardbuddy.domain.member.service.MemberService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String BUCKET_NAME;

    @Value("${cloud.aws.region.static}")
    private String REGION;

    // s3 DI
    private final AmazonS3 amazonS3Client;

    private final S3FileRepository s3FileRepository;

    private final MemberService memberService;

    private String BASE_URL;

    private final Set<String> ALLOWED_EXTENSIONS = new HashSet<String>();

    @PostConstruct
    public void init(){
        BASE_URL = "https://" + BUCKET_NAME + ".s3." + REGION + ".amazonaws.com/";

        ALLOWED_EXTENSIONS.add("jpg");
        ALLOWED_EXTENSIONS.add("png");
        ALLOWED_EXTENSIONS.add("pdf");
        ALLOWED_EXTENSIONS.add("csv");

    }

    // 파일 사이즈 제한
    private final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void uploadFile(FileUploadDto fileUploadDto) {

        // 파일명 추출
        // 곂칠 우려가 있기 때문에 파일명에 랜덤값 추가
        String fileName = fileUploadDto.getTargetTable() + "/" + UUID.randomUUID() + "_" + fileUploadDto.getTargetFile().getOriginalFilename();

        // 파일 확장자 체크
        if (!ALLOWED_EXTENSIONS.contains(extractExt(fileName))) {
            throw new ImpossibilityExtensionException(HttpStatus.BAD_REQUEST);
        }

        // 파일 크기 검사
        if (fileUploadDto.getTargetFile().getSize() > MAX_FILE_SIZE) {
            throw new FileSizeExceededException(HttpStatus.PAYLOAD_TOO_LARGE);
        }

        // 파일 url 생성
        String fileUrl = BASE_URL + fileName;

        try {
            // 파일 저장
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(fileUploadDto.getTargetFile().getContentType());
            metadata.setContentLength(fileUploadDto.getTargetFile().getSize());
            amazonS3Client.putObject(BUCKET_NAME, fileName, fileUploadDto.getTargetFile().getInputStream(), metadata);

            S3File file = new S3File(fileUrl, fileUploadDto);
            s3FileRepository.save(file);

        } catch (IOException | RuntimeException e) {
            throw new RuntimeException("파일 저장 중 알 수 없는 에러 발생");
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteFile(AuthUser authUser, FileDeleteDto fileDeleteDto) {

        memberService.verifyMember(authUser, fileDeleteDto.getWorkspaceId());

        S3File s3File = findS3File(fileDeleteDto.getTargetId(), fileDeleteDto.getTargetTable());

        try {
            // s3 파일 삭제
            amazonS3Client.deleteObject(BUCKET_NAME, s3File.getFileUrl());

            // 파일 삭제 처리
            s3File.setDeleted(true);

        } catch (RuntimeException e) {
            throw new RuntimeException("파일 삭제 중 알 수 없는 에러 발생");
        }

    }

    public String getFile(Long targetId, String targetTable) {
        S3File s3File = findS3File(targetId, targetTable);
        return s3File.getFileUrl();
    }


    private S3File findS3File(Long targetId, String targetTable) {
        return s3FileRepository.findByTargetIdAndAndTargetTable(targetId, targetTable).orElseThrow(
                () -> new NotFoundFileException(HttpStatus.BAD_REQUEST)
        );
    }

    //원래 파일명에서 확장자 뽑기
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
