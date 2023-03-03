package org.goafabric.objectstorageservice.controller;

import org.goafabric.objectstorageservice.logic.ObjectStorageLogic;
import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/objects", produces = MediaType.APPLICATION_JSON_VALUE)
@Transactional
public class ObjectStorageController {
    private final ObjectStorageLogic objectStorageLogic;

    public ObjectStorageController(ObjectStorageLogic objectStorageLogic) {
        this.objectStorageLogic = objectStorageLogic;
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    @PostMapping(value = "/persistObject", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String persistObject(@RequestBody final ObjectEntryBo fileEntry) {
        return objectStorageLogic.persistObject(fileEntry);
    }

    @GetMapping("/getObject/{objectId}")  //, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ObjectEntryBo getObject(@PathVariable(name = "objectId") final String objectId) {
        return objectStorageLogic.getObject(objectId);
    }


    @GetMapping("/getObjectMetaData/{objectId}")
    public ObjectMetaData getObjectMetaData(@PathVariable(name = "objectId") final String objectId) {
        return objectStorageLogic.getObjectMetaData(objectId);
    }

    @DeleteMapping("/deleteObject/{objectId}")
    public void deleteObject(@PathVariable(name = "objectId") final String objectId) {
        objectStorageLogic.deleteObject(objectId);
    }


}
