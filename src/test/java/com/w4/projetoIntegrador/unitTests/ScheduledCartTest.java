package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.dtos.*;
import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.repository.ScheduledCartRepository;
import com.w4.projetoIntegrador.repository.WarehouseRepository;
import com.w4.projetoIntegrador.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScheduledCartTest {

    private Product product1 = Product.builder().id(1L).name("product").productType(ProductTypes.congelado).build();
    private Seller seller1 = Seller.builder().id(1l).name("seller").build();
    private ProductAnnouncement pa1 = ProductAnnouncement.builder()
            .id(1l)
            .name("product")
            .brand("brand")
            .price(new BigDecimal(1))
            .volume(10f)
            .minimumTemperature(-10f)
            .maximumTemperature(0f)
            .product(product1)
            .seller(seller1)
            .build();

    private Product product2 = Product.builder().id(2L).name("product").productType(ProductTypes.congelado).build();
    private ProductAnnouncement pa2 = ProductAnnouncement.builder()
            .id(2l)
            .name("product")
            .brand("brand")
            .price(new BigDecimal(1))
            .volume(10f)
            .minimumTemperature(-10f)
            .maximumTemperature(0f)
            .product(product1)
            .seller(seller1)
            .build();


    ScheduledItemCart itemCart = ScheduledItemCart.builder().productAnnouncement(pa1).quantity(10).build();
    ScheduledItemCart itemCart2 = ScheduledItemCart.builder().productAnnouncement(pa2).quantity(10).build();

    List<ScheduledItemCart> itemCartsList = Arrays.asList(itemCart, itemCart2);


    ScheduledItemCartDto itemCartDto = ScheduledItemCartDto.builder().id(1L).productAnnouncementId(1L).quantity(10).build();
    ScheduledItemCartDto itemCartDto2 = ScheduledItemCartDto.builder().id(1L).productAnnouncementId(2L).quantity(10).build();

    List<ScheduledItemCartDto> itemCartsListDto = Arrays.asList(itemCartDto, itemCartDto2);

    Buyer buyer = Buyer.builder().id(1L).name("buyer").build();

    ScheduledCart cart = ScheduledCart.builder().date(LocalDate.now()).statusCode("aberto").buyer(buyer).scheduledItemCarts(itemCartsList).build();
    ScheduledCart cart2 = ScheduledCart.builder().date(LocalDate.now()).statusCode("fechado").buyer(buyer).scheduledItemCarts(itemCartsList).build();

    ScheduledCartDto cartDto = ScheduledCartDto.builder().date(LocalDate.now()).statusCode("aberto").products(itemCartsListDto).buyerId(1L).build();

    ScheduledCartDto cartDto2 = ScheduledCartDto.builder()
            .date(LocalDate.now())
            .statusCode("fechado")
            .products(itemCartsListDto)
            .buyerId(1L)
            .scheduledDateTimeFrom(LocalDateTime.now().plusDays(10))
            .scheduledDateTimeTo(LocalDateTime.now().plusDays(12))
            .build();

    WarehouseStockDto wsDto = WarehouseStockDto.builder().batch(1L).section(1L).totalquantity(1000).build();
    List<WarehouseStockDto> wLists = Arrays.asList(wsDto);

    @Test
    public void deveCadastrarUmCart() {

        //arrange
        ScheduledCartRepository mockCartRepository = Mockito.mock(ScheduledCartRepository.class);
        BuyerService mockBuyerService = Mockito.mock(BuyerService.class);
        ScheduledItemCartService mockItemCartService = Mockito.mock(ScheduledItemCartService.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        WarehouseService mockWarehouseService = Mockito.mock(WarehouseService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);
        WarehouseRepository.ProductWarehouse mockProductWarehouse = Mockito.mock(WarehouseRepository.ProductWarehouse.class);


        Mockito.when(mockProductWarehouse.getWarehouse()).thenReturn(1L);
        Mockito.when(mockProductWarehouse.getBatch()).thenReturn(1L);
        Mockito.when(mockProductWarehouse.getSection()).thenReturn(1L);
        Mockito.when(mockProductWarehouse.getStock()).thenReturn(100);

        Mockito.when(mockCartRepository.save(Mockito.any())).thenReturn(cart);
        Mockito.when(mockBuyerService.getBuyer(Mockito.anyLong())).thenReturn(buyer);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.anyLong())).thenReturn(pa1);
        Mockito.when(mockWarehouseService.getWarehouseStock(Mockito.anyLong())).thenReturn(ProductsByWarehouseDto.builder().warehouses(wLists).build());
        ScheduledCartService cartService = new ScheduledCartService(mockCartRepository, mockBuyerService, mockItemCartService, mockProductAnnouncementService, mockWarehouseService, mockBatchService);

        //act
        ScheduledCartDto c = cartService.create(cartDto);

        //assertion
        assertEquals(c.getDate(), cart.getDate());
    }

    @Test
    public void deveAtualizarUmCart() {

        //arrange
        ScheduledCartRepository mockCartRepository = Mockito.mock(ScheduledCartRepository.class);
        BuyerService mockBuyerService = Mockito.mock(BuyerService.class);
        ScheduledItemCartService mockItemCartService = Mockito.mock(ScheduledItemCartService.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        WarehouseService mockWarehouseService = Mockito.mock(WarehouseService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);
        WarehouseRepository.ProductWarehouse mockProductWarehouse = Mockito.mock(WarehouseRepository.ProductWarehouse.class);


        Mockito.when(mockProductWarehouse.getWarehouse()).thenReturn(1L);
        Mockito.when(mockProductWarehouse.getBatch()).thenReturn(1L);
        Mockito.when(mockProductWarehouse.getSection()).thenReturn(1L);
        Mockito.when(mockProductWarehouse.getStock()).thenReturn(100);

        Mockito.when(mockCartRepository.save(Mockito.any())).thenReturn(cart);
        Mockito.when(mockCartRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(cart2));

        Mockito.when(mockBuyerService.getBuyer(Mockito.anyLong())).thenReturn(buyer);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.anyLong())).thenReturn(pa1);
        Mockito.when(mockWarehouseService.getWarehouseStock(Mockito.anyLong())).thenReturn(ProductsByWarehouseDto.builder().warehouses(wLists).build());
        ScheduledCartService cartService = new ScheduledCartService(mockCartRepository, mockBuyerService, mockItemCartService, mockProductAnnouncementService, mockWarehouseService, mockBatchService);


        //act
        ScheduledCartDto c = cartService.updateCart(1L, cartDto2);

        //assertion
        assertEquals(c.getDate(), cart.getDate());
    }

    @Test
    public void deveBuscarUmCart() {
        //arrange
        ScheduledCartRepository mockCartRepository = Mockito.mock(ScheduledCartRepository.class);
        BuyerService mockBuyerService = Mockito.mock(BuyerService.class);
        ScheduledItemCartService mockItemCartService = Mockito.mock(ScheduledItemCartService.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        WarehouseService mockWarehouseService = Mockito.mock(WarehouseService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);
        WarehouseRepository.ProductWarehouse mockProductWarehouse = Mockito.mock(WarehouseRepository.ProductWarehouse.class);


        Mockito.when(mockProductWarehouse.getWarehouse()).thenReturn(1L);
        Mockito.when(mockProductWarehouse.getBatch()).thenReturn(1L);
        Mockito.when(mockProductWarehouse.getSection()).thenReturn(1L);
        Mockito.when(mockProductWarehouse.getStock()).thenReturn(100);

        Mockito.when(mockCartRepository.save(Mockito.any())).thenReturn(cart);
        Mockito.when(mockCartRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(cart2));

        Mockito.when(mockBuyerService.getBuyer(Mockito.anyLong())).thenReturn(buyer);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.anyLong())).thenReturn(pa1);
        Mockito.when(mockWarehouseService.getWarehouseStock(Mockito.anyLong())).thenReturn(ProductsByWarehouseDto.builder().warehouses(wLists).build());
        ScheduledCartService cartService = new ScheduledCartService(mockCartRepository, mockBuyerService, mockItemCartService, mockProductAnnouncementService, mockWarehouseService, mockBatchService);

        //act
        ScheduledCartDto c = cartService.get(1L);

        //assertion
        assertEquals(c.getDate(), cart.getDate());
    }

}
