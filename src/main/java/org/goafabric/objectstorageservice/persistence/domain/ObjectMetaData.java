package org.goafabric.objectstorageservice.persistence.domain;

public record ObjectMetaData (
    String objectName,
    String contentType,
    long objectSize
) {}
