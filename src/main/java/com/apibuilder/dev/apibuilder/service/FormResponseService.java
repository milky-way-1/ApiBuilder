package com.apibuilder.dev.apibuilder.service;

import com.apibuilder.dev.apibuilder.dto.request.FormSubmissionRequest;
import com.apibuilder.dev.apibuilder.dto.response.FormSubmissionResponse;
import com.apibuilder.dev.apibuilder.dto.response.PageResponse;
import com.apibuilder.dev.apibuilder.exception.ResourceNotFoundException;
import com.apibuilder.dev.apibuilder.exception.UnauthorizedException;
import com.apibuilder.dev.apibuilder.mapper.FormMapper;
import com.apibuilder.dev.apibuilder.model.FormResponse;
import com.apibuilder.dev.apibuilder.model.FormTemplate;
import com.apibuilder.dev.apibuilder.repository.FormResponseRepository;
import com.apibuilder.dev.apibuilder.repository.FormTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class FormResponseService {
    private final FormResponseRepository formResponseRepository;
    private final FormTemplateRepository formTemplateRepository;
    private final FormValidator formValidator;
    private final FormMapper formMapper;

    public FormSubmissionResponse submitForm(String userId, String formId, FormSubmissionRequest request) {
        FormTemplate template = formTemplateRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form template not found"));

        formValidator.validateResponses(template, request.getResponses());

        FormResponse response = new FormResponse();
        response.setFormTemplateId(formId);
        response.setSubmittedBy(userId);
        response.setFieldResponses(request.getResponses());
        response.setSubmittedAt(new Date());

        FormResponse savedResponse = formResponseRepository.save(response);
        return formMapper.toFormSubmissionResponse(savedResponse);
    }


    public PageResponse<FormSubmissionResponse> getFormResponses(String userId, String formId, Pageable pageable) {
        FormTemplate template = formTemplateRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form template not found"));

        if (!template.getCreatedBy().equals(userId)) {
            throw new UnauthorizedException("Not authorized to view these responses");
        }

        Page<FormResponse> responses = formResponseRepository.findByFormTemplateId(formId, pageable);
        Page<FormSubmissionResponse> responsePage = responses.map(formMapper::toFormSubmissionResponse);

        PageResponse<FormSubmissionResponse> pageResponse = new PageResponse<>();
        pageResponse.setPageStats(responsePage);
        return pageResponse;
    }

    public PageResponse<FormSubmissionResponse> getUserResponses(String userId, Pageable pageable) {
        Page<FormResponse> responses = formResponseRepository.findBySubmittedBy(userId, pageable);
        Page<FormSubmissionResponse> responsePage = responses.map(formMapper::toFormSubmissionResponse);

        PageResponse<FormSubmissionResponse> pageResponse = new PageResponse<>();
        pageResponse.setPageStats(responsePage);
        return pageResponse;
    }


    public FormSubmissionResponse getResponse(String userId, String responseId) {
        FormResponse response = formResponseRepository.findById(responseId)
                .orElseThrow(() -> new ResourceNotFoundException("Form response not found"));

        if (!response.getSubmittedBy().equals(userId)) {
            throw new UnauthorizedException("Not authorized to view this response");
        }

        return formMapper.toFormSubmissionResponse(response);
    }

}