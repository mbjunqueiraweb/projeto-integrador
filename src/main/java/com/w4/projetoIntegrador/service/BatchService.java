package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.dtos.BatchDto;
import com.w4.projetoIntegrador.entities.Batch;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.BatchRepository;

import org.springframework.stereotype.Service;

@Service
public class BatchService {

    BatchRepository batchRepository;

    public BatchService(BatchRepository batchRepository){
        this.batchRepository = batchRepository;
    }

    public BatchDto get(Long id) {
            return BatchDto.convert(getBatch(id));
    }

    public Batch getBatch(Long id) {
        try {
            Batch batch = batchRepository.findById(id).orElse(null);
            return batch;
        } catch (RuntimeException e) {
            throw new NotFoundException("Batch " + id + " não encontrado na base de dados.");
        }
    }

}
