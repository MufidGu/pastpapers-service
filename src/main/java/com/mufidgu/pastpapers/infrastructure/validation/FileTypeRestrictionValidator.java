package com.mufidgu.pastpapers.infrastructure.validation;

import com.mufidgu.pastpapers.infrastructure.annotation.FileTypeRestriction;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileTypeRestrictionValidator implements ConstraintValidator<FileTypeRestriction, Object> {

    private List<String> acceptedTypes;
    private String messageTemplate;

    public void initialize(FileTypeRestriction constraintAnnotation) {
        acceptedTypes = Arrays.asList(constraintAnnotation.acceptedTypes());
        messageTemplate = constraintAnnotation.message();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        if (value instanceof MultipartFile) {
            return validateFile((MultipartFile) value, context);
        } else if (value instanceof List<?>) {
            return ((List<?>) value).stream()
                    .allMatch(file -> validateFile((MultipartFile) file, context));
        } else if (value.getClass().isArray()) {
            return Arrays.stream((Object[]) value)
                    .allMatch(file -> validateFile((MultipartFile) file, context));
        }

        return false;
    }

    private boolean validateFile(MultipartFile file, ConstraintValidatorContext context) {
        try (BufferedInputStream bis = new BufferedInputStream(file.getInputStream())) {
            String contentType = detectContentType(bis);
            boolean isValid = acceptedTypes.contains(contentType);

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        String.format(messageTemplate, file.getOriginalFilename(), acceptedTypes)
                ).addConstraintViolation();
            }

            return isValid;
        } catch (IOException e) {
            return false;
        }
    }

    private String detectContentType(BufferedInputStream stream) throws IOException {
        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

        MediaType mediaType = detector.detect(stream, metadata);

        return mediaType.toString();
    }
}
