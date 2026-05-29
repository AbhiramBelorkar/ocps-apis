package com.ocps.auth.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "member_account")
public class MemberAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, unique = true)
    private String memberId;

    @Column(name = "establishment_name")
    private String establishmentName;

    @Column(name = "doj")
    private LocalDate doj;

    @Column(name = "doe")
    private LocalDate doe;

    @ManyToOne
    @JoinColumn(name = "uan_id")
    private UanMaster uanMaster;

    public MemberAccount() {
    }

    public Long getId() {
        return id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public LocalDate getDoj() {
        return doj;
    }

    public void setDoj(LocalDate doj) {
        this.doj = doj;
    }

    public LocalDate getDoe() {
        return doe;
    }

    public void setDoe(LocalDate doe) {
        this.doe = doe;
    }

    public UanMaster getUanMaster() {
        return uanMaster;
    }

    public void setUanMaster(UanMaster uanMaster) {
        this.uanMaster = uanMaster;
    }
}