package com.apibuilder.dev.apibuilder.repository;

import com.apibuilder.dev.apibuilder.model.FormResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FormResponseRepository extends MongoRepository<FormResponse, String> {
    Page<FormResponse> findByFormTemplateId(String formTemplateId, Pageable pageable);
    Page<FormResponse> findBySubmittedBy(String userId, Pageable pageable);
    List<FormResponse> findByFormTemplateId(String formTemplateId);
}
