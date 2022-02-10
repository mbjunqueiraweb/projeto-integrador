package com.w4.projetoIntegrador.repository;

import com.w4.projetoIntegrador.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
  @Query(value ="select pa.id as product_id, w.id as warehouse, s.id as section, b.id as batch, w.name, sum(b.stock) as stock from products_announcements pa inner join batches b on pa.id = b.product_announcement_id inner join inbounds i on b.inbound_id = i.id inner join sections s on i.section_id = s.id inner join warehouses w on s.warehouse_id = w.id where pa.id = :id group by pa.id, w.id, batch;",nativeQuery = true)


    List<ProductWarehouse> getStockByWarehouse(Long id);
    public interface ProductWarehouse{
        Long getWarehouse();
        Long getBatch();
        Long getSection();
        Integer getStock();
    }
}
