package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.dtos.BatchDto;
import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.BatchRepository;
import com.w4.projetoIntegrador.service.BatchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BatchServiceTest {

    private Product product1 = Product.builder().id(1L).name("product").productType(ProductTypes.congelado).build();
    private Seller seller1 = Seller.builder().id(1l).name("seller").build();
    private ProductAnnouncement pa1 = ProductAnnouncement.builder()
            .id(1l)
            .name("product")
            .brand("brand")
            .price(new BigDecimal(1))
            .volume(10f)
            .minimumTemperature(- 10f)
            .maximumTemperature(0f)
            .product(product1)
            .seller(seller1)
            .build();

    private BatchDto batchDto1 = BatchDto.builder().id(1l).initialQuantity(10).stock(10).build();
    private Batch batch1 = Batch.builder().id(1L).productAnnouncement(pa1).initialQuantity(10).stock(10).build();


    @Test
    public void deveBuscarUmBuyer() {

        //arrange
        BatchRepository mock = Mockito.mock(BatchRepository.class);

        Mockito.when(mock.findById(Mockito.anyLong())).thenReturn(Optional.of(batch1));

        BatchService batchService = new BatchService(mock);

        //act
        BatchDto b = batchService.get(1L);

        //assertion
        assertEquals(b.getInitialQuantity(), batchDto1.getInitialQuantity());
    }

    @Test
    public void deveLancarExcessaoAoBuscarBatchInexistente() {

        //arrange
        BatchRepository mock = Mockito.mock(BatchRepository.class);

        Mockito.when(mock.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        BatchService batchService = new BatchService(mock);

        //act
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () ->  batchService.get(1L));

        //assertion
        assertTrue(notFoundException.getMessage().contains("Batch 1 não encontrado na base de dados."));


    }

    @Test
    public void deveDecrementarUmBatch() {

        //arrange
        BatchRepository mock = Mockito.mock(BatchRepository.class);

        Mockito.when(mock.findById(Mockito.anyLong())).thenReturn(Optional.of(batch1));
        Mockito.when(mock.save(Mockito.any())).thenReturn(batch1);

        BatchService batchService = new BatchService(mock);

        //act
      Batch newBatch =  batchService.decrementBatch(8, 1L);

        //assertion
        assertTrue(newBatch.getStock().equals(2));


    }
}


