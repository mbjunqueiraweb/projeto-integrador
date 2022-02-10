package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.entities.ScheduledItemCart;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.ScheduledItemCartRepository;
import org.springframework.stereotype.Service;

@Service
public class ScheduledItemCartService {

    ScheduledItemCartRepository scheduledItemCartRepository;

    public ScheduledItemCartService(ScheduledItemCartRepository scheduledItemCartRepository) {
        this.scheduledItemCartRepository = scheduledItemCartRepository;
    }

    public ScheduledItemCart getPurchaseProduct(Long id) {
        try {
            ScheduledItemCart scheduledItemCart = scheduledItemCartRepository.findById(id).orElse(null);
            if (scheduledItemCart.equals(null)) throw new NotFoundException("Product " + id + " não encontrada na base de dados.");
            return scheduledItemCart;
        } catch (RuntimeException e) {
            throw new NotFoundException("Product " + id + " não encontrada na base de dados.");
        }
    }

    public ScheduledItemCart create(ScheduledItemCart scheduledItemCart) {
        return scheduledItemCartRepository.save(scheduledItemCart);
    }
}
