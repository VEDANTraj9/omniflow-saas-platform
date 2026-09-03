package com.omniflow.backendcore.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor   // <-- Check karein ye hona chahiye, @AllColors nahi
@Builder
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String subdomain;
}