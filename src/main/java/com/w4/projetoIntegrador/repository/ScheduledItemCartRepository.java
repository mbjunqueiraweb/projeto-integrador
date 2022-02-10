package com.w4.projetoIntegrador.repository;

import com.w4.projetoIntegrador.entities.ScheduledItemCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledItemCartRepository extends JpaRepository<ScheduledItemCart, Long> {
}
