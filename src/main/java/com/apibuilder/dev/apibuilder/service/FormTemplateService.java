package com.apibuilder.dev.apibuilder.service;

import com.apibuilder.dev.apibuilder.dto.request.FormTemplateRequest;
import com.apibuilder.dev.apibuilder.dto.response.FormTemplateResponse;
import com.apibuilder.dev.apibuilder.dto.response.PageResponse;
import com.apibuilder.dev.apibuilder.exception.ResourceNotFoundException;
import com.apibuilder.dev.apibuilder.exception.UnauthorizedException;
import com.apibuilder.dev.apibuilder.mapper.FormMapper;
import com.apibuilder.dev.apibuilder.model.FormTemplate;
import com.apibuilder.dev.apibuilder.repository.FormTemplateRepository;
import com.apibuilder.dev.apibuilder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class FormTemplateService {
    private final FormTemplateRepository formTemplateRepository;
    private final UserRepository userRepository;
    private final FormMapper formMapper;

     public FormTemplateResponse createFormTemplate(String userId, FormTemplateRequest request) {
        FormTemplate template = formMapper.toFormTemplateEntity(request);

        if (template.getFields() != null) {
            AtomicInteger counter = new AtomicInteger(0);
            template.getFields().forEach(field -> {
                field.setFieldId(generateUniqueId(counter.getAndIncrement()));
            });
        }
        
        template.setCreatedBy(userId);
        template.setCreatedAt(new Date());
        template.setUpdatedAt(new Date());

        FormTemplate savedTemplate = formTemplateRepository.save(template);

        savedTemplate.setGet_submission_url("http://localhost:4200/form?formId="+ savedTemplate.getId());
        savedTemplate.setPost_submission_url("http://localhost:4200/submissions?formId=" + savedTemplate.getId());

        savedTemplate = formTemplateRepository.save(savedTemplate);
        
        return formMapper.toFormTemplateResponse(savedTemplate);
    }

    public FormTemplateResponse getFormTemplate(String formId) {
        FormTemplate template = formTemplateRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form template not found"));
        return formMapper.toFormTemplateResponse(template);
    }


    public PageResponse<FormTemplateResponse> getUserForms(String userId, Pageable pageable) {
        Page<FormTemplate> templates = formTemplateRepository.findByCreatedBy(userId, pageable);
        Page<FormTemplateResponse> responsePage = templates.map(formMapper::toFormTemplateResponse);

        PageResponse<FormTemplateResponse> pageResponse = new PageResponse<>();
        pageResponse.setPageStats(responsePage);
        return pageResponse;
    }

    public FormTemplateResponse updateFormTemplate(String userId, String formId, FormTemplateRequest request) {
        FormTemplate template = formTemplateRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form template not found"));

        if (!template.getCreatedBy().equals(userId)) {
            throw new UnauthorizedException("Not authorized to update this form");
        }

        FormTemplate updatedTemplate = formMapper.toFormTemplateEntity(request);
        updatedTemplate.setId(template.getId());
        updatedTemplate.setCreatedBy(userId);
        updatedTemplate.setCreatedAt(template.getCreatedAt());
        updatedTemplate.setUpdatedAt(new Date());

        FormTemplate savedTemplate = formTemplateRepository.save(updatedTemplate);
        return formMapper.toFormTemplateResponse(savedTemplate);
    }

    public void deleteFormTemplate(String userId, String formId) {
        FormTemplate template = formTemplateRepository.findById(formId)
                .orElseThrow(() -> new ResourceNotFoundException("Form template not found"));

        if (!template.getCreatedBy().equals(userId)) {
            throw new UnauthorizedException("Not authorized to delete this form");
        }

        formTemplateRepository.delete(template);
    }

    public List<FormTemplateResponse> getProjectForms(String userId, String projectId) {
        validateUser(userId);
        List<FormTemplate> templates = formTemplateRepository.findByProjectId(projectId);
        return formMapper.toFormTemplateResponseList(templates);
    }

    private void validateUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
    }
        private String generateUniqueId(int counter) {
        int timestamp = (int) (System.currentTimeMillis() / 1000);
        
        int machineId = new Random().nextInt(16777215); 
        
        int increment = counter % 16777215;
        
        return String.format("%08x%06x%06x", 
                timestamp,
                machineId,
                increment);
    }
}
