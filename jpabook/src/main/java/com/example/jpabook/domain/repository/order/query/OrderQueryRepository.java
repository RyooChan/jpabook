package com.example.jpabook.domain.repository.order.query;

import com.example.jpabook.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        // 각각의 ID에 대해
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

        // 한번에 모든 값들을 in으로 꺼내온다.
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new com.example.jpabook.domain.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) "
                                + " from OrderItem oi "
                                + " join oi.item i"
                                + " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // Map을 사용하여 전체 값을 한번에 조회하도록 한다.
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

        // 추가 쿼리 없이 루프를 통한 컬렉션 조회 실시
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;

    }

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos(){
        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderIds){
        return em.createQuery(
                "select new com.example.jpabook.domain.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) "
                + " from OrderItem oi "
                + " join oi.item i"
                + " where oi.order.id = :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

    }

    public List<OrderQueryDto> findOrders(){
        return  em.createQuery(
                    "select new com.example.jpabook.domain.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)"
                            + " from Order o"
                            + " join o.member m"
                            + " join o.delivery d", OrderQueryDto.class).getResultList();

    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new "
                + " com.example.jpabook.domain.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count) "
                + " from Order o"
                + " join o.member m "
                + " join o.delivery d "
                + " join o.orderItems oi "
                + " join oi.item i ", OrderFlatDto.class).getResultList();

    }
}
