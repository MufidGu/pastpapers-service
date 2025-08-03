package com.mufidgu.pastpapers.infrastructure.configuration;

import com.mufidgu.pastpapers.Application;
import ddd.DomainService;
import ddd.Stub;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackageClasses = {Application.class},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class, Stub.class})}
)
public class DomainConfiguration {
}
