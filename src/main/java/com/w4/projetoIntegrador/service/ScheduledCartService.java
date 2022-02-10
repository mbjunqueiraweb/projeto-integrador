package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.dtos.ScheduledCartDto;
import com.w4.projetoIntegrador.dtos.ScheduledItemCartDto;
import com.w4.projetoIntegrador.dtos.WarehouseStockDto;
import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.exceptions.BusinessException;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.ScheduledCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduledCartService {

    ScheduledCartRepository scheduledCartRepository;
    BuyerService buyerService;
    ScheduledItemCartService scheduledItemCartService;
    ProductAnnouncementService productAnnouncementService;
    WarehouseService warehouseService;
    BatchService batchService;

    public ScheduledCartService(ScheduledCartRepository scheduledCartRepository,
                                BuyerService buyerService,
                                ScheduledItemCartService scheduledItemCartService,
                                ProductAnnouncementService productAnnouncementService,
                                WarehouseService warehouseService,
                                BatchService batchService) {
        this.scheduledCartRepository = scheduledCartRepository;
        this.buyerService = buyerService;
        this.scheduledItemCartService = scheduledItemCartService;
        this.productAnnouncementService = productAnnouncementService;
        this.warehouseService = warehouseService;
        this.batchService = batchService;
    }

    public ScheduledCartDto get(Long id) {
        ScheduledCart scheduledCart = getCart(id);
        ScheduledCartDto scheduledCartDto = ScheduledCartDto.convert(scheduledCart);
        scheduledCartDto.setTotalPrice(getTotalPrice(scheduledCart.getScheduledItemCarts()));
        return scheduledCartDto;
    }

    public ScheduledCart getCart(Long id) {

        try {
            ScheduledCart scheduledCart = scheduledCartRepository.findById(id).orElse(new ScheduledCart());
            List<ProductAnnouncement> productAnnouncements = new ArrayList<ProductAnnouncement>();
            for (ScheduledItemCart scheduledItemCart : scheduledCart.getScheduledItemCarts()) {
                productAnnouncements.add(scheduledItemCart.getProductAnnouncement());
            }
            return scheduledCart;
        } catch (RuntimeException e) {
            throw new NotFoundException("Cart " + id + " não encontrado na base de dados.");
        }
    }

    @Transactional
    public ScheduledCartDto create(ScheduledCartDto scheduledCartDto) {

        ScheduledCart scheduledCart = new ScheduledCart();
        if (isScheduleNotNull(scheduledCartDto)) {
            checkSchedule(scheduledCartDto.getScheduledDateTimeFrom(), scheduledCartDto.getScheduledDateTimeTo());
            scheduledCart.setScheduledDateTimeFrom(scheduledCartDto.getScheduledDateTimeFrom());
            scheduledCart.setScheduledDateTimeTo(scheduledCartDto.getScheduledDateTimeTo());
        }

        Buyer buyer = buyerService.getBuyer(scheduledCartDto.getBuyerId());
        scheduledCart.setStatusCode("aberto");
        scheduledCart.setDate(LocalDate.now());
        scheduledCart.setBuyer(buyer);

        List<ScheduledItemCart> itemCartList = checkAvailableStock(scheduledCartDto, scheduledCart);

        for (ScheduledItemCart s : itemCartList) {
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

        scheduledCart.setScheduledItemCarts(itemCartList);
        scheduledCartRepository.save(scheduledCart);
        scheduledCartDto.setId(scheduledCart.getId());
        BigDecimal totalPrice = getTotalPrice(scheduledCart.getScheduledItemCarts());
        ScheduledCartDto scheduledCardResponse = ScheduledCartDto.convert(scheduledCart);
        scheduledCardResponse.setTotalPrice(totalPrice);
        return scheduledCardResponse;
    }

    public ScheduledCartDto updateCart(Long id, ScheduledCartDto scheduledCartDto) {

        ScheduledCart scheduledCart = scheduledCartRepository.findById(id).orElse(null);

        if (scheduledCartDto.getStatusCode().equals("fechado")) {
            if (isScheduleNotNull(scheduledCartDto)) {
                checkSchedule(scheduledCartDto.getScheduledDateTimeFrom(), scheduledCartDto.getScheduledDateTimeTo());
                scheduledCart.setScheduledDateTimeFrom(scheduledCartDto.getScheduledDateTimeFrom());
                scheduledCart.setScheduledDateTimeTo(scheduledCartDto.getScheduledDateTimeTo());
                scheduledCart.setStatusCode("fechado");
            } else {
                throw new BusinessException("É necessário definir agendamento para fechar o pedido");
            }
        }

        scheduledCart.setDate(scheduledCart.getDate());
        scheduledCartRepository.save(scheduledCart);
        scheduledCartDto.setTotalPrice(getTotalPrice(scheduledCart.getScheduledItemCarts()));
        ScheduledCartDto cartResponse = ScheduledCartDto.convert(scheduledCart);
        cartResponse.setTotalPrice(scheduledCartDto.getTotalPrice());
        return cartResponse;
    }

    private BigDecimal getTotalPrice(List<ScheduledItemCart> scheduledItemCarts) {
        BigDecimal value = new BigDecimal(0);
        for (ScheduledItemCart scheduledItemCart : scheduledItemCarts) {
            ProductAnnouncement p = productAnnouncementService.getProductAnnouncement(scheduledItemCart.getProductAnnouncement().getId());
            BigDecimal itemValue = p.getPrice().multiply(new BigDecimal(String.valueOf(scheduledItemCart.getQuantity())));
            value = value.add(itemValue);
        }
        return value;
    }

    private Boolean isScheduleNotNull(ScheduledCartDto s) {
        return (s.getScheduledDateTimeFrom() != null && s.getScheduledDateTimeTo() != null);
    }

    private List<ScheduledItemCart> checkAvailableStock(ScheduledCartDto scheduledCartDto, ScheduledCart scheduledCart) {
        List<ScheduledItemCart> itemCartList = new ArrayList<>();
        for (ScheduledItemCartDto scheduledItemCartDto : scheduledCartDto.getProducts()) {
            if (scheduledItemCartDto.getQuantity() < 1)
                throw new BusinessException("É necessário comprar ao menos 1 Item");
            ProductAnnouncement p = productAnnouncementService.getProductAnnouncement(scheduledItemCartDto.getProductAnnouncementId());
            Integer stock = warehouseService
                    .getWarehouseStock(p.getId())
                    .getWarehouses()
                    .stream()
                    .map(w -> w.getTotalquantity())
                    .reduce((acc, res) -> acc + res).orElse(0);
            if (stock < scheduledItemCartDto.getQuantity()) throw new BusinessException("Produto indisponível");
            itemCartList.add(ScheduledItemCartDto.convert(scheduledItemCartDto, p, scheduledCart));
        }
        return itemCartList;
    }

    private void checkSchedule(LocalDateTime from, LocalDateTime to) {
        if (from.isBefore(LocalDateTime.now().plusDays(1)))
            throw new BusinessException("Impossível entregar na data solicitada");
        if (to.isBefore(from.plusHours(3)))
            throw new BusinessException("É necessário definir uma janela mínima de 3 horas para esta entrega");
    }
}
