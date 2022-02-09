package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.dtos.CartDto;
import com.w4.projetoIntegrador.dtos.ItemCartDto;
import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    CartRepository cartRepository;
    BuyerService buyerService;
    ItemCartService itemCartService;
    ProductAnnouncementService productAnnouncementService;

    public CartService(  CartRepository cartRepository,
            BuyerService buyerService,
            ItemCartService itemCartService,
            ProductAnnouncementService productAnnouncementService){
        this.cartRepository = cartRepository;
        this.buyerService = buyerService;
        this.itemCartService = itemCartService;
        this.productAnnouncementService = productAnnouncementService;
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
            for (ItemCart p : cart.getItemCarts()) {
                productAnnouncements.add(p.getProductAnnouncement());
            }
            return cart;
        } catch (RuntimeException e) {
            throw new NotFoundException("Cart " + id + " não encontrado na base de dados.");
        }
    }

    public CartDto create(CartDto cartDto) {

        Buyer buyer = buyerService.getBuyer(cartDto.getBuyerId());
        Cart cart = CartDto.convert(cartDto);
        cart.setDate(LocalDate.now());
        cart.setBuyer(buyer);

        List<ItemCart> itemCartList = new ArrayList<>();

        for (ItemCartDto itemCartDto : cartDto.getProducts()) {
            ProductAnnouncement p = productAnnouncementService.getProductAnnouncement(itemCartDto.getProductAnnouncementId());
            itemCartList.add(ItemCartDto.convert(itemCartDto, p, cart));
        }
        cart.setItemCarts(itemCartList);
        cartRepository.save(cart);
        cartDto.setId(cart.getId());
        cartDto.setTotalPrice(getTotalPrice(cart.getItemCarts()));
        return CartDto.convert(cart);
    }

    public CartDto updateCart(Long id, CartDto cartDto) {
        Cart cart = cartRepository.findById(id).orElse(null);
        cart.setBuyer(buyerService.getBuyer(cartDto.getBuyerId()));
        cart.setDate(cart.getDate());
        cart.setStatusCode(cartDto.getStatusCode());

        List<ItemCart> itemCarts = new ArrayList<>();

        for (ItemCartDto itemCartDto : cartDto.getProducts()) {
            ItemCart itemCart = itemCartService.getPurchaseProduct(itemCartDto.getId());
            itemCart.setQuantity(itemCartDto.getQuantity());
            ProductAnnouncement productAnnouncement = productAnnouncementService.getProductAnnouncement(itemCartDto.getProductAnnouncementId());
            itemCart.setProductAnnouncement(productAnnouncement);
            itemCart.setCart(cart);
            itemCarts.add(itemCart);
        }
        cart.setItemCarts(itemCarts);
        cartRepository.save(cart);
        cartDto.setTotalPrice(getTotalPrice(cart.getItemCarts()));
        CartDto cartResponse = CartDto.convert(cart);
        cartResponse.setTotalPrice(cartDto.getTotalPrice());
        return cartResponse;
    }

    private BigDecimal getTotalPrice(List<ItemCart> itemCartList) {

        BigDecimal value = new BigDecimal(0);
        for (ItemCart itemCart : itemCartList) {
            ProductAnnouncement p = productAnnouncementService.getProductAnnouncement(itemCart.getProductAnnouncement().getId());
            BigDecimal itemValue = p.getPrice().multiply(new BigDecimal(String.valueOf(itemCart.getQuantity())));
            value = value.add(itemValue);
        }
        return value;
    }

}
