package com.apibuilder.dev.apibuilder.model;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;

import org.springframework.data.mongodb.core.index.Indexed;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Document(collection = "form_templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormTemplate {
    @Id
    private String id;
    
    @jakarta.validation.constraints.NotBlank
    private String formName;
    
    @jakarta.validation.constraints.NotBlank
    @Indexed
    private String createdBy;
    
    @jakarta.validation.constraints.NotBlank
    private String projectId;
    
    @NotEmpty
    private List<FormField> fields;

    private String get_submission_url; 
    
    private String post_submission_url;
    
    @CreatedDate
    private Date createdAt;
    
    @LastModifiedDate
    private Date updatedAt;
}
