package com.ocps.auth.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "uan_master")
public class UanMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uan", nullable = false, unique = true)
    private String uan;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "aadhaar")
    private String aadhaar;

    @Column(name = "mobile")
    private String mobile;

    @OneToMany(mappedBy = "uanMaster")
    private List<MemberAccount> memberAccounts;

    public UanMaster() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUan() {
        return uan;
    }

    public void setUan(String uan) {
        this.uan = uan;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<MemberAccount> getMemberAccounts() {
        return memberAccounts;
    }
}