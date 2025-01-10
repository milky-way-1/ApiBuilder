package com.apibuilder.dev.apibuilder.dto.response;
import com.apibuilder.dev.apibuilder.constant.FieldType;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class FormFieldResponse {
    private String fieldId;
    private String name;
    private FieldType type;
    private boolean isUnique;
    private boolean isMandatory;
    private String validations;
    private boolean longText;
    private String placeholder;
    private List<String> values;
}
