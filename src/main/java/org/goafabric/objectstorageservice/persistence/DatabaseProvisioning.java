package org.goafabric.objectstorageservice.persistence;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.goafabric.objectstorageservice.logic.ObjectStorageLogic;
import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.nio.file.Files;

@Component
@Slf4j
public class DatabaseProvisioning {
    @Value("${database.provisioning.goals:}")
    String goals;

    @Autowired
    ObjectStorageLogic objectStorageLogic;

    @Autowired
    ApplicationContext applicationContext;

    public void run() {
        if (goals.contains("-import-demo-data")) {
            log.info("Importing demo data ...");
            importDemoData();
        }

        if (goals.contains("-terminate")) {
            log.info("Terminating app ...");
            SpringApplication.exit(applicationContext, () -> 0); //if an exception is raised, spring will automatically terminate with 1
        }
    }

    private void importDemoData() {
        if (objectStorageLogic.findAll().isEmpty()) {
            insertData();
        }
    }

    @SneakyThrows
    private void insertData() {
        final String pathSource = ResourceUtils.getURL("classpath:testfiles") + "/";
        final byte[] data = Files.readAllBytes(ResourceUtils.getFile(pathSource + "lorem_ipsum.pdf").toPath());

        ObjectEntryBo fileEntry = ObjectEntryBo.builder()
                .id("42")
                .data(data)
                .objectName("lorem_ipsum.pdf")
                .objectSize(data.length)
                .contentType(MediaType.APPLICATION_PDF_VALUE)
                .build();

        final String id = objectStorageLogic.persistObject(fileEntry);
    }


}
