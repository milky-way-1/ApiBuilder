package com.apibuilder.dev.apibuilder.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String name;
    private Date createdAt;
}
