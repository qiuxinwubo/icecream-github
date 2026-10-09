package com.icecream.repository;

import com.icecream.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long> {

    Optional<Goods> findByBarcode(String barcode);

    @Query("SELECT g FROM Goods g WHERE " +
            "(:barcode IS NULL OR g.barcode LIKE CONCAT('%', :barcode, '%')) AND " +
            "(:name IS NULL OR g.name LIKE CONCAT('%', :name, '%')) AND " +
            "(:factory IS NULL OR g.factory = :factory)")
    Page<Goods> findByCondition(@Param("barcode") String barcode,
                                @Param("name") String name,
                                @Param("factory") String factory,
                                Pageable pageable);

    boolean existsByBarcode(String barcode);
}