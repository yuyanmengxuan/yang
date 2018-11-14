package com.wei.pojo;

import java.util.Date;
import java.util.Objects;

public class Trade {
    private Integer id;
    private String order;
    private Integer no;
    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "id=" + id +
                ", order='" + order + '\'' +
                ", no=" + no +
                ", createTime=" + createTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return Objects.equals(id, trade.id) &&
                Objects.equals(order, trade.order) &&
                Objects.equals(no, trade.no);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, no);
    }
}
