package org.goafabric.objectstorageservice.logic;

import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ObjectStorageLogicS3Test {

    @Test
    void persistObject() {
    }

    @Test
    void getObject() {
    }

    @Test
    void getObjectMetaData() {
    }

    @Test
    void deleteObject() {
    }

    @Test
    void findAll() {
    }

    @Test
    void map() {
    }

    @Test
    void testMap() {
    }

    @Test
    void testMapObjectEntry() {
        assertThat(ObjectStorageLogicS3.map(createObjectEntry())).isNotNull();
    }

    @Test
    void getKeyName() {
        final String keyName = ObjectStorageLogicS3.getKeyName(createObjectEntry());
        assertThat(keyName).startsWith("lorem_ipsum");
    }


    private ObjectEntryBo createObjectEntry() {
        byte[] data = new byte[0];
        return ObjectEntryBo.builder()
                .id(UUID.randomUUID().toString())
                .data(data)
                .objectName("lorem_ipsum.pdf")
                .objectSize(data.length)
                .contentType(MediaType.APPLICATION_PDF_VALUE)
                .build();
    }
}