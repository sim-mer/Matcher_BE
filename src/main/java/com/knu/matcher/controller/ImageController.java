package com.knu.matcher.controller;

import com.knu.matcher.domain.jobpost.JobPostImg;
import com.knu.matcher.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {
    @Value("${ImageFolder}")
    private String imageFolder;

    private final ImageService imageService;

    @GetMapping("/{imageId}")
    public ResponseEntity<Resource> showImage(@PathVariable long imageId) throws Exception{
        String imageName = imageService.getImageName(imageId);
        Path imagePath = Paths.get(imageFolder, imageName);
        Resource resource = new UrlResource(imagePath.toUri());
        String contentType = Files.probeContentType(imagePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PostMapping("/{jobPostId}")
    public List<Long> ContractDocumentUpload(@RequestParam("files") MultipartFile[] files, @PathVariable Long jobPostId) {
        return imageService.uploadImages(files, jobPostId);
    }

    @DeleteMapping("/{imageId}")
    public void deleteImage(@PathVariable long imageId) {
        imageService.deleteImage(imageId);
    }
}
