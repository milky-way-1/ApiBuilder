package com.apibuilder.dev.apibuilder.controllers;

import com.apibuilder.dev.apibuilder.dto.request.FormTemplateRequest;
import com.apibuilder.dev.apibuilder.model.User;
import com.apibuilder.dev.apibuilder.service.FormTemplateService;
import com.apibuilder.dev.apibuilder.service.JwtService;
import com.apibuilder.dev.apibuilder.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*",allowCredentials = "true")
public class FormTemplateController {
    private final FormTemplateService formTemplateService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createForm(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody FormTemplateRequest request) {
        ResponseEntity<?> tokenValidation = validateTokenAndGetEmail(token);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }
        String email = (String) tokenValidation.getBody();
        User user = userService.getUserByEmail(email);
        System.out.println(user);
        return ResponseEntity.ok(formTemplateService.createFormTemplate(user.getId(), request));
    }

    @GetMapping("/{formId}")
    public ResponseEntity<?> getForm(
            @RequestHeader("Authorization") String token,
            @PathVariable String formId) {
        ResponseEntity<?> tokenValidation = validateTokenAndGetEmail(token);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }
        return ResponseEntity.ok(formTemplateService.getFormTemplate(formId));
    }

    @GetMapping
    public ResponseEntity<?> getUserForms(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<?> tokenValidation = validateTokenAndGetEmail(token);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }
        String email = (String) tokenValidation.getBody();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(formTemplateService.getUserForms(user.getId(), PageRequest.of(page, size)));
    }

    @PutMapping("/{formId}")
    public ResponseEntity<?> updateForm(
            @RequestHeader("Authorization") String token,
            @PathVariable String formId,
            @Valid @RequestBody FormTemplateRequest request) {
        ResponseEntity<?> tokenValidation = validateTokenAndGetEmail(token);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }
        String email = (String) tokenValidation.getBody();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(formTemplateService.updateFormTemplate(user.getId(), formId, request));
    }

    @DeleteMapping("/{formId}")
    public ResponseEntity<?> deleteForm(
            @RequestHeader("Authorization") String token,
            @PathVariable String formId) {
        ResponseEntity<?> tokenValidation = validateTokenAndGetEmail(token);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }
        String email = (String) tokenValidation.getBody();
        User user = userService.getUserByEmail(email);
        formTemplateService.deleteFormTemplate(user.getId(), formId);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> validateTokenAndGetEmail(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String jwtToken = token.substring(7);

        if (jwtService.isTokenExpired(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtService.extractSubject(jwtToken);
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(email);
    }
}