package com.luv2code.springmvc.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class JasyptEncryptorConfig {
    @Autowired
    private Environment env;

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor passwordEncryptor() {
        String secretKey = env.getProperty("ENV.APP_KEY");
        PooledPBEStringEncryptor  encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig  config = new SimpleStringPBEConfig();
        config.setPassword(secretKey); //secretkey
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
}
