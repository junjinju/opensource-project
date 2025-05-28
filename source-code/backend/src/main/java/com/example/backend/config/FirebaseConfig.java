package com.example.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.credentials.path}")
    private String firebaseCredentialsPath;

    @Value("${firebase.project-id}")
    private String projectId;

    @Bean
    public Storage firebaseStorage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseCredentialsPath).getInputStream());

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }
}