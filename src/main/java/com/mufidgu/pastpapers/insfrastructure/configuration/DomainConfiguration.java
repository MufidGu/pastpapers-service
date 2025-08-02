package com.mufidgu.pastpapers.insfrastructure.configuration;

import com.mufidgu.pastpapers.Application;
import ddd.DomainService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackageClasses = {Application.class},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class})}
)
public class DomainConfiguration {
}
