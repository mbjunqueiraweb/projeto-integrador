package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.dtos.BatchDto;
import com.w4.projetoIntegrador.dtos.InboundDto;
import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.exceptions.BusinessException;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.InboundRepository;
import com.w4.projetoIntegrador.service.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InboundsTests {

    private Product product1 = Product.builder().id(1L).name("product").productType(ProductTypes.congelado).build();
    private Seller seller1 = Seller.builder().id(1l).name("seller").build();
    private ProductAnnouncement pa1 = ProductAnnouncement.builder()
            .id(1l)
            .name("product")
            .brand("brand")
            .price(new BigDecimal(1))
            .volume(10f)
            .minimumTemperature(- 10f)
            .maximumTemperature(0f)
            .product(product1)
            .seller(seller1)
            .build();

    private BatchDto batchDto1 = BatchDto.builder().id(1l).initialQuantity(10).manufacturingDateTime(LocalDateTime.now()).dueDate(LocalDate.now().plusDays(30)).currentTemperature(0f).build();
    private Batch batch1 = Batch.builder().id(1L).productAnnouncement(pa1).initialQuantity(10).build();
    private Section section1 = Section.builder().id(1L).totalSpace(10000f).type(ProductTypes.congelado).build();
    private Section section2 = Section.builder().id(2L).totalSpace(10000f).type(ProductTypes.refrigerado).build();
    private Section section3 = Section.builder().id(1L).totalSpace(10000f).type(ProductTypes.congelado).build();

    private Agent agent1 = Agent.builder().id(1L).section(section1).build();
    private Agent agent2 = Agent.builder().id(1L).section(section2).build();
    private Agent agent3 = Agent.builder().id(1L).section(section3).build();


    private InboundDto validInboundDto1 = InboundDto.builder()
            .agentId(1L)
            .date(LocalDateTime.now())
            .sectionId(1L)
            .batchDtoList(Arrays.asList(batchDto1))
            .build();

    private InboundDto invalidInboundDto1 = InboundDto.builder()
            .agentId(2L)
            .date(LocalDateTime.now())
            .sectionId(1L)
            .batchDtoList(Arrays.asList(batchDto1))
            .build();

    private InboundDto invalidInboundDto2 = InboundDto.builder()
            .agentId(2L)
            .date(LocalDateTime.now())
            .sectionId(2L)
            .batchDtoList(Arrays.asList(batchDto1))
            .build();

    private InboundDto invalidInboundDto3 = InboundDto.builder()
            .agentId(3L)
            .date(LocalDateTime.now())
            .sectionId(3L)
            .batchDtoList(Arrays.asList(batchDto1))
            .build();

    private Inbound inbound1 = Inbound.builder()
            .id(1L)
            .agent(agent1)
            .section(section1)
            .batchList(Arrays.asList(batch1))
            .build();

    private Inbound inbound2 = Inbound.builder()
            .agent(agent2)
            .section(section2)
            .build();

    private Batch batch1a = Batch.builder()
            .id(1L)
            .productAnnouncement(pa1)
            .initialQuantity(10)
            .stock(10)
            .inbound(inbound1).build();

    private Batch batch2a = Batch.builder()
            .id(1L)
            .productAnnouncement(pa1)
            .initialQuantity(10)
            .stock(10)
            .inbound(inbound2).build();

    @Test
    public void deveBuscarUmaInboundOrder() {

        //arrange
        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);
        AgentService mockAgentService = Mockito.mock(AgentService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);

        Mockito.when(mockInboundRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(inbound1));
        Mockito.when(mockSectionService.getSection(Mockito.any())).thenReturn(section1);
        Mockito.when(mockAgentService.getAgent(Mockito.any())).thenReturn(agent1);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.any())).thenReturn(pa1);

        InboundService inboundService = new InboundService(mockInboundRepository,
                mockProductAnnouncementService, mockSectionService, mockAgentService, mockBatchService);

        //act
        InboundDto inboundDtoReceived = inboundService.get(1L);

        //assertion
        assertEquals(inboundDtoReceived.getSectionId(), validInboundDto1.getSectionId());
    }

    @Test
    public void deveCadastrarUmaInboundOrder() {

        //arrange
        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);
        AgentService mockAgentService = Mockito.mock(AgentService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);

        Mockito.when(mockInboundRepository.save(Mockito.any())).thenReturn(inbound1);
        Mockito.when(mockSectionService.getSection(Mockito.any())).thenReturn(section1);
        Mockito.when(mockAgentService.getAgent(Mockito.any())).thenReturn(agent1);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.any())).thenReturn(pa1);

        InboundService inboundService = new InboundService(mockInboundRepository,
                mockProductAnnouncementService, mockSectionService, mockAgentService, mockBatchService);

        //act
        InboundDto inboundDtoReceived = inboundService.create(validInboundDto1);

        //assertion
        assertEquals(inboundDtoReceived.getAgentId(), validInboundDto1.getAgentId());
    }

    @Test
    public void deveAtualizarUmaInboundOrder() {

        //arrange
        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);
        AgentService mockAgentService = Mockito.mock(AgentService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);

        Mockito.when(mockInboundRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(inbound1));

        Mockito.when(mockBatchService.getBatch(Mockito.any())).thenReturn(batch1a);
        Mockito.when(mockInboundRepository.save(Mockito.any())).thenReturn(inbound1);
        Mockito.when(mockSectionService.getSection(Mockito.any())).thenReturn(section1);
        Mockito.when(mockAgentService.getAgent(Mockito.any())).thenReturn(agent1);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.any())).thenReturn(pa1);

        InboundService inboundService = new InboundService(mockInboundRepository,
                mockProductAnnouncementService, mockSectionService, mockAgentService, mockBatchService);

        //act
        InboundDto inboundDtoReceived = inboundService.update(1L, validInboundDto1);

        //assertion
        assertEquals(inboundDtoReceived.getAgentId(), validInboundDto1.getAgentId());
    }

    @Test
    public void deveLancarExcessaoAoAtualizarInboundComBatchsDeOutrosInbounds() {

        //arrange
        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);
        AgentService mockAgentService = Mockito.mock(AgentService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);

        Mockito.when(mockInboundRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(inbound1));

        Mockito.when(mockBatchService.getBatch(Mockito.any())).thenReturn(batch2a);
        Mockito.when(mockInboundRepository.save(Mockito.any())).thenReturn(inbound1);
        Mockito.when(mockSectionService.getSection(Mockito.any())).thenReturn(section1);
        Mockito.when(mockAgentService.getAgent(Mockito.any())).thenReturn(agent1);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.any())).thenReturn(pa1);

        InboundService inboundService = new InboundService(mockInboundRepository,
                mockProductAnnouncementService, mockSectionService, mockAgentService, mockBatchService);

        //act
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> inboundService.update(1L, validInboundDto1));

        //assertion
        assertTrue(businessException.getMessage().contains("Id de batch não corresponde ao inbound"));

    }


    @Test
    public void deveLançarExceçãoAoCadastrarInboundComRepresentanteQueNaoPertenceAoSetor() {

        //arrange
        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);
        AgentService mockAgentService = Mockito.mock(AgentService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);

        Mockito.when(mockSectionService.getSection(Mockito.any())).thenReturn(section1);
        Mockito.when(mockAgentService.getAgent(Mockito.any())).thenReturn(agent2);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.any())).thenReturn(pa1);

        InboundService inboundService = new InboundService(mockInboundRepository,
                mockProductAnnouncementService, mockSectionService, mockAgentService, mockBatchService);

        //act
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> inboundService.create(invalidInboundDto1));

        //assertion
        assertTrue(businessException.getMessage().contains("O representante não pertence a este setor"));
    }

    @Test
    public void deveLançarExceçãoAoCadastrarInboundComProdutoIncompativelComSetor() {

        //arrange
        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);
        AgentService mockAgentService = Mockito.mock(AgentService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);

        Mockito.when(mockSectionService.getSection(Mockito.any())).thenReturn(section2);
        Mockito.when(mockAgentService.getAgent(Mockito.any())).thenReturn(agent2);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.any())).thenReturn(pa1);

        InboundService inboundService = new InboundService(mockInboundRepository,
                mockProductAnnouncementService, mockSectionService, mockAgentService, mockBatchService);

        //act
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> inboundService.create(invalidInboundDto2));

        //assertion
        assertTrue(businessException.getMessage().contains("Um produto congelado não pode ser armazenado em um setor de refrigerado"));
    }

    @Test
    public void deveLançarExcessaoAoCadastrarInboundEmSetorSemEspacoDisponivel() {

        //arrange
        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);
        AgentService mockAgentService = Mockito.mock(AgentService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);
        InboundRepository.SectionsCapacity mockSectionCapacityInterface = Mockito.mock(InboundRepository.SectionsCapacity.class);

        Mockito.when(mockInboundRepository.save(Mockito.any())).thenReturn(inbound1);
        Mockito.when(mockSectionCapacityInterface.getVolume()).thenReturn(10000F);
        Mockito.when(mockSectionCapacityInterface.getId()).thenReturn(1L);
        List<InboundRepository.SectionsCapacity> s = new ArrayList<>();
        s.add(mockSectionCapacityInterface);

        Mockito.when(mockInboundRepository.findCapacityAllSections()).thenReturn(s);

        Mockito.when(mockSectionService.getSection(Mockito.any())).thenReturn(section1);
        Mockito.when(mockAgentService.getAgent(Mockito.any())).thenReturn(agent1);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.any())).thenReturn(pa1);

        InboundService inboundService = new InboundService(mockInboundRepository,
                mockProductAnnouncementService, mockSectionService, mockAgentService, mockBatchService);

        //act
        BusinessException businessException = assertThrows(BusinessException.class,
                () -> inboundService.create(invalidInboundDto3));

        //assertion
        assertTrue(businessException.getMessage().contains("Não há espaço disponível neste setor"));
    }

    @Test
    public void deveLançarExcessaoAoBuscarUmaInboundPorIdnexistente() {

        //arrange
        InboundRepository mockInboundRepository = Mockito.mock(InboundRepository.class);
        ProductAnnouncementService mockProductAnnouncementService = Mockito.mock(ProductAnnouncementService.class);
        SectionService mockSectionService = Mockito.mock(SectionService.class);
        AgentService mockAgentService = Mockito.mock(AgentService.class);
        BatchService mockBatchService = Mockito.mock(BatchService.class);

        Mockito.when(mockInboundRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Mockito.when(mockSectionService.getSection(Mockito.any())).thenReturn(section1);
        Mockito.when(mockAgentService.getAgent(Mockito.any())).thenReturn(agent1);
        Mockito.when(mockProductAnnouncementService.getProductAnnouncement(Mockito.any())).thenReturn(pa1);

        InboundService inboundService = new InboundService(mockInboundRepository,
                mockProductAnnouncementService, mockSectionService, mockAgentService, mockBatchService);

        //act
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> inboundService.get(1L));

        //assertion
        assertTrue(notFoundException.getMessage().contains("Inbound order 1 não encontrado na base de dados."));
    }

}
