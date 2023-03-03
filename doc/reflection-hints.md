//resources
hints.resources().registerResource(new ClassPathResource("/com/amazonaws/internal/config/awssdk_config_default.json"));
hints.resources().registerResource(new ClassPathResource("com/amazonaws/partitions/endpoints.json"));

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
