package alfio.manager;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import alfio.model.vendor.VendorApplication;
import alfio.repository.EventRepository;
import alfio.repository.VendorApplicationRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
@Transactional
public class VendorApplicationManager {
    private final VendorApplicationRepository vendorApplicationRepository;
    private final EventRepository eventRepository;

    public void createVendorApplication(
            String eventName,
            String applicantName,
            String storeName,
            String email,
            String phoneNumber,
            String instagram,
            String portfolio,
            int boothTypeId,
            String status,
            Date createdAt,
            String description) {
        var eventOptional = eventRepository.findOptionalEventAndOrganizationIdByShortName(eventName);
        if (eventOptional.isEmpty()) {
            throw new IllegalArgumentException("Event not found: " + eventName);
        }
        var event = eventOptional.get();
        int eventId = event.getId();
        int organizationId = event.getOrganizationId();
        vendorApplicationRepository.insert(
                applicantName,
                storeName,
                email,
                phoneNumber,
                instagram,
                portfolio,
                boothTypeId,
                status,
                createdAt,
                eventId,
                organizationId,
                description);
    }

    public void updateVendorApplicationStatus(Long id, String status) {
        vendorApplicationRepository.updateStatus(status, id);
    }

    public void deleteVendorApplication(Long id) {
        vendorApplicationRepository.delete(id);
    }

    public List<VendorApplication> getVendorApplicationsByEvent(String eventName) {
        var eventOptional = eventRepository.findOptionalEventAndOrganizationIdByShortName(eventName);
        if (eventOptional.isEmpty()) {
            throw new IllegalArgumentException("Event not found: " + eventName);
        }
        int eventId = eventOptional.get().getId();
        return vendorApplicationRepository.findByEventId(eventId);
    }

    public VendorApplication getVendorApplicationById(Long id) {
        return vendorApplicationRepository.findById(id);
    }
}
