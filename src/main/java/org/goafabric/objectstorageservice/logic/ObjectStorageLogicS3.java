package org.goafabric.objectstorageservice.logic;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Profile("s3")
@Component
public class ObjectStorageLogicS3 implements ObjectStorageLogic {
    @Autowired
    private AmazonS3 s3Client;

    private static final String BUCKET_NAME = "test";

    @Override
    @SneakyThrows
    public String persistObject(ObjectEntryBo fileEntry) {
        s3Client.putObject(BUCKET_NAME,
                getKeyName(fileEntry.getId()),
                new ByteArrayInputStream(fileEntry.getData()),
                map(fileEntry));
        return fileEntry.getId();
    }

    @Override
    @SneakyThrows
    public ObjectEntryBo getObject(String objectId) {
        final S3Object s3Object = s3Client.getObject(BUCKET_NAME, getKeyName(objectId));
        return map(getKeyName(objectId),
                s3Object.getObjectContent().readAllBytes(),
                map(s3Object));
    }

    @Override
    public ObjectMetaData getObjectMetaData(String objectId) {
        return map(s3Client.getObject(BUCKET_NAME, getKeyName(objectId)));
    }

    @Override
    public void deleteObject(@NonNull String objectId) {
        s3Client.deleteObject(BUCKET_NAME, getKeyName(objectId));
    }

    @Override
    public List<ObjectEntryBo> findAll() {
        return new ArrayList<>();
    }


    private static ObjectEntryBo map(String keyName, byte[] data, ObjectMetaData objectMetaDataProjection) throws IOException {
        return ObjectEntryBo.builder()
                .id(keyName)
                .data(data)
                .objectName(objectMetaDataProjection.getObjectName())
                .contentType(objectMetaDataProjection.getContentType())
                .objectSize(objectMetaDataProjection.getObjectSize())
                .build();
    }

    private static ObjectMetaData map(S3Object s3Object) {
        final ObjectMetadata metadata = s3Object.getObjectMetadata();

        return new ObjectMetaData(
                s3Object.getKey(),
                metadata.getUserMetadata().get("filename"),
                metadata.getContentType(),
                metadata.getContentLength());
    }

    private static ObjectMetadata map(ObjectEntryBo fileEntry) {
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileEntry.getData().length);
        objectMetadata.setContentType(fileEntry.getContentType());
        objectMetadata.addUserMetadata("filename", fileEntry.getObjectName());
        return objectMetadata;
    }

    private static String getKeyName(String objectId) {
        return objectId + ".pdf";
    }

}
