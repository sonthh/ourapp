package com.son.request;

import lombok.Data;

@Data
public class GroupMessageRequest {
    private String userId;
    private String roomId;
    private String message;
}
