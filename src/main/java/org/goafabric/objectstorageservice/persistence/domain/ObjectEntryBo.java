package org.goafabric.objectstorageservice.persistence.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "object_entry")
public class ObjectEntryBo {
    public ObjectEntryBo(String id, String objectName, String contentType, long objectSize, byte[] data) {
        this.id = id;
        this.objectName = objectName;
        this.contentType = contentType;
        this.objectSize = objectSize;
        this.data = data;
    }
    ObjectEntryBo() {}

    @Id
    public String id;

    @Column(name = "object_name", nullable = false, length = 128)
    public String objectName;

    @Column(name = "content_type", nullable = false, length = 128)
    public String contentType;

    @Column(name = "object_size", nullable = false)
    public long objectSize;

    @Column(name = "data", nullable = false)
    public byte[] data;
}