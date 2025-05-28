package com.example.backend.util;

import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FirebaseUploader {

    private final Storage firebaseStorage;

    @Value("${firebase.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file, String folderName) {
        try {
            String fileName = folderName + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType(file.getContentType())
                    .build();

            firebaseStorage.create(blobInfo, file.getBytes());
            firebaseStorage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

            return "https://storage.googleapis.com/" + bucketName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Firebase 업로드 실패", e);
        }
    }
}