package alfio.controller.api.admin;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import alfio.controller.api.support.PageAndContent;
import alfio.manager.VendorBoothTypeManager;
import alfio.model.vendor.VendorBoothType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/api/{eventName}/vendor-booth-type")
public class VendorBoothTypeApiController {
    private final VendorBoothTypeManager vendorBoothTypeManager;

    @PostMapping("")
    public void createVendorBoothType(
            @PathVariable String eventName,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price) {

        vendorBoothTypeManager.createVendorBoothType(name, description, price, eventName);
    }

    @PutMapping("/{id}")
    public void updateVendorBoothType(
            @PathVariable String eventName,
            @PathVariable UUID id,
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam double price) {

        vendorBoothTypeManager.updateVendorBoothType(id, name, description, price);
    }

    @DeleteMapping("/{id}")
    public void deleteVendorBoothType(
            @PathVariable String eventName,
            @PathVariable UUID id) {

        vendorBoothTypeManager.deleteVendorBoothType(id);
    }

    @GetMapping("/{id}")
    public VendorBoothType getVendorBoothType(
            @PathVariable String eventName,
            @PathVariable UUID id) {

        return vendorBoothTypeManager.getVendorBoothType(id);
    }

    @GetMapping("")
    public PageAndContent<List<VendorBoothType>> getVendorBoothTypes(
            @PathVariable String eventName,
            @RequestParam int page,
            @RequestParam(defaultValue = "50") int pageSize) {
        List<VendorBoothType> vendorBoothTypes = vendorBoothTypeManager.getVendorBoothTypesByEventId(eventName);
        List<VendorBoothType> content = vendorBoothTypes.stream()
                .skip((long) page * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return new PageAndContent<List<VendorBoothType>>(content, page);
    }

}
