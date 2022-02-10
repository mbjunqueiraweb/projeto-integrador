package com.w4.projetoIntegrador.repository;

import com.w4.projetoIntegrador.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>  {
}
