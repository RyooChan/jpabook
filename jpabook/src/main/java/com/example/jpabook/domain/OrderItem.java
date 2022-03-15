package com.example.jpabook.domain;

import com.example.jpabook.domain.Item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // createOrderItem을 제외하고 다른 위치에서 생성자의 생성을 제약한다.
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    // 생성 메서드
    // 해당 메서드를 사용하는 이유는 할인행사 등을 수행할 때에 변경해서 세팅할 가능성이 있기 때문이다.
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    // 비즈니스로직
    // 재고 수량을 다시 돌려주는 로직.
    public void cancel(){
        getItem().addStock(count);
    }

    // 조회 로직
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }

}
