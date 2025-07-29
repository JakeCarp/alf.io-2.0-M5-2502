package alfio.model.vendor;

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
    private final double price;
    private final int eventId;

    public VendorBoothType(
            @Column("id") UUID id,
            @Column("name") String name,
            @Column("description") String description,
            @Column("price") double price,
            @Column("event_id") int eventId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.eventId = eventId;
    }
}
