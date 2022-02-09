package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.entities.ItemCart;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.ItemCartRepository;
import org.springframework.stereotype.Service;

@Service
public class ItemCartService {

    ItemCartRepository itemCartRepository;

    public ItemCartService(ItemCartRepository itemCartRepository) {
        this.itemCartRepository = itemCartRepository;
    }

    public ItemCart getPurchaseProduct(Long id) {
        try {
            ItemCart itemCart = itemCartRepository.findById(id).orElse(null);
            if (itemCart.equals(null)) throw new NotFoundException("Product " + id + " não encontrada na base de dados.");
        return itemCart;
        } catch (RuntimeException e) {
            throw new NotFoundException("Product " + id + " não encontrada na base de dados.");
        }
    }

    public ItemCart create(ItemCart itemCart) {

        return itemCartRepository.save(itemCart);

    }
}

