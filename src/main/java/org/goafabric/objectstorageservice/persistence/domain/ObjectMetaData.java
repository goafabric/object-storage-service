package org.goafabric.objectstorageservice.persistence.domain;

import lombok.*;

@Value
public class ObjectMetaData {
    private String objectName;
    private String contentType;
    private long objectSize;
}
