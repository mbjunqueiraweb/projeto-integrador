package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.dtos.SellerDto;
import com.w4.projetoIntegrador.entities.Seller;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.SellerRepository;
import com.w4.projetoIntegrador.service.SellerService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

public class SellerTest {

    private Seller seller = Seller.builder().id(1L).name("seller 1").build();
    private SellerDto sellerDto = SellerDto.builder().id(1L).name("seller 1").build();

    @Test
    public void deveCadastrarUmSeller() {

        SellerRepository mock = Mockito.mock(SellerRepository.class);
        Mockito.when(mock.save(Mockito.any())).thenReturn(seller);

        SellerService sellerService = new SellerService(mock);

        SellerDto sDto = sellerService.save(sellerDto);

        assertEquals(sDto.getName(), sellerDto.getName());

    }

    @Test
    public void deveRetornarUmSellerPassandoUmId() {

        SellerRepository mock = Mockito.mock(SellerRepository.class);
        Mockito.when(mock.findById(Mockito.anyLong())).thenReturn(Optional.of(seller));

        SellerService sellerService =  new SellerService(mock);

        SellerDto sDtoReceived = sellerService.get(1L);

        assertEquals(sDtoReceived, sellerDto);

    }
    @Test
    public void deveLançarExcessaoAoBuscarUmSellerPorIdnexistente() {
        SellerRepository mock = Mockito.mock(SellerRepository.class);
        Mockito.when(mock.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        SellerService sellerService =  new SellerService(mock);
     
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> sellerService.get(10L));

        assertTrue(notFoundException.getMessage().contains("Seller 10 não encontrado na base de dados."));
    }
    
}
