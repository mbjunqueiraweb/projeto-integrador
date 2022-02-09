package com.w4.projetoIntegrador.repository;

import java.util.List;

import com.w4.projetoIntegrador.entities.Batch;
import com.w4.projetoIntegrador.entities.ProductAnnouncement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRepository extends JpaRepository<Batch, Long> {
     List<Batch> findByProductAnnouncement(ProductAnnouncement product);

     @Query(value = "select  distinct b.product_announcement_id as productId, s.id as section  from batches b inner join  inbounds i on b.inbound_id = i.id inner join sections s on i.section_id = s.id  where product_announcement_id  = :id", nativeQuery = true)
     List<SectionById> getSectionsById(Long id);

     public interface SectionById {
          Long getProductId();
          Long getSection();
     }

     @Query(value = "select  sum(b.stock) as stock, b.product_announcement_id as productId, b.id as batch, s.id as section, s.warehouse_id as warehouse  from batches b inner join  inbounds i on b.inbound_id = i.id inner join sections s on i.section_id = s.id  where product_announcement_id  = :pId and s.id = :sId group by productId, batch, section, warehouse", nativeQuery = true)
     List<SoldStock> getStock(Long pId, Long sId);

     public interface SoldStock{
          Long getProductId();
          Long getSection();
          Integer getStock();
          Long getBatch();
          Long getWarehouse();
     }

}
