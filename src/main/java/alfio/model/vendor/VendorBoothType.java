package alfio.model.vendor;

import java.util.List;
import java.util.UUID;

import ch.digitalfondue.npjt.ConstructorAnnotationRowMapper.Column;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class VendorBoothType {

    private final UUID id;
    private final String name;
    private final String description;
    private final String status;
    private final int stock;
    private final double price;
    private final int eventId;

    private List<VendorApplication> approvedApplications;

    public VendorBoothType(
            @Column("id") UUID id,
            @Column("name") String name,
            @Column("description") String description,
            @Column("status") String status,
            @Column("stock") int stock,
            @Column("price") double price,
            @Column("event_id") int eventId,
            @Column("approved_applications") List<VendorApplication> approvedApplications) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status.toUpperCase();
        this.stock = stock;
        this.price = price;
        this.eventId = eventId;
    }

    public void setApprovedApplications(List<VendorApplication> approvedApplications) {
        this.approvedApplications = approvedApplications;
    }

    public UUID getId() {
        return id;
    }
}
