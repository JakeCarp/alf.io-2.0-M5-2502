/**
 * This file is part of alf.io.
 *
 * alf.io is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * alf.io is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with alf.io.  If not, see <http://www.gnu.org/licenses/>.
 */

package alfio.controller.api.admin;

import alfio.manager.VendorApplicationManager;
import alfio.controller.api.support.PageAndContent;
import alfio.model.PurchaseContext;
import alfio.model.vendor.VendorApplication;

import java.security.Principal;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/api/{eventName}/vendor-application")
public class VendorApplicationApiController {
        private final VendorApplicationManager vendorApplicationManager;

        @PostMapping("")
        public void createVendorApplication(
                        @PathVariable String eventName,
                        @RequestBody createVendorApplicationRequest request,
                        Principal principal) {
                vendorApplicationManager.createVendorApplication(
                                eventName, request.applicantName(), request.storeName(), request.email(),
                                request.phoneNumber(),
                                request.instagram(), request.portfolio(), request.boothTypeId(), "PENDING",
                                java.sql.Date.valueOf(java.time.LocalDate.now(ZoneId.systemDefault())),
                                request.description());
        }

        @PutMapping("/{id}/status")
        public void updateVendorApplicationStatus(
                        @PathVariable String eventName,
                        @PathVariable UUID id,
                        @RequestBody UpdateVendorApplicationStatusRequest request) {
                vendorApplicationManager.updateVendorApplicationStatus(id, request.status());
        }

        @DeleteMapping("/{id}")
        public void deleteVendorApplication(
                        @PathVariable String eventName,
                        @PathVariable UUID id) {
                vendorApplicationManager.deleteVendorApplication(id);
        }

        @GetMapping("")
        public PageAndContent<List<VendorApplication>> getVendorApplicationsByEventId(
                        @PathVariable String eventName,
                        @RequestParam int page,
                        @RequestParam int pageSize) {
                List<VendorApplication> applications = vendorApplicationManager.getVendorApplicationsByEvent(eventName);
                List<VendorApplication> content = applications.stream()
                                .skip((long) page * pageSize)
                                .limit(pageSize)
                                .collect(Collectors.toList());
                return new PageAndContent<List<VendorApplication>>(content, page);
        }

        @GetMapping("/{id}")
        public VendorApplication getVendorApplicationById(
                        @PathVariable String eventName,
                        @PathVariable UUID id) {
                return vendorApplicationManager.getVendorApplicationById(id);
        }

        public record createVendorApplicationRequest(
                        String applicantName,
                        String storeName,
                        String email,
                        String phoneNumber,
                        String instagram,
                        String portfolio,
                        UUID boothTypeId,
                        String description) {
        }

        public record UpdateVendorApplicationStatusRequest(
                        String status) {
        }

}
