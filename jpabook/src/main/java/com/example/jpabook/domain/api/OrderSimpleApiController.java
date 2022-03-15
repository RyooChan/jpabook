package com.example.jpabook.domain.api;

import com.example.jpabook.domain.Address;
import com.example.jpabook.domain.Order;
import com.example.jpabook.domain.OrderStatus;
import com.example.jpabook.domain.repository.OrderRepository;
import com.example.jpabook.domain.repository.OrderSearch;
import com.example.jpabook.domain.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne
 * Order
 * Order -> Member : ManyToOne
 * Order -> Delivery : OneToOne
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // 안좋은 방식 : 바로 Entity로 받아온다.
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for(Order order : all){
            order.getMember().getName();    //  Lazy강제 초기화하여 해당 엔티티만 가져오기.
            order.getDelivery().getAddress();
        }
        return all;
    }

    // Dto를 통해 받아오는 방식. LAZY로 되어있는 내용을 확인할 때 N+1문제가 발생한다.
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        return orderRepository.findAllByCriteria(new OrderSearch())
                .stream().map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    // Fetch join을 써서, 해당하는 내용을 받아올 때 일괄로 다 가져오는 방식.
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    // repository에서 가져올 때에 DTO로 바로 받아오는 방식. 위의 3에서 추가로 최적화가 된 방법이다.
    // DTO에서 가져올 데이터에 한해서 select 해 올 수 있도록 해준다!
    // 단, 위의 3방식과 비교하면 재사용성이 굉장히 떨어지게 된다... 해당 쿼리는 해당 장소에서만 사용할 수 있기 때문.
    // 어지간하면 3방식을 사용하는것이 더 좋지 않을까? 싶은느낌이다.
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4(){
        return orderRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
