package com.sparta.newneoboardbuddy.domain.file.repository;

import com.sparta.newneoboardbuddy.domain.file.entity.S3File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface S3FileRepository extends JpaRepository<S3File, Long> {

    Optional<S3File> findByTargetIdAndAndTargetTable(Long targetId, String targetTable);

}
