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
@Table(name = "suppliers", uniqueConstraints = @UniqueConstraint(columnNames = "supplierCode"))
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String supplierCode;

    @Column(nullable = false)
    private String supplierName;

    @Column(nullable = true)
    private String contactPerson;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String physicalAddress;

    @Column(nullable = true)
    private String postalAddress;

    // Tracking Fields
    @ManyToOne
    @JoinColumn(name = "posted_by", nullable = false, updatable = false)
    private User postedBy;

    @Column(nullable = false)
    private String postedFlag = "N"; // Default value 'N'

    @Column(nullable = true, updatable = false)
    private LocalDateTime postedTime;

    @ManyToOne
    @JoinColumn(name = "deleted_by", updatable = true)
    private User deletedBy;

    @Column(nullable = false)
    private String deletedFlag = "N"; // Default value 'N'

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedTime;

    @ManyToOne
    @JoinColumn(name = "modified_by", updatable = true)
    private User modifiedBy;

    @Column(nullable = false)
    private String modifiedFlag = "N"; // Default value 'N'

    @Column(nullable = true, updatable = true)
    private LocalDateTime modifiedTime;

    public Supplier(String supplierCode, String supplierName, String contactPerson, String phoneNumber, String email, String physicalAddress, String postalAddress, User postedBy) {
        this.supplierCode = supplierCode;
        this.supplierName = supplierName;
        this.contactPerson = contactPerson;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.physicalAddress = physicalAddress;
        this.postalAddress = postalAddress;
        this.postedBy = postedBy;
        this.postedTime = LocalDateTime.now();
        this.postedFlag = "Y";
        this.modifiedFlag = "N";
    }
}
