package com.tasksoft.mark.mainservice.dto;

import java.util.List;

public record ModifyGroupDto(
        String updatedName,
        List<Long> userIdsToAdd,
        List<Long> userIdsToRemove
) {
}
