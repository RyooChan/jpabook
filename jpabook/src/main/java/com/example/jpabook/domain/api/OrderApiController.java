package com.example.jpabook.domain.api;

import com.example.jpabook.domain.Address;
import com.example.jpabook.domain.Order;
import com.example.jpabook.domain.OrderItem;
import com.example.jpabook.domain.OrderStatus;
import com.example.jpabook.domain.repository.OrderRepository;
import com.example.jpabook.domain.repository.OrderSearch;
import com.example.jpabook.domain.repository.order.query.OrderFlatDto;
import com.example.jpabook.domain.repository.order.query.OrderItemQueryDto;
import com.example.jpabook.domain.repository.order.query.OrderQueryDto;
import com.example.jpabook.domain.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    // Entity를 직접 노출하는 방법.
    // 이 방법은 실제로 사용하지 않는다.
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            // order내에서 OrderItem의 정보를 노출시키려 한다.
            // 강제 초기화를 진행한다. 이는 Hibernate5 module를 사용했기 때문이다.
            // @JsonIgnore을 해줘야지 이 뒤에 무한호출 문제를 해결 가능하다.
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    // 모든 값을 싹다 Dto로 변경하여 집어넣는 방법. (DTO내에 entity가 들어가면 안된다!!)
    // N+1문제가 발생한다. 여기서는
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());
        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o))
                .collect(toList());

        return collect;
    }

    // fetch join을 통하여 값을 가져온다.
    // 그런데 OneToMany처럼 하나에서 가져오는 데이터가 많이 있는 경우 가져오는 데이터가 뻥튀기된다.
    // 예를 들어 A의 orderItem이 2개이면 A에 관한 데이터 라인이 2개가 생기게 될 것이다.
    // 그런데 우리는 A가 굳이 2개일 필요는 없고, 하위의 데이터만을 원할 것이다.
    // 참고로 위의 A데이터들은 동일한 주소값을 가진다. 이를 통해 distinct를 사용하면 필요없는 값을 필터링시켜준다.
    // 이 distinct는 JPA의 굉장히 큰 장점중에 하나이다. 이를 통해 코드 최적화가 엄청나게 진행된다.
    // 하지만 이 경우는 큰 문제가 있다. DB의 row자체가 늘어나게 된다. 이 distinct는 어플리케이션에서 중복을 걸러주기 때문이다.
    // 그렇기 때문에 페이징이 불가능해진다. -> 일대다/다대다를 패치조인 하는순간 페이징 쿼리가 아예 나가지를 않는다.
    // TIL study 16참조하기.
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithItem();

        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o))
                .collect(toList());

        return collect;
    }

    // 위의 방법에서 페이징을 할 수 있도록 진행하는 방법이당.

    // 그리고 이 방식을 사용하면 위의 v3보다 쿼리자체는 더 날리게 된다. 처음에 가져온 데이터에 대해 추가로 더 가져오기 때문이다.
    // 하지만 v3의 방법은 중복이 엄청나게 많이 발생하며 그 많은 중복을 DB -> 어플케이션으로 싹다 보내서 전송되는 데이터 양이 많아진다.
    // 그래서 v3의 방식은 용량 이슈가 발생할 수 있다.

    // 반대로 v3.1의 방식은 쿼리 전송 자체는 많지만
    // 1. 처음에 fetch로 보낸 데이터는 원하는 것들만이 있기 때문에 중복 없는 최적화된 데이터만을 갖고있는다.
    // 2. 다음으로 가져온 in방식으로 들고온 데이터들 또한 중복이 존재하지 않는다.
    // 만약 2에서 가져온 데이터에 연결된 것이 있는 경우, 이 또한 중복 없이 필요한 것들만을 가져온다.
    // 그렇기 때문에 3.1방식으로 데이터 통신을 진행하면 정규화된 상태로 데이터 조회를 한다는 것이다!

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset
            , @RequestParam(value = "limit", defaultValue = "100") int limit) {
        // 먼저 지금 order와 xToOne으로 되어있는 값들을 한꺼번에 fetch join하여 가져온다.
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        // 추가로 LAZY는 이미 되어있음.

        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o))
                .collect(toList());

        return collect;
    }

    // DTO로 바로 조회하는 방법.
    // 해당 방법은 N+1에러 발생
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }

    // 한꺼번에 가져오지 않고, 값들을 하나씩 들고온 후에 이걸 다시 map으로 바꾸어서 memorymap에 올려둔 후 그것을 찾아온다.
    // 이렇게 하면 위의 v4에서 발생한 문제를 쿼리 2번으로 해결해 낼 수 있다.
    // 즉 쿼리는 루트1번, 컬렉션1번 총 2번 발생한다.
    // 즉 ToOne관계를 먼저 조회한 후, 이후 식별자를 통한 컬렉션 조회를 한꺼번에 진행.
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }

    // 한번에 모든 데이터를 가져오기 때문에 쿼리가 1번에 끝난다
    // 이 경우는 페이징을 하지 못한다. 이유는 데이터의 중복이 발생하기 때문이다.
    // 그리고 데이터의 중복이 많기 때문에 상황에 따라 위의 V5보다 오래 걸릴 수도 있다.
    // 그리고 어플리케이션에서 추가 작업이 크다.

    // 그리고 값의 반환이 OrderFlatDto로 이루어지기 때문에 데이터의 스펙이 변경된다.
    // 따라서 이를 다시 값을 변환해주어야 한다.
    // 저 변환하는 내용을 보면 굉장히 코드가 길고 귀찮다...
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6(){
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(
                        groupingBy(o -> new OrderQueryDto(o.getOrderId(),o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(),o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                ))
                .entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),e.getKey().getName(), e.getKey().getOrderDate(), e.getKey()
                .getOrderStatus(),e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }



    @Getter
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream().map(orderItem -> new OrderItemDto(orderItem))
                    .collect(toList());
        }
    }

    @Getter
    static class OrderItemDto{
        private String itemName;    // 상품명
        private int orderPrice;     // 주문 가격
        private int count;          // 주문 수량

        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
