package com.mufidgu.pastpapers.infrastructure.annotation;


import com.mufidgu.pastpapers.infrastructure.validation.FileTypeRestrictionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileTypeRestrictionValidator.class)
public @interface FileTypeRestriction {
    String[] acceptedTypes();
    String message() default "File is not allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}