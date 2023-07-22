package com.example.eCommerceSpringBoot.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name= "Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    private Date creationDate;
    private Date deliveredDate;
    private Double total;

    @ManyToOne
    private User user;
    @OneToOne(mappedBy = "order")
    private OrderDetail orderDetail;

    public Order() {
    }

    public Order(Long id, String number, Date creationDate, Date deliveredDate, Double total) {
        this.id = id;
        this.number = number;
        this.creationDate = creationDate;
        this.deliveredDate = deliveredDate;
        this.total = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDeliveredDate() {
        return deliveredDate;
    }

    public void setDeliveredDate(Date deliveredDate) {
        this.deliveredDate = deliveredDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", deliveredDate=" + deliveredDate +
                ", total=" + total +
                ", user=" + user +
                ", orderDetail=" + orderDetail +
                '}';
    }
}
