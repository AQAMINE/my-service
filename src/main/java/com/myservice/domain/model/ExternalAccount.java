package com.myservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExternalAccount {
    private UUID id;
    private UUID userId;
    private UUID categoryId;
    private UUID providerId;
    private String fullName;
    private String username;
    private String email;
    private String encryptedPassword;
    private String encryptionIv;
    private String link;
    private String description;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    
}
