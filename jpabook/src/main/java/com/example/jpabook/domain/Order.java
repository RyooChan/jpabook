package com.example.jpabook.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    private OrderStatus status; // ORDER or CANCEL

    // 연관관계 편의 메소드 생성.
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드
    // 주문생성에 관한 모든 비즈니스 로직을 한꺼번에 완결시킨다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // order의 처음 상태를 ORDER로 강제한다.
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직

    // 주문 취소
    // 주문 취소 관련 로직이다. 배송완료 관련 예외처리를 끝낸 후 재고를 원복시킨다.
    // 추가로 재고의 경우 OrderItem Domain에 저장되어 있기 때문에 해당 Domain에서 관련 cancel 비즈니스 로직을 수행한다.
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            orderItem.cancel();
        }
    }

    // 조회 로직

    // 전체 주문 가격 조회
    // 전체 주문 가격을 조회한다.
    // 주문 가격의 경우 또한 OrderItem에 가격과 수량이 있기 때문에 해당 위치에서 수행한 결과를 여기서 조회하도록 한다.
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
