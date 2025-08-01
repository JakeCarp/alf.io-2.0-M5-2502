package alfio.manager;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import alfio.model.vendor.VendorBoothType;
import alfio.repository.EventRepository;
import alfio.repository.VendorBoothTypeRepository;
import alfio.repository.VendorApplicationRepository;
import alfio.model.vendor.VendorApplication;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@Transactional
public class VendorBoothTypeManager {
    private final VendorBoothTypeRepository boothTypeRepository;
    private final EventRepository eventRepository;
    private final VendorApplicationRepository vendorApplicationRepository;

    public VendorBoothType createVendorBoothType(String name, String description, int stock, double price,
            String eventName) {
        var eventOptional = eventRepository.findOptionalEventAndOrganizationIdByShortName(eventName);
        if (eventOptional.isEmpty()) {
            throw new IllegalArgumentException("Event not found");
        }
        var eventId = eventOptional.get().getId();

        var result = boothTypeRepository.insert(name, description, "ACTIVE", stock, price,
                eventId);
        var out = new VendorBoothType(result.getKey(), name, description, "ACTIVE", stock,
                price, eventId);
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
        int eventId = eventOptional.get().getId();

        // loggin event id
        System.out.println("Event ID: " + eventId);

        var boothTypes = boothTypeRepository.findByEventId(eventId);
        boothTypes.forEach(boothType -> {
            List<VendorApplication> approvedApplications = vendorApplicationRepository
                    .findApprovedByBoothTypeId(boothType.getId());
            boothType.setApprovedApplications(approvedApplications);
        });
        return boothTypes;
    }

    public boolean updateVendorBoothType(UUID id, String name, String description,
            String status, int stock, double price) {
        return boothTypeRepository.update(id, name, description, status, stock, price) > 0;
    }

    public boolean deleteVendorBoothType(UUID id) {
        return boothTypeRepository.delete(id) > 0;
    }
}
