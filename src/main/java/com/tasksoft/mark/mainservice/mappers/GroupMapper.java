package com.tasksoft.mark.mainservice.mappers;

import com.tasksoft.mark.mainservice.dto.GroupContentDTO;
import com.tasksoft.mark.mainservice.dto.GroupMemberDto;
import com.tasksoft.mark.mainservice.entity.Group;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GroupMapper {
    public static GroupContentDTO mapGroupToDTO(Group group) {
        if (group == null) return null;

        List<GroupMemberDto> members = group.getMembers().stream()
                .map(user -> new GroupMemberDto(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstName() + " " + user.getLastName()
                ))
                .toList();

        return new GroupContentDTO(group.getName(), members);
    }
}
