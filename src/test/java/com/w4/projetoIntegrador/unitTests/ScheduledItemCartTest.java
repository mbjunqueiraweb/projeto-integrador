package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.ScheduledItemCartRepository;
import com.w4.projetoIntegrador.service.ScheduledItemCartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduledItemCartTest {

    private Product product1 = Product.builder().id(1L).name("product").productType(ProductTypes.congelado).build();
    private Seller seller1 = Seller.builder().id(1l).name("seller").build();


    private ProductAnnouncement productAnnouncement = ProductAnnouncement.builder()
            .id(1L)
            .name("teste")
            .brand("teste")
            .price(new BigDecimal(10.0))
            .volume(1.0f)
            .minimumTemperature(2.0f)
            .maximumTemperature(15.0f)
            .product(product1)
            .seller(seller1)
            .build();


    private ScheduledItemCart itemCart = ScheduledItemCart.builder().id(1L).productAnnouncement(productAnnouncement).quantity(10).build();


    @Test
    public void deveSerPossivelCriarUmItemCart() {
        ScheduledItemCartRepository mockItemCartRepository = Mockito.mock(ScheduledItemCartRepository.class);
        Mockito.when(mockItemCartRepository.save(Mockito.any())).thenReturn(itemCart);

        ScheduledItemCartService service = new ScheduledItemCartService(mockItemCartRepository);

        ScheduledItemCart itemSaved = service.create(itemCart);

        assertEquals(itemSaved.getId(), itemCart.getId());

    }

    @Test
    public void deveSerPossivelPegarUmProdutoDoItemCart() {
        ScheduledItemCartRepository mockItemCartRepository = Mockito.mock(ScheduledItemCartRepository.class);
        Mockito.when(mockItemCartRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(itemCart));

        ScheduledItemCartService service = new ScheduledItemCartService(mockItemCartRepository);

        ScheduledItemCart getItemCart = service.getPurchaseProduct(1L);

        assertEquals(getItemCart.getId(), itemCart.getId());

    }

    @Test
    public void deveLançarExcessaoAoBuscarUmPurchaseProductPorIdnexistente() {
        ScheduledItemCartRepository mockItemCartRepository = Mockito.mock(ScheduledItemCartRepository.class);
        Mockito.when(mockItemCartRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        ScheduledItemCartService service = new ScheduledItemCartService(mockItemCartRepository);


        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> service.getPurchaseProduct(100L));

        assertTrue(notFoundException.getMessage().contains("Product 100 não encontrada na base de dados."));

    }
}
