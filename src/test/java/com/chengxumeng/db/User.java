package com.chengxumeng.db;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: dbutils
 * @description:
 * @author: 程序梦
 * @create: 2024-05-09 15:31
 **/

public class User {
    @Column("u_id")
    private Integer uid;
    @Column("u_name")
    private String uname;
    @Column("u_password")
    private String upassword;
    @Column("u_age")
    private int uage;

    @Column("deposit")
    private BigDecimal deposit;
    @Column("birth")
    private LocalDateTime birth;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public int getUage() {
        return uage;
    }

    public void setUage(int uage) {
        this.uage = uage;
    }

    public BigDecimal getDeposit() {
        return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public LocalDateTime getBirth() {
        return birth;
    }

    public void setBirth(LocalDateTime birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", upassword='" + upassword + '\'' +
                ", uage=" + uage +
                ", deposit=" + deposit +
                ", birth=" + birth +
                '}';
    }
}
