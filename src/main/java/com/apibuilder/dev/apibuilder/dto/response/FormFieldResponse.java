package com.apibuilder.dev.apibuilder.dto.response;
import com.apibuilder.dev.apibuilder.constant.FieldType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class FormFieldResponse {
    private String fieldId;
    private String name;
    private FieldType type;  
    
    @JsonProperty("isUnique")
    private boolean isUnique; 
    
    @JsonProperty("isMandatory")
    private boolean isMandatory; 
    
    private String validations;
    private boolean longText;
    private String placeholder;
    private List<String> values;
}
