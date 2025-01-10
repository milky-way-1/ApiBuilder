package com.apibuilder.dev.apibuilder.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class FormTemplateResponse {
    private String id;
    private String formName;
    private String createdBy;
    private String projectId;
    private List<FormFieldResponse> fields;
    private Date createdAt;
    private Date updatedAt;
}
