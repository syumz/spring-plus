package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String nickName;
    private final String email;

    public UserResponse(Long id, String nickName, String email) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
    }
}
