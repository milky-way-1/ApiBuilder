package com.apibuilder.dev.apibuilder.controllers;

import com.apibuilder.dev.apibuilder.dto.request.FormSubmissionRequest;
import com.apibuilder.dev.apibuilder.model.User;
import com.apibuilder.dev.apibuilder.service.FormResponseService;

import com.apibuilder.dev.apibuilder.service.JwtService;
import com.apibuilder.dev.apibuilder.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/responses")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/",allowedHeaders = "*")
public class FormResponseController {
    private final FormResponseService formResponseService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/submit/{formId}")
    public ResponseEntity<?> submitForm(
            @RequestHeader("Authorization") String token,
            @PathVariable String formId,
            @Valid @RequestBody FormSubmissionRequest request) {
        ResponseEntity<?> tokenValidation = validateTokenAndGetEmail(token);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }
        String email = (String) tokenValidation.getBody();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(formResponseService.submitForm(user.getId(), formId, request));
    }

    @GetMapping("/form/{formId}")
    public ResponseEntity<?> getFormResponses(
            @RequestHeader("Authorization") String token,
            @PathVariable String formId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<?> tokenValidation = validateTokenAndGetEmail(token);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }
        String email = (String) tokenValidation.getBody();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(formResponseService.getFormResponses(user.getId(), formId,
                PageRequest.of(page, size)));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserResponses(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<?> tokenValidation = validateTokenAndGetEmail(token);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }
        String email = (String) tokenValidation.getBody();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(formResponseService.getUserResponses(user.getId(),
                PageRequest.of(page, size)));
    }

    @GetMapping("/{responseId}")
    public ResponseEntity<?> getResponse(
            @RequestHeader("Authorization") String token,
            @PathVariable String responseId) {
        ResponseEntity<?> tokenValidation = validateTokenAndGetEmail(token);
        if (tokenValidation.getStatusCode() != HttpStatus.OK) {
            return tokenValidation;
        }
        String email = (String) tokenValidation.getBody();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(formResponseService.getResponse(user.getId(), responseId));
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