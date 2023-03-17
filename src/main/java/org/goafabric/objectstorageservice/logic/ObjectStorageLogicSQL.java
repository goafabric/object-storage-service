package org.goafabric.objectstorageservice.logic;

import org.goafabric.objectstorageservice.persistence.ObjectStorageRepository;
import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Profile("sql-object-storage")
@Component
public class ObjectStorageLogicSQL implements ObjectStorageLogic {
    private final ObjectStorageRepository objectStorageRepository;

    public ObjectStorageLogicSQL(ObjectStorageRepository objectStorageRepository) {
        this.objectStorageRepository = objectStorageRepository;
    }

    @PostMapping(value = "/persistFile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String persistObject(final ObjectEntryBo fileEntry) {
        return objectStorageRepository.save(fileEntry).id;
    }

    @GetMapping("/getFile/{objectId}")  //, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ObjectEntryBo getObject(String objectId) {
        return objectStorageRepository.getReferenceById(objectId);
    }


    @GetMapping("/getObjectMetaData/{objectId}")
    public ObjectMetaData getObjectMetaData(String objectId) {
        return objectStorageRepository.findMetaDataById(objectId);
    }

    public void deleteObject(final String fileid) {
        objectStorageRepository.deleteById(fileid);
    }
}
