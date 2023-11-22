package com.knu.matcher.service;

import com.knu.matcher.domain.jobpost.JobPostImg;
import com.knu.matcher.repository.JobPostImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value("${ImageFolder}")
    private String imageFolder;

    private final JobPostImgRepository jobPostImgRepository;

    public List<Long> uploadImages(MultipartFile[] files, Long jobPostId) {
        List<Long> imgIds = new ArrayList<>();
        long id = jobPostImgRepository.findLastJobPostImgId() + 1;
        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            Path upload = Paths.get(imageFolder);
            String name = uuid.toString() + "_" + file.getOriginalFilename();
            try {
                Files.copy(file.getInputStream(), upload.resolve(name));
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장에 실패했습니다.");
            }
            JobPostImg img = JobPostImg.builder()
                    .id(id)
                    .url(uuid.toString())
                    .name(file.getOriginalFilename())
                    .jobPostId(jobPostId)
                    .build();
            imgIds.add(jobPostImgRepository.save(img));
        }
        return imgIds;
    }

    public void deleteImage(long imageId) {
        String name = jobPostImgRepository.findUUIDname(imageId);
        if(name == null) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다.");
        }
        Path imagePath = Paths.get(imageFolder, name);
        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 파일 삭제에 실패했습니다.");
        }
        if(!jobPostImgRepository.delete(imageId)) {
            throw new IllegalArgumentException("이미지 삭제에 실패했습니다.");
        }
    }

    public String getImageName(long imageId) {
        String name = jobPostImgRepository.findUUIDname(imageId);
        if(name == null) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다.");
        }
        return name;
    }
}
