package com.tasksoft.mark.mainservice.dto;

import com.tasksoft.mark.mainservice.entity.enums.NotificationType;

public record TaskCreateDto(
        String name,
        String description,
        Long assignerId,
        Long assigneeId,
        NotificationType type
) {
}
