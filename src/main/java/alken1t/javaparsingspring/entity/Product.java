package alken1t.javaparsingspring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_name")
    private String storeName;

    private String category;
    private String name;

    @Column(name = "link_product")
    private String linkProduct;

    private Integer price;

    private LocalDate date;

    @Column(name = "img_link")
    private String img;

    public Product(String storeName, String category, String name, String linkProduct, Integer price, LocalDate date,String img) {
        this.storeName = storeName;
        this.category = category;
        this.name = name;
        this.linkProduct = linkProduct;
        this.price = price;
        this.date = date;
        this.img = img;
    }
}