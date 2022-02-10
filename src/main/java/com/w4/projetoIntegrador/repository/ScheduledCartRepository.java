package com.w4.projetoIntegrador.repository;

import com.w4.projetoIntegrador.entities.ScheduledCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledCartRepository extends JpaRepository<ScheduledCart, Long>  {
}
