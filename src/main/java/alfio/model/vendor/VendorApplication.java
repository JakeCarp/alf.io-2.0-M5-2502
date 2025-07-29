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

package alfio.model.vendor;

import java.sql.Date;
import java.util.UUID;

import ch.digitalfondue.npjt.ConstructorAnnotationRowMapper.Column;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VendorApplication {

    public enum ApplicationStatus {
        PENDING,
        APPROVED,
        REJECTED,
        PAID
    }

    private final UUID id;
    private final ApplicationStatus status;
    private final String applicantName;
    private final String storeName;
    private final String email;
    private final String phoneNumber;
    private final String description;
    private final String instagram;
    private final String portfolio;
    private final int boothTypeId;
    private final Date createdAt;
    private final int eventId;
    private final int organizationId;

    public VendorApplication(
            @Column("id") UUID id,
            @Column("status") ApplicationStatus status,
            @Column("applicant_name") String applicantName,
            @Column("store_name") String storeName,
            @Column("email") String email,
            @Column("phone_number") String phoneNumber,
            @Column("description") String description,
            @Column("instagram") String instagram,
            @Column("portfolio") String portfolio,
            @Column("booth_type_id") int boothTypeId,
            @Column("created_at") Date createdAt,
            @Column("event_id") int eventId,
            @Column("organization_id") int organizationId) {
        this.id = id;
        this.status = status;
        this.applicantName = applicantName;
        this.storeName = storeName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.instagram = instagram;
        this.portfolio = portfolio;
        this.boothTypeId = boothTypeId;
        this.createdAt = createdAt;
        this.eventId = eventId;
        this.organizationId = organizationId;
    }
}
