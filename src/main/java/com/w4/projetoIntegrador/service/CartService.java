package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.dtos.CartDto;
import com.w4.projetoIntegrador.dtos.ItemCartDto;
import com.w4.projetoIntegrador.dtos.WarehouseStockDto;
import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.exceptions.BusinessException;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    CartRepository cartRepository;
    BuyerService buyerService;
    ItemCartService itemCartService;
    ProductAnnouncementService productAnnouncementService;
    WarehouseService warehouseService;
    BatchService batchService;

    public CartService(CartRepository cartRepository,
                       BuyerService buyerService,
                       ItemCartService itemCartService,
                       ProductAnnouncementService productAnnouncementService,
                       WarehouseService warehouseService,
                       BatchService batchService) {
        this.cartRepository = cartRepository;
        this.buyerService = buyerService;
        this.itemCartService = itemCartService;
        this.productAnnouncementService = productAnnouncementService;
        this.warehouseService = warehouseService;
        this.batchService = batchService;
    }

    public CartDto get(Long id) {
        Cart cart = getCart(id);
        CartDto cartDto = CartDto.convert(cart);
        cartDto.setTotalPrice(getTotalPrice(cart.getItemCarts()));
        return cartDto;
    }

    public Cart getCart(Long id) {

        try {
            Cart cart = cartRepository.findById(id).orElse(new Cart());
            List<ProductAnnouncement> productAnnouncements = new ArrayList<ProductAnnouncement>();
            for (ItemCart itemCart : cart.getItemCarts()) {
                productAnnouncements.add(itemCart.getProductAnnouncement());
            }
            return cart;
        } catch (RuntimeException e) {
            throw new NotFoundException("Cart " + id + " não encontrado na base de dados.");
        }
    }

    @Transactional
    public CartDto create(CartDto cartDto) {

        Cart cart = new Cart();

        Buyer buyer = buyerService.getBuyer(cartDto.getBuyerId());
        cart.setStatusCode("aberto");
        cart.setDate(LocalDate.now());
        cart.setBuyer(buyer);

        List<ItemCart> itemCartList = checkAvailableStock(cartDto, cart);

        for (ItemCart s : itemCartList) {
            Integer remaining = s.getQuantity();
            List<WarehouseStockDto> warehouseStockList = warehouseService.getWarehouseStock(s.getProductAnnouncement().getId()).getWarehouses();
            for (WarehouseStockDto w : warehouseStockList) {
                Integer currentStock = w.getTotalquantity();
                if (remaining < currentStock) {
                    batchService.decrementBatch(remaining, w.getBatch());
                    break;
                }
                if (remaining >= currentStock) {
                    batchService.decrementBatch(currentStock, w.getBatch());
                    remaining = remaining - currentStock;
                    if (remaining == 0) break;
                }
            }
        }

        cart.setItemCarts(itemCartList);
        cartRepository.save(cart);
        cartDto.setId(cart.getId());
        BigDecimal totalPrice = getTotalPrice(cart.getItemCarts());
        CartDto scheduledCardResponse = CartDto.convert(cart);
        scheduledCardResponse.setTotalPrice(totalPrice);
        return scheduledCardResponse;
    }

    public CartDto updateCart(Long id, CartDto cartDto) {

        Cart cart = cartRepository.findById(id).orElse(null);

        cart.setStatusCode("fechado");

        cart.setDate(cart.getDate());
        cartRepository.save(cart);
        cartDto.setTotalPrice(getTotalPrice(cart.getItemCarts()));
        CartDto cartResponse = CartDto.convert(cart);
        cartResponse.setTotalPrice(cartDto.getTotalPrice());
        return cartResponse;
    }

    private BigDecimal getTotalPrice(List<ItemCart> itemCarts) {
        BigDecimal value = new BigDecimal(0);
        for (ItemCart itemCart : itemCarts) {
            ProductAnnouncement p = productAnnouncementService.getProductAnnouncement(itemCart.getProductAnnouncement().getId());
            BigDecimal itemValue = p.getPrice().multiply(new BigDecimal(String.valueOf(itemCart.getQuantity())));
            value = value.add(itemValue);
        }
        return value;
    }

    private List<ItemCart> checkAvailableStock(CartDto cartDto, Cart cart) {
        List<ItemCart> itemCartList = new ArrayList<>();
        for (ItemCartDto itemCartDto : cartDto.getProducts()) {
            if (itemCartDto.getQuantity() < 1)
                throw new BusinessException("É necessário comprar ao menos 1 Item");
            ProductAnnouncement p = productAnnouncementService.getProductAnnouncement(itemCartDto.getProductAnnouncementId());
            Integer stock = warehouseService
                    .getWarehouseStock(p.getId())
                    .getWarehouses()
                    .stream()
                    .map(w -> w.getTotalquantity())
                    .reduce((acc, res) -> acc + res).orElse(0);
            if (stock < itemCartDto.getQuantity()) throw new BusinessException("Produto indisponível");
            itemCartList.add(ItemCartDto.convert(itemCartDto, p, cart));
        }
        return itemCartList;
    }
}
