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
                        @RequestParam String applicantName,
                        @RequestParam String storeName,
                        @RequestParam String email,
                        @RequestParam String phoneNumber,
                        @RequestParam String instagram,
                        @RequestParam String portfolio,
                        @RequestParam UUID boothTypeId,
                        @RequestParam String description,
                        Principal principal) {
                vendorApplicationManager.createVendorApplication(
                                eventName, applicantName, storeName, email, phoneNumber,
                                instagram, portfolio, boothTypeId, "PENDING",
                                java.sql.Date.valueOf(java.time.LocalDate.now(ZoneId.systemDefault())), description);
        }

        @PutMapping("/{id}/status")
        public void updateVendorApplicationStatus(
                        @PathVariable String eventName,
                        @PathVariable UUID id,
                        @RequestParam String status) {
                vendorApplicationManager.updateVendorApplicationStatus(id, status);
        }

        @DeleteMapping("/{id}")
        public void deleteVendorApplication(
                        @PathVariable String eventName,
                        @PathVariable Long id) {
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
                        @PathVariable Long id) {
                return vendorApplicationManager.getVendorApplicationById(id);
        }
}
