package org.goafabric.objectstorageservice.logic;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Profile("s3-object-storage")
@Component
public class ObjectStorageLogicS3 implements ObjectStorageLogic {
    @Autowired
    private AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket.name}")
    private String BUCKET_NAME;

    @Override
    @SneakyThrows
    public String persistObject(@NonNull ObjectEntryBo objectEntry) {
        s3Client.putObject(BUCKET_NAME,
                getKeyName(objectEntry),
                new ByteArrayInputStream(objectEntry.getData()),
                map(objectEntry));
        return getKeyName(objectEntry);
    }

    @Override
    @SneakyThrows
    public ObjectEntryBo getObject(@NonNull String objectId) {
        final S3Object s3Object = s3Client.getObject(BUCKET_NAME, objectId);
        return map(objectId,
                s3Object.getObjectContent().readAllBytes(),
                map(s3Object));
    }

    @Override
    public ObjectMetaData getObjectMetaData(@NonNull String objectId) {
        return map(s3Client.getObject(BUCKET_NAME, objectId));
    }

    @Override
    public void deleteObject(@NonNull String objectId) {
        s3Client.deleteObject(BUCKET_NAME, objectId);
    }

    @Override
    public List<ObjectEntryBo> findAll() {
        return new ArrayList<>();
    }


    private static ObjectEntryBo map(@NonNull String keyName, byte[] data, @NonNull ObjectMetaData objectMetaDataProjection) throws IOException {
        return ObjectEntryBo.builder()
                .id(keyName)
                .data(data)
                .objectName(objectMetaDataProjection.getObjectName())
                .contentType(objectMetaDataProjection.getContentType())
                .objectSize(objectMetaDataProjection.getObjectSize())
                .build();
    }

    private static ObjectMetaData map(@NonNull S3Object s3Object) {
        final ObjectMetadata metadata = s3Object.getObjectMetadata();

        return new ObjectMetaData(
                s3Object.getKey(),
                metadata.getUserMetadata().get("filename"),
                metadata.getContentType(),
                metadata.getContentLength());
    }

    private static ObjectMetadata map(@NonNull ObjectEntryBo fileEntry) {
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileEntry.getData().length);
        objectMetadata.setContentType(fileEntry.getContentType());
        objectMetadata.addUserMetadata("filename", fileEntry.getObjectName());
        return objectMetadata;
    }

    private static String getKeyName(@NonNull ObjectEntryBo objectEntry) {
        final String[] fix = objectEntry.getObjectName().split("\\.");
        return fix.length == 2 ? (fix[0] + "_" + objectEntry.getId() + "." + fix[1]) : objectEntry.getId();
    }

}
