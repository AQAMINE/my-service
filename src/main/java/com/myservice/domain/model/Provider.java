package com.myservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Provider {
    private UUID id;
    private UUID userId; // null si provider système global
    private String name;
    private String slug;
    private String websiteUrl;
    private String color;
    private String logoUrl;
    private Boolean isActive;
    private OffsetDateTime createdAt;
}
