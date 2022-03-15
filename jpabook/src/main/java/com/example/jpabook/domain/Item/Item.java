package com.example.jpabook.domain.Item;

import com.example.jpabook.domain.Category;
import com.example.jpabook.domain.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items", fetch = FetchType.LAZY)
    private List<Category> categories = new ArrayList<>();

    // 도메인 주도 설계를 할 때에 Domain내에 비즈니스 로직을 넣을 수도 있다.
    // 해당 도메인 내에 stockQuatity가 있기 때문에 이와 관련된 로직을 여기서 처리하는 것이 더 객체지향적

    // stock 증가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    // stock 감소
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }


}
