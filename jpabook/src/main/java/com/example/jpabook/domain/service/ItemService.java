package com.example.jpabook.domain.service;

import com.example.jpabook.domain.Item.Book;
import com.example.jpabook.domain.Item.Item;
import com.example.jpabook.domain.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, Book bookParam){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(bookParam.getPrice());
        findItem.setName(bookParam.getName());
        findItem.setStockQuantity(bookParam.getStockQuantity());
        // ... 이렇게 필드를 채워서 진행해주면 findOne을 통해 영속상태로 가져와준 값이 param으로 세팅되게 된다.
        // 그러면 따로 업데이트를 해주지 않아도 영속성 컨텍스트의 관리대상이 되어서 Dirty Checking을 해준다.
        // 그래서 이렇게 하면 바로 업데이트 한다!!!
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }

}
