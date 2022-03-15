package com.example.jpabook.domain.service;

import com.example.jpabook.domain.Delivery;
import com.example.jpabook.domain.Item.Item;
import com.example.jpabook.domain.Member;
import com.example.jpabook.domain.Order;
import com.example.jpabook.domain.OrderItem;
import com.example.jpabook.domain.repository.ItemRepository;
import com.example.jpabook.domain.repository.MemberRepository;
import com.example.jpabook.domain.repository.OrderRepository;
import com.example.jpabook.domain.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId){
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 최소
        order.cancel();
    }

    // 검색
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAllByCriteria(orderSearch);
    }
}
