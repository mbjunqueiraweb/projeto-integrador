package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.dtos.BatchDto;
import com.w4.projetoIntegrador.dtos.InboundDto;
import com.w4.projetoIntegrador.entities.*;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.exceptions.BusinessException;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InboundService {

    private InboundRepository inboundRepository;

    private ProductAnnouncementService productAnnouncementService;

    private SectionService sectionService;

    private AgentService agentService;

    private BatchService batchService;

    public InboundService(InboundRepository inboundRepository,
                          ProductAnnouncementService productAnnouncementService,
                          SectionService sectionService,
                          AgentService agentService,
                          BatchService batchService) {

        this.inboundRepository = inboundRepository;
        this.productAnnouncementService = productAnnouncementService;
        this.sectionService = sectionService;
        this.agentService = agentService;
        this.batchService = batchService;
    }

    public InboundDto create(InboundDto inboundDto) {
        try {
            Section section = sectionService.getSection(inboundDto.getSectionId());
            Agent agent = agentService.getAgent(inboundDto.getAgentId());

            if (!agent.getSection().getId().equals(section.getId()))
                throw new BusinessException("O representante não pertence a este setor");

            List<BatchDto> batchDtoList = inboundDto.getBatchDtoList();
            List<ProductAnnouncement> productsAnnouncementsList = batchDtoList.stream()
                    .map(b -> productAnnouncementService.getProductAnnouncement(b.getProductId()))
                    .collect(Collectors.toList());

            checkBatchList(batchDtoList, productsAnnouncementsList, section);
            checkSectionCapacity(section, getInboundVolume(batchDtoList, productsAnnouncementsList));

            List<Batch> batchList = batchListFromInbound(batchDtoList, productsAnnouncementsList, section);

            inboundDto.setDate(LocalDateTime.now());
            Inbound inbound = InboundDto.convert(inboundDto, batchList, section, agent);
            inbound.getBatchList().stream().forEach(batch -> batch.setInbound(inbound));
            System.out.println("a");
            inboundRepository.save(inbound);
            System.out.println("b");

            return InboundDto.convert(inbound);
        } catch (RuntimeException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public InboundDto get(Long id) {
        try {
            Inbound inbound = inboundRepository.findById(id).orElse(null);
            return InboundDto.convert(inbound);
        } catch (RuntimeException e) {
            throw new NotFoundException("Inbound order " + id + " não encontrado na base de dados.");
        }
    }

    public InboundDto update(Long id, InboundDto inbound) {
        Inbound foundedInbound = inboundRepository.findById(id).orElse(null);
        Section section = sectionService.getSection(inbound.getSectionId());
        foundedInbound.setSection(section);

        List<BatchDto> payloadBatchList = inbound.getBatchDtoList();
        List<Batch> newBatchList = new ArrayList<>();

        List<ProductAnnouncement> productsAnnouncementsList = payloadBatchList.stream()
                .map(b -> productAnnouncementService.getProductAnnouncement(b.getProductId()))
                .collect(Collectors.toList());

        checkBatchList(payloadBatchList, productsAnnouncementsList, section);
        checkSectionCapacity(section, getInboundVolume(payloadBatchList, productsAnnouncementsList));

        for (BatchDto payloadBatch : inbound.getBatchDtoList()) {
            Batch foundedBatch = batchService.getBatch(payloadBatch.getId());
            if (foundedBatch.getInbound().getId() != id)
                throw new BusinessException("Id de batch não corresponde ao inbound");
            foundedBatch.setProductAnnouncement(productAnnouncementService.getProductAnnouncement(payloadBatch.getProductId()));
            Integer sold = foundedBatch.getInitialQuantity() - foundedBatch.getStock();
            foundedBatch.setStock(payloadBatch.getInitialQuantity() - sold);
            foundedBatch.setInitialQuantity(payloadBatch.getInitialQuantity());
            foundedBatch.setManufacturingDateTime(payloadBatch.getManufacturingDateTime());
            foundedBatch.setDueDate(payloadBatch.getDueDate());
            foundedBatch.setCurrentTemperature(payloadBatch.getCurrentTemperature());
            foundedBatch.setInbound(foundedInbound);
            newBatchList.add(foundedBatch);
        }
        foundedInbound.setBatchList(newBatchList);
        inboundRepository.save(foundedInbound);
        return InboundDto.convert(foundedInbound);
    }

    private void checkTypeMatch(ProductTypes p1, ProductTypes p2) {
        if (!p1.equals(p2)) {
            String message = "Um produto " + p1 + " não pode ser armazenado em um setor de " + p2;
            throw new BusinessException(message);
        }
    }

    private void checkSectionCapacity(Section s, Float inboundVolume) {
        List<InboundRepository.SectionsCapacity> capacitySections = inboundRepository.findCapacityAllSections();

        List<InboundRepository.SectionsCapacity> sectionsCapacity = capacitySections.stream().filter(cap -> cap.getId().equals(s.getId())).collect(Collectors.toList());
        if (sectionsCapacity.size() == 0) return;

        Float sectionCurrentVolume = capacitySections.stream().filter(cap -> cap.getId().equals(s.getId())).collect(Collectors.toList()).get(0).getVolume();
        Float availableSectionVolume = s.getTotalSpace() - sectionCurrentVolume;
        if (inboundVolume > availableSectionVolume) throw new BusinessException("Não há espaço disponível neste setor");
    }

    private void checkBusinessValidateBatch(BatchDto batchDto, ProductAnnouncement pa) {
        if(batchDto.getInitialQuantity() < 1)
            throw new BusinessException("O lote deve conter ao menos 1 item");
        if (batchDto.getDueDate().isBefore(LocalDate.now().plusDays(10)))
            throw new BusinessException("Data de validade não pode ser inferior a daqui a 10 dias");
        if (batchDto.getManufacturingDateTime().isAfter(LocalDateTime.now()))
            throw new BusinessException("Data de fabricação não pode ser futura");
        if (batchDto.getCurrentTemperature() > pa.getMaximumTemperature())
            throw new BusinessException("O lote está acima da temperatura adequada para o produto e não pode ser aceito");
        if (batchDto.getCurrentTemperature() < pa.getMinimumTemperature())
            throw new BusinessException("O lote está abaixo da temperatura adequada para o produto e não pode ser aceito");
    }

    private Float getInboundVolume(List<BatchDto> batchDtoList, List<ProductAnnouncement> productAnnouncementList) {
        Float inboundVolume = 0F;

        for (int i = 0; i < batchDtoList.size(); i++) {
            BatchDto b = batchDtoList.get(i);
            ProductAnnouncement p = productAnnouncementList.get(i);
            inboundVolume += p.getVolume() * b.getInitialQuantity();
        }
        return inboundVolume;
    }

    private List<Batch> batchListFromInbound(List<BatchDto> batchDtoList, List<ProductAnnouncement> productsAnnouncementsList, Section section) {
        List<Batch> batchList = new ArrayList<>();
        for (int i = 0; i < batchDtoList.size(); i++) {
            ProductAnnouncement pa = productsAnnouncementsList.get(i);
            BatchDto b = batchDtoList.get(i);
            Batch batch = BatchDto.convert(b, pa, b.getInitialQuantity());
            batchList.add(batch);
        }
        return batchList;
    }

    private void checkBatchList(List<BatchDto> batchDtoList, List<ProductAnnouncement> productsAnnouncementsList, Section section) {
        for (int i = 0; i < batchDtoList.size(); i++) {
            ProductAnnouncement pa = productsAnnouncementsList.get(i);
            checkTypeMatch(pa.getProduct().getProductType(), section.getType());
            checkBusinessValidateBatch(batchDtoList.get(i), pa);
        }
    }
}