package com.ocps.auth.dto;

import java.util.List;

public class MemberDetailsResponseDto {

    private String uan;
    private String memberName;

    private List<MemberAccountDto> memberAccounts;

    public MemberDetailsResponseDto() {
    }

    public String getUan() {
        return uan;
    }

    public void setUan(String uan) {
        this.uan = uan;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public List<MemberAccountDto> getMemberAccounts() {
        return memberAccounts;
    }

    public void setMemberAccounts(List<MemberAccountDto> memberAccounts) {
        this.memberAccounts = memberAccounts;
    }
}