package com.anbang.qipai.hongbao.web.vo;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class InvitationQuery {
    private String memberId; //邀请人id
    private String state;

    private String createTimeSort;
    private String activationTimeSort;

    private Integer page;
    private Integer size;

    public Sort getSort() {
        List<Sort.Order> orderList = new ArrayList<>();
        if ("ASC".equals(createTimeSort)) {
            orderList.add(new Sort.Order(Sort.Direction.ASC, "createTime"));
        } else if ("DESC".equals(createTimeSort)) {
            orderList.add(new Sort.Order(Sort.Direction.DESC, "createTime"));
        }
        if ("ASC".equals(activationTimeSort)) {
            orderList.add(new Sort.Order(Sort.Direction.ASC, "activationTime"));
        } else if ("DESC".equals(activationTimeSort)) {
            orderList.add(new Sort.Order(Sort.Direction.DESC, "activationTime"));
        }
        if ("ASC".equals(createTimeSort)) {
            orderList.add(new Sort.Order(Sort.Direction.ASC, "createTime"));
        } else if ("DESC".equals(createTimeSort)) {
            orderList.add(new Sort.Order(Sort.Direction.DESC, "createTime"));
        }
        if (!orderList.isEmpty()) {
            Sort sort = new Sort(orderList);
            return sort;
        }
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "createTime"));
        return sort;
    }

    public InvitationQuery(String memberId) {
        this.memberId = memberId;
    }

    public InvitationQuery() {
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreateTimeSort() {
        return createTimeSort;
    }

    public void setCreateTimeSort(String createTimeSort) {
        this.createTimeSort = createTimeSort;
    }

    public String getActivationTimeSort() {
        return activationTimeSort;
    }

    public void setActivationTimeSort(String activationTimeSort) {
        this.activationTimeSort = activationTimeSort;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
