package org.goafabric.objectstorageservice.controller;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;

import java.io.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AWS3DirectNRIT {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    AmazonS3Client s3Client;

    @Test
    public void testAWS() throws Exception {
        doStuff();
    }

    private static String bucketName = "test";
    private static String keyName = "hosts";
    private static String uploadFileName = "/etc/hosts";

    public static void doStuff() throws Exception {
        AmazonS3 s3Client = createClient();

        try {
            System.out.println("Uploading a new object to S3 from a file\n");
            File file = new File(uploadFileName);
            // Upload file
            s3Client.putObject(new PutObjectRequest(bucketName, keyName, file));

            // Download file
            GetObjectRequest rangeObjectRequest = new GetObjectRequest(bucketName, keyName);
            S3Object objectPortion = s3Client.getObject(rangeObjectRequest);
            System.out.println("Printing bytes retrieved:");
            displayTextInputStream(objectPortion.getObjectContent());
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " + "means your request made it "
                    + "to Amazon S3, but was rejected with an error response" + " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());

        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " + "means the client encountered " + "an internal error while trying to "
                    + "communicate with S3, " + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());

        }

    }

    private static AmazonS3 createClient() {
        AWSCredentials credentials = new BasicAWSCredentials("minioadmin", "minioadmin");
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride("AWSS3V4SignerType");

        AmazonS3 s3Client = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:9100", Regions.US_EAST_1.name()))
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        return s3Client;
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        // Read one text line at a time and display.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null)
                break;

            System.out.println("    " + line);
        }
        System.out.println();
    }
}
