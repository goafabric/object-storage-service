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

@Profile("s3-object-storage")
@Component
public class ObjectStorageLogicS3 implements ObjectStorageLogic {
    @Autowired
    private AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket.name}")
    private String bucketName;

    @Override
    @SneakyThrows
    public String persistObject(@NonNull ObjectEntryBo objectEntry) {
        s3Client.putObject(bucketName,
                getKeyName(objectEntry),
                new ByteArrayInputStream(objectEntry.getData()),
                map(objectEntry));
        return getKeyName(objectEntry);
    }

    @Override
    @SneakyThrows
    public ObjectEntryBo getObject(@NonNull String objectId) {
        final S3Object s3Object = s3Client.getObject(bucketName, objectId);
        return map(objectId,
                s3Object.getObjectContent().readAllBytes(),
                map(s3Object));
    }

    @Override
    public ObjectMetaData getObjectMetaData(@NonNull String objectId) {
        return map(s3Client.getObject(bucketName, objectId));
    }

    @Override
    public void deleteObject(@NonNull String objectId) {
        s3Client.deleteObject(bucketName, objectId);
    }

    static ObjectEntryBo map(@NonNull String keyName, byte[] data, @NonNull ObjectMetaData objectMetaDataProjection) throws IOException {
        return ObjectEntryBo.builder()
                .id(keyName)
                .data(data)
                .objectName(objectMetaDataProjection.getObjectName())
                .contentType(objectMetaDataProjection.getContentType())
                .objectSize(objectMetaDataProjection.getObjectSize())
                .build();
    }

    static ObjectMetaData map(@NonNull S3Object s3Object) {
        return new ObjectMetaData(
                s3Object.getKey(),
                s3Object.getObjectMetadata().getUserMetadata().get("filename"),
                s3Object.getObjectMetadata().getContentType(),
                s3Object.getObjectMetadata().getContentLength());
    }

    static ObjectMetadata map(@NonNull ObjectEntryBo fileEntry) {
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileEntry.getData().length);
        objectMetadata.setContentType(fileEntry.getContentType());
        objectMetadata.addUserMetadata("filename", fileEntry.getObjectName());
        return objectMetadata;
    }

    static String getKeyName(@NonNull ObjectEntryBo objectEntry) {
        final String[] fix = objectEntry.getObjectName().split("\\.");
        return fix.length == 2 ? (fix[0] + "_" + objectEntry.getId() + "." + fix[1]) : objectEntry.getId();
    }

}
