package com.apibuilder.dev.apibuilder.repository;

import com.apibuilder.dev.apibuilder.model.FormTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormTemplateRepository extends MongoRepository<FormTemplate, String> {
    Page<FormTemplate> findByCreatedBy(String userId, Pageable pageable);
    List<FormTemplate> findByProjectId(String projectId);
    boolean existsByFormNameAndCreatedBy(String formName, String userId);
}
