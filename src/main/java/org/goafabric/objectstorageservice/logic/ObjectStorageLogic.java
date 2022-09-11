package org.goafabric.objectstorageservice.logic;

import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;

import java.util.List;

public interface ObjectStorageLogic {
    String persistObject(ObjectEntryBo fileEntry);

    ObjectEntryBo getObject(String objectId);


    ObjectMetaData getObjectMetaData(String objectId);

    void deleteObject(String objectId);

    List<ObjectEntryBo> findAll();
}
