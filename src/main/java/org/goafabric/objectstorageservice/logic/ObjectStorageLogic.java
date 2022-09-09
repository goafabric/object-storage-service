package org.goafabric.objectstorageservice.logic;

import lombok.NonNull;
import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ObjectStorageLogic {
    String persistObject(@RequestBody final ObjectEntryBo fileEntry);

    ObjectEntryBo getObject(@PathVariable(name = "objectId") final String objectId);


    ObjectMetaData getObjectMetaData(@PathVariable(name = "objectId") final String objectId);

    void deleteObject(@NonNull final String objectId);

    List<ObjectEntryBo> findAll();
}
