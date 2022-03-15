package com.example.jpabook.domain.service;

import com.example.jpabook.domain.Item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception {
        //given
        Book book = em.find(Book.class, 1L);

        //TX
        book.setName("aafaf");      // 변경감지(dirty checking)을 통해 영속성 컨텍스트 내의 변경을 감지하여 자동 update진행

        //when

        //then

    }
}
