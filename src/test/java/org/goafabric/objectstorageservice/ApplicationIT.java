package org.goafabric.objectstorageservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

class ApplicationIT {

    @Test
    void runApplication() {
        SpringApplication.run(Application.class, "");
    }

}