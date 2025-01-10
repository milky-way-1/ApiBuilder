package com.apibuilder.dev.apibuilder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class FormTemplateRequest {
    @NotBlank(message = "Form name is required")
    private String formName;

    @NotBlank(message = "Project ID is required")
    private String projectId;

    @NotEmpty(message = "Fields are required")
    private List<FormFieldRequest> fields;
}
