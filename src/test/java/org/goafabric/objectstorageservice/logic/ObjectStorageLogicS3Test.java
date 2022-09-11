package org.goafabric.objectstorageservice.logic;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import org.goafabric.objectstorageservice.persistence.domain.ObjectEntryBo;
import org.goafabric.objectstorageservice.persistence.domain.ObjectMetaData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ObjectStorageLogicS3Test {

    @Mock
    private AmazonS3 s3Client;

    @InjectMocks
    private ObjectStorageLogicS3 objectStorageLogic = new ObjectStorageLogicS3();

    @Test
    void persistObject() {
        String id = objectStorageLogic.persistObject(createObjectEntry());
        assertThat(id).isNotNull();
    }

    @Test
    void getObject() {
        final S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new ByteArrayInputStream(new byte[0]));
        when(s3Client.getObject(anyString(), anyString())).thenReturn(s3Object);
        objectStorageLogic.bucketName = "test";

        final ObjectEntryBo objectEntry = objectStorageLogic.getObject("1");
        assertThat(objectEntry).isNotNull();
        assertThat(objectEntry.getId()).isNotNull();
    }

    @Test
    void getObjectMetaData() {
        when(s3Client.getObject(anyString(), anyString())).thenReturn(new S3Object());
        objectStorageLogic.bucketName = "test";

        ObjectMetaData objectMetaData = objectStorageLogic.getObjectMetaData("1");
        assertThat(objectMetaData).isNotNull();
    }

    @Test
    void deleteObject() {
        objectStorageLogic.bucketName = "test";
        objectStorageLogic.deleteObject("1");
        Mockito.verify(s3Client, times(1)).deleteObject(anyString(), anyString());
    }
    

    @Test
    void map() {
        assertThat(ObjectStorageLogicS3.map("", null, new ObjectMetaData("1", "", "", 0))).isNotNull();
    }

    @Test
    void testMapS3Object() {
        assertThat(ObjectStorageLogicS3.map(new S3Object())).isNotNull();
    }

    @Test
    void testMapObjectEntry() {
        assertThat(ObjectStorageLogicS3.map(createObjectEntry())).isNotNull();
    }

    @Test
    void getKeyNameWithExt() {
        ObjectEntryBo objectEntry = createObjectEntry();
        assertThat(ObjectStorageLogicS3.getKeyName(objectEntry))
            .startsWith("lorem_ipsum");
    }


    @Test
    void getKeyNameWithOutExt() {
        ObjectEntryBo objectEntry = createObjectEntry();
        objectEntry.setObjectName("anotherfile_without_extension");

        assertThat(ObjectStorageLogicS3.getKeyName(objectEntry)).isEqualTo(objectEntry.getId());
    }

    /*
    @Test
    void createS3Configuration() {
        assertThat(new S3Configuration().amazonS3("http://localhost:0", Boolean.TRUE, "", "", "", "test")).isNotNull();
    }

     */

    private ObjectEntryBo createObjectEntry() {
        byte[] data = new byte[0];
        return ObjectEntryBo.builder()
                .id(UUID.randomUUID().toString())
                .data(data)
                .objectName("lorem_ipsum.pdf")
                .objectSize(data.length)
                .contentType(MediaType.APPLICATION_PDF_VALUE)
                .build();
    }
}