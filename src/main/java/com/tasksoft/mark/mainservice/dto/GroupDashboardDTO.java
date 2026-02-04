package com.tasksoft.mark.mainservice.dto;

import java.util.List;

public record GroupDashboardDTO(
        String name,
        GroupContentDTO usersDto,
        HomeDashboardDTO.DashboardStats stats,
        List<HomeDashboardDTO.TaskSummary> tasks
)
{}
