package org.goafabric.objectstorageservice.persistence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "object_entry")
public class ObjectEntryBo {
    @Id
    //@GeneratedValue(generator = "uuid") @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @Column(name = "object_name", nullable = false, length = 128)
    private String objectName;

    @Column(name = "content_type", nullable = false, length = 128)
    private String contentType;

    @Column(name = "object_size", nullable = false)
    private long objectSize;

    @Column(name = "data", nullable = false)
    private byte[] data;
}