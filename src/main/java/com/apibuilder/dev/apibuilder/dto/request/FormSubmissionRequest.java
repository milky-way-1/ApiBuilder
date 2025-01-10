package com.apibuilder.dev.apibuilder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
public class FormSubmissionRequest {
    @NotBlank(message = "Form template ID is required")
    private String formTemplateId;

    @NotNull(message = "Responses are required")
    private Map<String, Object> responses;
}
