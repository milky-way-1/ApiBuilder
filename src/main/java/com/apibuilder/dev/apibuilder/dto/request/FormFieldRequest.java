package com.apibuilder.dev.apibuilder.dto.request;

import com.apibuilder.dev.apibuilder.constant.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class FormFieldRequest {
    @NotBlank(message = "Field name is required")
    private String name;

    @NotNull(message = "Field type is required")
    private FieldType type;

    private boolean isUnique;
    private boolean isMandatory;
    private String validations;
    private boolean longText;
    private String placeholder;
    private List<String> values;
}
