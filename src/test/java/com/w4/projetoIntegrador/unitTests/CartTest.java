package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.dtos.*;
import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.repository.CartRepository;
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

public class CartTest {

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


    ItemCart itemCart = ItemCart.builder().productAnnouncement(pa1).quantity(10).build();
    ItemCart itemCart2 = ItemCart.builder().productAnnouncement(pa2).quantity(10).build();

    List<ItemCart> itemCartsList = Arrays.asList(itemCart, itemCart2);


    ItemCartDto itemCartDto = ItemCartDto.builder().id(1L).productAnnouncementId(1L).quantity(10).build();
    ItemCartDto itemCartDto2 = ItemCartDto.builder().id(1L).productAnnouncementId(2L).quantity(10).build();

    List<ItemCartDto> itemCartsListDto = Arrays.asList(itemCartDto, itemCartDto2);

    Buyer buyer = Buyer.builder().id(1L).name("buyer").build();

    Cart cart = Cart.builder().date(LocalDate.now()).statusCode("aberto").buyer(buyer).itemCarts(itemCartsList).build();
    Cart cart2 = Cart.builder().date(LocalDate.now()).statusCode("fechado").buyer(buyer).itemCarts(itemCartsList).build();

    CartDto cartDto = CartDto.builder().date(LocalDate.now()).statusCode("aberto").products(itemCartsListDto).buyerId(1L).build();

    CartDto cartDto2 = CartDto.builder()
            .date(LocalDate.now())
            .statusCode("fechado")
            .products(itemCartsListDto)
            .buyerId(1L)
            .build();

    WarehouseStockDto wsDto = WarehouseStockDto.builder().batch(1L).section(1L).totalquantity(1000).build();
    List<WarehouseStockDto> wLists = Arrays.asList(wsDto);

    @Test
    public void deveCadastrarUmCart() {

        //arrange
        CartRepository mockCartRepository = Mockito.mock(CartRepository.class);
        BuyerService mockBuyerService = Mockito.mock(BuyerService.class);
        ItemCartService mockItemCartService = Mockito.mock(ItemCartService.class);
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
        CartService cartService = new CartService(mockCartRepository, mockBuyerService, mockItemCartService, mockProductAnnouncementService, mockWarehouseService, mockBatchService);

        //act
        CartDto c = cartService.create(cartDto);

        //assertion
        assertEquals(c.getDate(), cart.getDate());
    }

    @Test
    public void deveAtualizarUmCart() {

        //arrange
        CartRepository mockCartRepository = Mockito.mock(CartRepository.class);
        BuyerService mockBuyerService = Mockito.mock(BuyerService.class);
        ItemCartService mockItemCartService = Mockito.mock(ItemCartService.class);
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
        CartService cartService = new CartService(mockCartRepository, mockBuyerService, mockItemCartService, mockProductAnnouncementService, mockWarehouseService, mockBatchService);


        //act
        CartDto c = cartService.updateCart(1L, cartDto2);

        //assertion
        assertEquals(c.getDate(), cart.getDate());
    }

    @Test
    public void deveBuscarUmCart() {
        //arrange
        CartRepository mockCartRepository = Mockito.mock(CartRepository.class);
        BuyerService mockBuyerService = Mockito.mock(BuyerService.class);
        ItemCartService mockItemCartService = Mockito.mock(ItemCartService.class);
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
        CartService cartService = new CartService(mockCartRepository, mockBuyerService, mockItemCartService, mockProductAnnouncementService, mockWarehouseService, mockBatchService);

        //act
        CartDto c = cartService.get(1L);

        //assertion
        assertEquals(c.getDate(), cart.getDate());
    }

}
