package org.goafabric.objectstorageservice.xfunctional;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("s3")
public class S3Configuration {

    @Bean
    public AmazonS3 amazonS3(
            @Value("${cloud.aws.s3.endpoint}") String serviceEndpoint,
            @Value("${cloud.aws.s3.pathstyle.enabled}") Boolean pathStyleAccessEnabled,
            @Value("${cloud.aws.credentials.access-key}") String accesKey,
            @Value("${cloud.aws.credentials.secret-key}") String secretKey,
            @Value("${cloud.aws.region.static:null}") String region) {
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        serviceEndpoint, region))
                .withPathStyleAccessEnabled(pathStyleAccessEnabled)
                .withCredentials(new AWSStaticCredentialsProvider(
                        new BasicAWSCredentials(accesKey, secretKey)))
                .build();
    }
}
