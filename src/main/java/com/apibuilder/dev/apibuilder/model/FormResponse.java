package com.apibuilder.dev.apibuilder.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Map;

@Document(collection = "form_responses")
@Data
@NoArgsConstructor
public class FormResponse {
    @Id
    private String id;

    @NotBlank
    @Indexed
    private String formTemplateId;

    @NotBlank
    @Indexed
    private String submittedBy;

    @NotNull
    private Map<String, Object> fieldResponses;

    @CreatedDate
    private Date submittedAt; 
    
    private Date updatedAt;
}
