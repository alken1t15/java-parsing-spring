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

    private String name;

    @Column(name = "link_product")
    private String linkProduct;

    private Integer price;

    private LocalDate date;

    public Product(String storeName, String name, String linkProduct, Integer price, LocalDate date) {
        this.storeName = storeName;
        this.name = name;
        this.linkProduct = linkProduct;
        this.price = price;
        this.date = date;
    }
}