package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.dtos.*;
import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.repository.*;
import com.w4.projetoIntegrador.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.w4.projetoIntegrador.dtos.BatchDto;
import com.w4.projetoIntegrador.dtos.InboundDto;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTests {

    private Product product = Product.builder().id(1L).name("product").productType(ProductTypes.congelado).build();
    private Product product2 = Product.builder().id(1L).name("product").productType(ProductTypes.fresco).build();
    private Product product3 = Product.builder().id(1L).name("product").productType(ProductTypes.refrigerado).build();

    private ProductDto productDto = ProductDto.builder().name("product").productType("congelado").build();
    private ProductDto productDto2 = ProductDto.builder().name("product").productType("fresco").build();
    private ProductDto productDto3 = ProductDto.builder().name("product").productType("refrigerado").build();

    List<Product> pDtoList = Arrays.asList(product, product2, product3);

    private Seller seller = Seller.builder().id(1l).name("seller").build();

    private ProductAnnouncement pa = ProductAnnouncement.builder()
            .id(1l)
            .name("product")
            .brand("brand")
            .price(new BigDecimal(1))
            .volume(10f)
            .minimumTemperature(-10f)
            .maximumTemperature(0f)
            .product(product)
            .seller(seller)
            .build();

    private ProductAnnouncement pa2 = ProductAnnouncement.builder()
            .id(2l)
            .name("product")
            .brand("brand")
            .price(new BigDecimal(1))
            .volume(10f)
            .minimumTemperature(-10f)
            .maximumTemperature(0f)
            .product(product)
            .seller(seller)
            .build();

    private BatchDto batchDto = BatchDto.builder().id(1l).initialQuantity(10).build();

    private Agent agent = Agent.builder().id(1L).build();

    private Warehouse warehouse = Warehouse.builder().id(1L).name("warehouse").build();
    private Section section = Section.builder().id(1L).totalSpace(10000f).warehouse(warehouse).build();


    private InboundDto validInboundDto1 = InboundDto.builder()
            .agentId(1L)
            .date(LocalDateTime.now())
            .sectionId(1L)
            .batchDtoList(Arrays.asList(batchDto))
            .build();

    private Inbound inbound1 = Inbound.builder()
            .agent(agent)
            .section(section)
            .build();

    private Batch batch1 = Batch.builder().id(2L).productAnnouncement(pa).inbound(inbound1).initialQuantity(20).stock(20).dueDate(LocalDate.now()).build();
    private Batch batch2 = Batch.builder().id(1L).productAnnouncement(pa2).inbound(inbound1).initialQuantity(10).stock(10).dueDate(LocalDate.now().minusDays(5)).build();

    List<Batch> batchList = Arrays.asList(batch1, batch2);

    @Test
    public void deveCadastrarUmProduct() {

        //arrange
        ProductRepository mockProductRepository = Mockito.mock(ProductRepository.class);
        ProductAnnouncementRepository mockProductAnnouncementRepository = Mockito.mock(ProductAnnouncementRepository.class);
        BatchRepository mockBatchRepository = Mockito.mock(BatchRepository.class);

        BatchService mockBatchService = Mockito.mock(BatchService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);

        Mockito.when(mockProductRepository.save(Mockito.any())).thenReturn(product);

        ProductService productService = new ProductService(mockProductRepository, mockProductAnnouncementRepository, mockBatchRepository, mockBatchService, mockSectionService);

        //act
        ProductDto p = productService.save(productDto);

        //assertion
        assertEquals(p.getName(), productDto.getName());
    }

    @Test
    public void deveObterUmProduct() {

        //arrange
        ProductRepository mockProductRepository = Mockito.mock(ProductRepository.class);
        ProductAnnouncementRepository mockProductAnnouncementRepository = Mockito.mock(ProductAnnouncementRepository.class);
        BatchRepository mockBatchRepository = Mockito.mock(BatchRepository.class);

        BatchService mockBatchService = Mockito.mock(BatchService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);

        Mockito.when(mockProductRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));

        ProductService productService = new ProductService(mockProductRepository, mockProductAnnouncementRepository, mockBatchRepository, mockBatchService, mockSectionService);

        //act
        ProductDto p = productService.get(1L);

        //assertion
        assertEquals(p.getName(), productDto.getName());
    }

    @Test
    public void deveObterUmaListaDeProdutosPorCategoria() {

        //arrange
        ProductRepository mockProductRepository = Mockito.mock(ProductRepository.class);
        ProductAnnouncementRepository mockProductAnnouncementRepository = Mockito.mock(ProductAnnouncementRepository.class);
        BatchRepository mockBatchRepository = Mockito.mock(BatchRepository.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);

        Mockito.when(mockProductRepository.findAll()).thenReturn(pDtoList);

        ProductService productService = new ProductService(mockProductRepository, mockProductAnnouncementRepository, mockBatchRepository, mockBatchService, mockSectionService);

        //act

        List<ProductDto> pDtoList = productService.getProductDtoListByCategory("congelado");

        //assertion
        assertTrue(pDtoList.size() == 1);
        assertTrue(pDtoList.get(0).getProductType().equals("congelado"));
    }

//    @Test
//    public void deveObterUmaListaDeProdutosOrdenadosPorIdDeStock() {
//
//        //arrange
//        ProductRepository mockProductRepository = Mockito.mock(ProductRepository.class);
//        ProductAnnouncementRepository mockProductAnnouncementRepository = Mockito.mock(ProductAnnouncementRepository.class);
//        BatchRepository mockBatchRepository = Mockito.mock(BatchRepository.class);
//       // InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
//
//        BatchService mockBatchService = Mockito.mock(BatchService.class);
//        SectionService mockSectionService = Mockito.mock(SectionService.class);
//
//        Mockito.when(mockProductRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));
//        Mockito.when(mockProductAnnouncementRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pa));
//        Mockito.when(mockBatchRepository.findByProductAnnouncement(Mockito.any())).thenReturn(batchList);
//        Mockito.when(mockBatchService.getBatch(Mockito.anyLong())).thenReturn(batch1);
//        Mockito.when(mockSectionService.getSection(Mockito.anyLong())).thenReturn(section);
//        //Mockito.when(mockInboundRepository.getById(Mockito.anyLong())).thenReturn(inbound1);
//
//        ProductService productService = new ProductService(mockProductRepository, mockProductAnnouncementRepository, mockBatchRepository, mockBatchService, mockSectionService);
//
//        //act
//        ProductLocationDto plDto = productService.orderProductByCategory(1L, 'L');
//
//        //assertion
//        assertTrue(plDto.getBatchStockDto().size() == 2);
//        assertTrue(plDto.getBatchStockDto().get(0).getId().equals(1L));
//    }
//
//    @Test
//    public void deveObterUmaListaDeProdutosOrdenadosPorStock() {
//
//        //arrange
//        ProductRepository mockProductRepository = Mockito.mock(ProductRepository.class);
//        ProductAnnouncementRepository mockProductAnnouncementRepository = Mockito.mock(ProductAnnouncementRepository.class);
//        BatchRepository mockBatchRepository = Mockito.mock(BatchRepository.class);
//        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
//        SectionRepository mockSectionRepository = Mockito.mock(SectionRepository.class);
//
//        Mockito.when(mockProductRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));
//        Mockito.when(mockProductAnnouncementRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pa));
//        Mockito.when(mockBatchRepository.findByProductAnnouncement(Mockito.any())).thenReturn(batchList);
//        Mockito.when(mockInboundRepository.getById(Mockito.anyLong())).thenReturn(inbound1);
//
//        ProductService productService = new ProductService(mockProductRepository, mockProductAnnouncementRepository, mockBatchRepository, mockInboundRepository, mockSectionRepository);
//
//        //act
//        ProductLocationDto plDto = productService.orderProductByCategory(1L, 'C');
//
//        //assertion
//        assertTrue(plDto.getBatchStockDto().size() == 2);
//        assertTrue(plDto.getBatchStockDto().get(0).getStock().equals(10));
//    }
//
//    @Test
//    public void deveObterUmaListaDeProdutosOrdenadosPorDueDate() {
//
//        //arrange
//        ProductRepository mockProductRepository = Mockito.mock(ProductRepository.class);
//        ProductAnnouncementRepository mockProductAnnouncementRepository = Mockito.mock(ProductAnnouncementRepository.class);
//        BatchRepository mockBatchRepository = Mockito.mock(BatchRepository.class);
//        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
//        SectionRepository mockSectionRepository = Mockito.mock(SectionRepository.class);
//
//        Mockito.when(mockProductRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(product));
//        Mockito.when(mockProductAnnouncementRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pa));
//        Mockito.when(mockBatchRepository.findByProductAnnouncement(Mockito.any())).thenReturn(batchList);
//        Mockito.when(mockInboundRepository.getById(Mockito.anyLong())).thenReturn(inbound1);
//
//        ProductService productService = new ProductService(mockProductRepository, mockProductAnnouncementRepository, mockBatchRepository, mockInboundRepository, mockSectionRepository);
//
//        //act
//        ProductLocationDto plDto = productService.orderProductByCategory(1L, 'F');
//
//        //assertion
//        assertTrue(plDto.getBatchStockDto().size() == 2);
//        assertTrue(plDto.getBatchStockDto().get(0).getDueDate().equals(LocalDate.now().minusDays(5)));
//    }
}
