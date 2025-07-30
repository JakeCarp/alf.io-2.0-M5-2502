package alfio.model.vendor;

import java.util.List;
import java.util.UUID;

import ch.digitalfondue.npjt.ConstructorAnnotationRowMapper.Column;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VendorBoothType {
    public enum BoothTypeStatus {
        ACTIVE,
        INACTIVE
    }

    private final UUID id;
    private final String name;
    private final String description;
    private final BoothTypeStatus status;
    private final int stock;
    private final double price;
    private final int eventId;

    private List<VendorApplication> approvedApplications;

    public VendorBoothType(
            @Column("id") UUID id,
            @Column("name") String name,
            @Column("description") String description,
            @Column("status") BoothTypeStatus status,
            @Column("stock") int stock,
            @Column("price") double price,
            @Column("event_id") int eventId,
            @Column("approved_applications") List<VendorApplication> approvedApplications) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.stock = stock;
        this.price = price;
        this.eventId = eventId;
        this.approvedApplications = approvedApplications;
    }

    public void setApprovedApplications(List<VendorApplication> approvedApplications) {
        this.approvedApplications = approvedApplications;
    }

    public UUID getId() {
        return id;
    }
}
