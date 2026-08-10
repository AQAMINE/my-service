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
public class AccountCategory {
    private UUID id;
    private UUID userId; // null si catégorie système globale
    private String name;
    private String slug;
    private String description;
    private OffsetDateTime createdAt;
}
