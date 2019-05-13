package com.anbang.qipai.hongbao.cqrs.q.dbo;

/**
 * @Author: 吴硕涵
 * @Date: 2018/12/25 2:02 PM
 * @Version 1.0
 */
public class ReceiverInfoDbo {
    String id;
    String memberId;
    String name;//收货人姓名
    String gender;//性别
    String telephone;
    String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
