package com.tasksoft.mark.mainservice.dto;

public record TaskNotificationDto(
        Long taskId,
        Long recipientId,
        boolean personal,
        String message

)
{}
