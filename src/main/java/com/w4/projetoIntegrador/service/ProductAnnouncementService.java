package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.dtos.ProductAnnouncementDto;
import com.w4.projetoIntegrador.entities.Product;
import com.w4.projetoIntegrador.entities.ProductAnnouncement;
import com.w4.projetoIntegrador.entities.Seller;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.ProductAnnouncementRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductAnnouncementService {

    ProductService productService;
    ProductAnnouncementRepository productAnnouncementRepository;
    SellerService sellerService;

    public ProductAnnouncementService(ProductService productService,
                                      ProductAnnouncementRepository productAnnouncementRepository,
                                      SellerService sellerService) {
        this.productService = productService;
        this.productAnnouncementRepository = productAnnouncementRepository;
        this.sellerService = sellerService;
    }

    public ProductAnnouncement getProductAnnouncement(Long id) {
        try {
            ProductAnnouncement productAnnouncement = productAnnouncementRepository.findById(id).orElse(null);
            productAnnouncement.setProduct(productAnnouncement.getProduct());
            productAnnouncement.setSeller(productAnnouncement.getSeller());

            return productAnnouncement;
        } catch (RuntimeException e) {
            throw new NotFoundException("ProductAnnouncementDto  " + id + " não encontrada na base de dados.");
        }
    }

    public ProductAnnouncementDto get(Long id) {
        return ProductAnnouncementDto.convert(getProductAnnouncement(id));
    }

    public ProductAnnouncementDto save(ProductAnnouncementDto productAnnouncementDto) {
        Product p = productService.getProduct(productAnnouncementDto.getProductId());
        Seller seller = sellerService.getSeller(productAnnouncementDto.getSellerId());
        ProductAnnouncement productAnnouncement = ProductAnnouncementDto.convert(productAnnouncementDto,seller,p);
        productAnnouncementRepository.save(productAnnouncement);

        return productAnnouncementDto;
    }

}
