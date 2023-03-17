package org.goafabric.objectstorageservice.logic;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Profile("s3-object-storage")
@Component
public class ObjectStorageLogicS3 implements ObjectStorageLogic {
    private final AmazonS3 s3Client;

    private final String bucketName;

    private final Boolean anonymousFilesEnabled;

    public ObjectStorageLogicS3(AmazonS3 s3Client,  @Value("${cloud.aws.s3.bucket.name}") String bucketName, @Value("${cloud.aws.s3.anonymous-files.enabled}") Boolean anonymousFilesEnabled) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.anonymousFilesEnabled = anonymousFilesEnabled;
    }

    @Override
    public String persistObject(ObjectEntryBo objectEntry) {
        s3Client.putObject(bucketName,
                getKeyName(objectEntry, anonymousFilesEnabled),
                new ByteArrayInputStream(objectEntry.data),
                map(objectEntry));
        return getKeyName(objectEntry, anonymousFilesEnabled);
    }

    @Override
    public ObjectEntryBo getObject(String objectId) {
        try {
            var s3Object = s3Client.getObject(bucketName, objectId);
            return map(objectId,
                    s3Object.getObjectContent().readAllBytes(),
                    map(s3Object.getObjectMetadata()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObjectMetaData getObjectMetaData(String objectId) {
        return map(s3Client.getObjectMetadata(bucketName, objectId));
    }

    @Override
    public void deleteObject(String objectId) {
        s3Client.deleteObject(bucketName, objectId);
    }

    static ObjectEntryBo map(String keyName, byte[] data, ObjectMetaData objectMetaDataProjection)  {
        return new ObjectEntryBo(
                keyName,
                objectMetaDataProjection.objectName(),
                objectMetaDataProjection.contentType(),
                objectMetaDataProjection.objectSize(),
                data);
    }

    static ObjectMetaData map(ObjectMetadata s3ObjectMetaData) {
        return new ObjectMetaData(
                s3ObjectMetaData.getUserMetadata().get("filename"),
                s3ObjectMetaData.getContentType(),
                s3ObjectMetaData.getContentLength());
    }

    static ObjectMetadata map(ObjectEntryBo fileEntry) {
        final ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(fileEntry.data.length);
        objectMetadata.setContentType(fileEntry.contentType);
        objectMetadata.addUserMetadata("filename", fileEntry.objectName);
        return objectMetadata;
    }

    static String getKeyName(ObjectEntryBo objectEntry, Boolean anonymousFilesEnabled) {
        final String[] fix = objectEntry.objectName.split("\\.");
        return (fix.length < 2 || anonymousFilesEnabled)
                        ? objectEntry.id
                        : (fix[0] + "_" + objectEntry.id + "." + fix[1]);
    }

}
