package com.manu.springboot_backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "business")
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private String contactNumber;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String description;

    @ManyToOne
    @JoinColumn(name = "posted_by", nullable = false, updatable = false)
    private User postedBy;

    @Column(nullable = false)
    private String postedFlag = "N";

    @Column(nullable = true, updatable = false)
    private LocalDateTime postedTime;

    @ManyToOne
    @JoinColumn(name = "deleted_by", updatable = true)
    private User deletedBy;

    @Column(nullable = false)
    private String deletedFlag = "N";

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedTime;

    @ManyToOne
    @JoinColumn(name = "modified_by", updatable = true)
    private User modifiedBy;

    @Column(nullable = false)
    private String modifiedFlag = "N";

    @Column(nullable = true, updatable = true)
    private LocalDateTime modifiedTime;

    public Business(String name, String location, String contactNumber, String email, String description, User postedBy) {
        this.name = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.email = email;
        this.description = description;
        this.postedBy = postedBy;
        this.postedTime = LocalDateTime.now();
        this.postedFlag = "Y";
        this.modifiedFlag = "N";
    }
}
