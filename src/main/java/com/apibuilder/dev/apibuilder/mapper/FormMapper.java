package com.apibuilder.dev.apibuilder.mapper;

import com.apibuilder.dev.apibuilder.dto.request.FormTemplateRequest;
import com.apibuilder.dev.apibuilder.dto.response.FormSubmissionResponse;
import com.apibuilder.dev.apibuilder.dto.response.FormTemplateResponse;
import com.apibuilder.dev.apibuilder.model.FormResponse;
import com.apibuilder.dev.apibuilder.model.FormTemplate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FormMapper {

    private final ModelMapper modelMapper;

    public FormTemplateResponse toFormTemplateResponse(FormTemplate template) {
        return modelMapper.map(template, FormTemplateResponse.class);
    }

    public FormTemplate toFormTemplateEntity(FormTemplateRequest request) {
        return modelMapper.map(request, FormTemplate.class);
    }

    public FormSubmissionResponse toFormSubmissionResponse(FormResponse formResponse) {
        FormSubmissionResponse response = modelMapper.map(formResponse, FormSubmissionResponse.class);
        response.setResponses(formResponse.getFieldResponses());
        return response;
    }

    public List<FormTemplateResponse> toFormTemplateResponseList(List<FormTemplate> templates) {
        return templates.stream()
                .map(this::toFormTemplateResponse)
                .collect(Collectors.toList());
    }
}
