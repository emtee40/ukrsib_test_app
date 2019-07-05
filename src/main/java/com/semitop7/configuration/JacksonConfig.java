package com.semitop7.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper createJsonMapper() {
        return new ObjectMapper()
                .enable(READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
    }

    @Bean
    public XmlMapper createXmlMapper() {
        return (XmlMapper) new XmlMapper()
                .enable(READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
                .registerModule(new JaxbAnnotationModule());
    }
}
