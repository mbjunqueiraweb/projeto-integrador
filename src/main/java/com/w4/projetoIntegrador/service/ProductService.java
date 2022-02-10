package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.dtos.BatchDto;
import com.w4.projetoIntegrador.dtos.ProductLocationDto;
import com.w4.projetoIntegrador.dtos.SectionDto;
import com.w4.projetoIntegrador.entities.Batch;
import com.w4.projetoIntegrador.dtos.ProductDto;

import com.w4.projetoIntegrador.entities.Product;
import com.w4.projetoIntegrador.entities.ProductAnnouncement;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.exceptions.BusinessException;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.BatchRepository;
import com.w4.projetoIntegrador.repository.ProductAnnouncementRepository;
import com.w4.projetoIntegrador.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    ProductRepository productRepository;
    ProductAnnouncementRepository productAnnouncementRepository;
    BatchRepository batchRepository;
    BatchService batchService;
    SectionService sectionService;

    public ProductService(ProductRepository productRepository,
                          ProductAnnouncementRepository productAnnouncementRepository,
                          BatchRepository batchRepository,
                          BatchService batchService,
                          SectionService sectionService) {
        this.productRepository = productRepository;
        this.productAnnouncementRepository = productAnnouncementRepository;
        this.batchRepository = batchRepository;
        this.batchService = batchService;
        this.sectionService = sectionService;
    }

    public Product getProduct(Long id) {
        try {
            return productRepository.findById(id).orElse(null);
        } catch (RuntimeException e) {
            throw new NotFoundException("Product " + id + " não encontrada na base de dados.");
        }
    }

    public ProductDto get(Long id) {
        return ProductDto.convert(getProduct(id));
    }

    public ProductDto save(ProductDto p) {
        Product product = ProductDto.convert(p);
        productRepository.save(product);
        return p;
    }

    public List<ProductDto> getProductDtoList() {
        List<ProductDto> productDtoList = new ArrayList<>();

        for (Product p : getProductList()) {
            productDtoList.add(ProductDto.convert(p));
        }
        return productDtoList;
    }

    private List<Product> getProductList() {

        List<Product> productList = new ArrayList<Product>();

        productList = productRepository.findAll();

        if (productList.size() == 0)
            throw new NotFoundException("Não existem produtos cadastrados na base de dados");

        return productList;
    }

    public List<ProductDto> getProductDtoListByCategory(String category) {
        List<ProductDto> productDtoList = new ArrayList<>();

        for (Product p : getProductListByCategory(category)) {
            productDtoList.add(ProductDto.convert(p));
        }
        return productDtoList;
    }


    private List<Product> getProductListByCategory(String category) {

        List<Product> productListByCategory = getProductList();

        ProductTypes mp = ProductTypes.valueOf(category);

        productListByCategory = productListByCategory
                .stream()
                .filter(product -> product.getProductType().equals(ProductTypes.valueOf(category)))
                .collect(Collectors.toList());


        if (productListByCategory.size() == 0)
            throw new NotFoundException("Não existem produtos cadastrados nessa categoria na base de dados");

        return productListByCategory;
    }

    public List<ProductLocationDto> getProductLocation(Long id) {
        try {
            ProductAnnouncement product = productAnnouncementRepository.findById(id).orElse(null);
            if (product.equals(null)) throw new NotFoundException("Não encontrado produto com id " + id);
            List<Batch> batchesList = batchRepository.findByProductAnnouncement(product);
            List<BatchDto> batchesDtoList = new ArrayList<>();
            for (Batch b : batchesList) {
                batchesDtoList.add(BatchDto.convert(b));
            }

            List<ProductLocationDto> productLocationList = new ArrayList<>();

            List<BatchRepository.SectionById> sectionById = batchRepository.getSectionsById(id);
            for (BatchRepository.SectionById b : sectionById) {
                SectionDto sDto = SectionDto.convert(sectionService.getSection(b.getSection()));
                List<BatchDto> batchDtoList = new ArrayList<>();
                List<BatchRepository.SoldStock> batchStockList = batchRepository.getStock(id, b.getSection());
                for (BatchRepository.SoldStock batchStock : batchStockList) {
                    Batch batch = batchService.getBatch(batchStock.getBatch());
                    batchDtoList.add(BatchDto.convert(batch));
                }

                ProductLocationDto p = ProductLocationDto.builder().productId(id).section(sDto).batchStockDto(batchDtoList).build();
                productLocationList.add(p);
            }
            return productLocationList;
        } catch (NullPointerException e) {
            throw new NotFoundException("Não foi encontrado produto com id " + id);
        }
    }

    public ProductLocationDto orderProductByCategory(Long id, Character ordenation) {
        List<ProductLocationDto> productLocationDtoList = getProductLocation(id);
        if (productLocationDtoList.size() == 0) throw new NotFoundException("Não encontrado");
        ProductLocationDto productLocationDto = productLocationDtoList.get(0);

        List<BatchDto> batchList;

        if (ordenation == null) return productLocationDto;

        switch (ordenation) {
            case 'L':
                batchList = productLocationDto.getBatchStockDto().stream().sorted(Comparator.comparingLong(BatchDto::getId)).collect(Collectors.toList());

                productLocationDto.setBatchStockDto(batchList);

                return productLocationDto;

            case 'C':
                batchList = productLocationDto.getBatchStockDto().stream().sorted(Comparator.comparingInt(BatchDto::getStock)).collect(Collectors.toList());

                productLocationDto.setBatchStockDto(batchList);

                return productLocationDto;

            case 'F':
                batchList = productLocationDto.getBatchStockDto().stream().sorted((b1, b2) -> String.CASE_INSENSITIVE_ORDER.compare(b1.getDueDate().toString(), b2.getDueDate().toString())).collect(Collectors.toList());

                productLocationDto.setBatchStockDto(batchList);

                return productLocationDto;

            default:
                throw new BusinessException("Parâmetro inválido");

        }
    }
}
