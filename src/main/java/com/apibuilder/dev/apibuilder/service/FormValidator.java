package com.apibuilder.dev.apibuilder.service;

import com.apibuilder.dev.apibuilder.exception.ValidationException;
import com.apibuilder.dev.apibuilder.model.FormField;
import com.apibuilder.dev.apibuilder.model.FormTemplate;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;

@Component
public class FormValidator {

    public void validateResponses(FormTemplate template, Map<String, Object> responses) {
        for (FormField field : template.getFields()) {
            Object value = responses.get(field.getFieldId());

            if (field.isMandatory() && (value == null || String.valueOf(value).trim().isEmpty())) {
                throw new ValidationException(field.getName() + " is required");
            }

            if (value != null) {
                validateFieldValue(field, value);
            }
        }
    }

    private void validateFieldValue(FormField field, Object value) {
        switch (field.getType()) {
            case NUMBER:
                validateNumber(field, value);
                break;
            case DATE:
                validateDate(field, value);
                break;
            case LIST:
                validateList(field, value);
                break;
            case URL:
                validateUrl(field, value);
                break;
        }
    }

    private void validateNumber(FormField field, Object value) {
        try {
            Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException e) {
            throw new ValidationException(field.getName() + " must be a valid number");
        }
    }

    private void validateDate(FormField field, Object value) {
        try {
            LocalDate.parse(String.valueOf(value));
        } catch (DateTimeParseException e) {
            throw new ValidationException(field.getName() + " must be a valid date (YYYY-MM-DD)");
        }
    }

    private void validateList(FormField field, Object value) {
        if (!field.getValues().contains(String.valueOf(value))) {
            throw new ValidationException(field.getName() + " must be one of: " +
                    String.join(", ", field.getValues()));
        }
    }

    private void validateUrl(FormField field, Object value) {
        try {
            new URL(String.valueOf(value));
        } catch (MalformedURLException e) {
            throw new ValidationException(field.getName() + " must be a valid URL");
        }
    }
}
