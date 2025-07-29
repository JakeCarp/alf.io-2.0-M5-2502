package alfio.manager;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import alfio.model.vendor.VendorBoothType;
import alfio.repository.EventRepository;
import alfio.repository.VendorBoothTypeRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@Transactional
public class VendorBoothTypeManager {
    private final VendorBoothTypeRepository boothTypeRepository;
    private final EventRepository eventRepository;

    public VendorBoothType createVendorBoothType(String name, String description, double price, String eventName) {
        var eventOptional = eventRepository.findOptionalEventAndOrganizationIdByShortName(eventName);
        if (eventOptional.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }
        var eventId = eventOptional.get().getId();

        var result = boothTypeRepository.insert(name, description, price, eventId);
        var out = new VendorBoothType(result.getKey(), name, description, price, eventId);
        return out;
    }

    public VendorBoothType getVendorBoothType(UUID id) {
        return boothTypeRepository.findById(id);
    }

    public List<VendorBoothType> getVendorBoothTypesByEventId(String eventName) {
        var eventOptional = eventRepository.findOptionalEventAndOrganizationIdByShortName(eventName);
        if (eventOptional.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }
        var eventId = eventOptional.get().getId();

        return boothTypeRepository.findByEventId(eventId);
    }

    public boolean updateVendorBoothType(UUID id, String name, String description, double price) {
        return boothTypeRepository.update(id, name, description, price) > 0;
    }

    public boolean deleteVendorBoothType(UUID id) {
        return boothTypeRepository.delete(id) > 0;
    }
}
