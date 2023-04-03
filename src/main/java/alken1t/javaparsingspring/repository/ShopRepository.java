package alken1t.javaparsingspring.repository;

import alken1t.javaparsingspring.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop,Long> {
    List<Shop> findAllBy();
}