package org.goafabric.objectstorageservice.logic;

import lombok.NonNull;
import org.goafabric.objectstorageservice.persistence.ObjectStorageRepository;
import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Profile("sql-object-storage")
@Component
public class ObjectStorageLogicSQL implements ObjectStorageLogic {
    @Autowired
    ObjectStorageRepository objectStorageRepository;

    @PostMapping(value = "/persistFile", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String persistObject(@RequestBody final ObjectEntryBo fileEntry) {
        return objectStorageRepository.save(fileEntry).getId();
    }

    @GetMapping("/getFile/{objectId}")  //, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ObjectEntryBo getObject(@PathVariable(name = "objectId") final String objectId) {
        return objectStorageRepository.getReferenceById(objectId);
    }


    @GetMapping("/getObjectMetaData/{objectId}")
    public ObjectMetaData getObjectMetaData(@PathVariable(name = "objectId") final String objectId) {
        return objectStorageRepository.findMetaDataById(objectId);
    }

    public void deleteObject(@NonNull final String fileid) {
        objectStorageRepository.deleteById(fileid);
    }

    public List<ObjectEntryBo> findAll() {
        return objectStorageRepository.findAll();
    }
}
