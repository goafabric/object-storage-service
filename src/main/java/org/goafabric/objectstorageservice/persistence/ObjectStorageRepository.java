package org.goafabric.objectstorageservice.persistence;

import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectStorageRepository extends JpaRepository<ObjectEntryBo, String> {
    ObjectMetaData findMetaDataById(String id);
}




