package org.goafabric.objectstorageservice.controller;

import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:test.properties")
class ObjectStorageControllerIT {
    @Autowired
    private ObjectStorageController objectStorageController;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("database.provisioning.goals", "");
    }


    @Test
    @Transactional
    @Rollback(false)
    public void testPersistObject() throws IOException {
        final String pathSource = ResourceUtils.getURL("classpath:testfiles") + "/";
        final byte[] data = Files.readAllBytes(ResourceUtils.getFile(pathSource + "lorem_ipsum.pdf").toPath());

        ObjectEntryBo objectEntry = ObjectEntryBo.builder()
                .id(UUID.randomUUID().toString())
                .data(data)
                        .objectName("lorem_ipsum.pdf")
                        .objectSize(data.length)
                        .contentType(MediaType.APPLICATION_PDF_VALUE)
                .build();

        final String id = objectStorageController.persistObject(objectEntry);
        assertThat(id).isNotNull();

        final byte[] objectData = objectStorageController.getObject(id).getData();
        assertThat(objectData).isNotNull();
        assertThat(objectData).hasSizeGreaterThan(1);

        final ObjectMetaData objectMetaData = objectStorageController.getObjectMetaData(id);
        assertThat(objectMetaData).isNotNull();
        assertThat(objectMetaData.getObjectSize()).isEqualTo(data.length);
        assertThat(objectMetaData.getContentType()).isEqualTo(MediaType.APPLICATION_PDF_VALUE);
        assertThat(objectMetaData.getObjectName()).isEqualTo("lorem_ipsum.pdf");

        //fileStorageController.deleteFile(id);
    }
    
}