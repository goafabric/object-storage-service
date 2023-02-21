package org.goafabric.objectstorageservice;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;


/**
 * Created by amautsch on 26.06.2015.
 */

@ImportRuntimeHints(Application.ApplicationRuntimeHints.class)
@SpringBootApplication
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner init(ApplicationContext context) {
        return args -> {
            if ((args.length > 0) && ("-check-integrity".equals(args[0]))) { SpringApplication.exit(context, () -> 0);}
        };

    }

    static class ApplicationRuntimeHints implements RuntimeHintsRegistrar {
        @Override
        public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
            //resources
            hints.resources().registerResource(new ClassPathResource("/com/amazonaws/internal/config/awssdk_config_default.json"));
            hints.resources().registerResource(new ClassPathResource("com/amazonaws/partitions/endpoints.json"));

            /*
            hints.reflection().registerType(com.amazonaws.internal.config.InternalConfigJsonHelper.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(com.amazonaws.internal.config.SignerConfigJsonHelper.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(com.amazonaws.internal.config.JsonIndex.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(com.amazonaws.internal.config.HttpClientConfig.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(com.amazonaws.internal.config.HttpClientConfigJsonHelper.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(com.amazonaws.internal.config.HttpClientConfig.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(com.amazonaws.internal.config.HttpClientConfigJsonHelper.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(com.amazonaws.internal.config.HostRegexToRegionMapping.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(com.amazonaws.internal.config.HostRegexToRegionMappingJsonHelper.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
            hints.reflection().registerType(com.amazonaws.internal.config.EndpointDiscoveryConfig.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

            hints.reflection().registerType(com.amazonaws.services.s3.internal.AWSS3V4Signer.class, MemberCategory.INVOKE_DECLARED_METHODS, MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);

            try {
                hints.proxies().registerJdkProxy(org.apache.http.conn.HttpClientConnectionManager.class, org.apache.http.pool.ConnPoolControl.class, Class.forName("com.amazonaws.http.conn.Wrapped"));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
             */
        }
    }

}
