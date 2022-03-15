package com.example.jpabook.domain.service;

import com.example.jpabook.domain.Address;
import com.example.jpabook.domain.Item.Book;
import com.example.jpabook.domain.Item.Item;
import com.example.jpabook.domain.Member;
import com.example.jpabook.domain.Order;
import com.example.jpabook.domain.OrderStatus;
import com.example.jpabook.domain.exception.NotEnoughStockException;
import com.example.jpabook.domain.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Book book = createBook();

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("상품주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문한 가격은 가격*수량이다.", 10000*orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다..", 8, book.getStockQuantity());

    }


    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Book book = createBook();

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 생티는 CANCEL이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문 취소시 상품 재고는 그만큼 다시 증가한다..", 10, book.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createBook();

        int orderCount = 12;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량 예외가 발생해야 한다.");

    }

    private Book createBook() {
        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}