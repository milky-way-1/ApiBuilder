package com.apibuilder.dev.apibuilder.model;

import com.apibuilder.dev.apibuilder.constant.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FormField {
    @NotBlank
    private String fieldId;

    @NotBlank
    private String name;

    @NotNull
    private FieldType type;

    private boolean isUnique;
    private boolean isMandatory;
    private String validations;
    private boolean longText;
    private String placeholder;
    private List<String> values; // For LIST type fields
}

