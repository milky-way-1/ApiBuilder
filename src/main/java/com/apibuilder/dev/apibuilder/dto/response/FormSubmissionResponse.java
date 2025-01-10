package com.apibuilder.dev.apibuilder.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
public class FormSubmissionResponse {
    private String id;
    private String formTemplateId;
    private String submittedBy;
    private Map<String, Object> responses;
    private Date submittedAt;
    private Date updatedAt;
}
