package cl.duoc.rutaexpress.ms_rutaexpress_shipments.service;

import cl.duoc.rutaexpress.ms_rutaexpress_shipments.dto.CreateShipmentDTO;
import cl.duoc.rutaexpress.ms_rutaexpress_shipments.dto.ShipmentDTO;
import cl.duoc.rutaexpress.ms_rutaexpress_shipments.entity.Shipment;
import cl.duoc.rutaexpress.ms_rutaexpress_shipments.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ShipmentService {

    private final ShipmentRepository repository;

    public ShipmentService(ShipmentRepository repository) {
        this.repository = repository;
    }

    public List<ShipmentDTO> getAllShipments() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ShipmentDTO getShipmentById(Long id) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con ID: " + id));
        return convertToDTO(shipment);
    }

    public ShipmentDTO createShipment(CreateShipmentDTO dto) {
        Shipment shipment = new Shipment();
        shipment.setSenderEmail(dto.getSenderEmail());
        shipment.setRecipientAddress(dto.getRecipientAddress());
        shipment.setWeightKg(dto.getWeightKg());
        shipment.setTrackingNumber("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        Shipment saved = repository.save(shipment);
        return convertToDTO(saved);
    }

    public ShipmentDTO updateStatus(Long id, String status) {
        Shipment shipment = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con ID: " + id));
        shipment.setStatus(status);
        Shipment updated = repository.save(shipment);
        return convertToDTO(updated);
    }

    public void deleteShipment(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Envío no encontrado con ID: " + id);
        }
        repository.deleteById(id);
    }

    private ShipmentDTO convertToDTO(Shipment shipment) {
        ShipmentDTO dto = new ShipmentDTO();
        dto.setId(shipment.getId());
        dto.setTrackingNumber(shipment.getTrackingNumber());
        dto.setSenderEmail(shipment.getSenderEmail());
        dto.setRecipientAddress(shipment.getRecipientAddress());
        dto.setStatus(shipment.getStatus());
        dto.setWeightKg(shipment.getWeightKg());
        dto.setCreatedAt(shipment.getCreatedAt());
        return dto;
    }
}