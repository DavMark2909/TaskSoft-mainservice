package com.tasksoft.mark.mainservice.controllers;

import com.tasksoft.mark.mainservice.dto.*;
import com.tasksoft.mark.mainservice.entity.Group;
import com.tasksoft.mark.mainservice.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/")
    public ResponseEntity<List<GroupContentDTO>> getAllGroups() {
//  TODO: implement JWT Authentication extraction here to get the user id
        List<GroupContentDTO> list = groupService.getUserGroups(null).stream().map(this::mapGroupToDTO).toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createGroup(@RequestBody GroupCreateDto groupCreateDto) {
        Group group = groupService.createGroup(groupCreateDto);
        return new ResponseEntity<>(group.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/add-user")
    public ResponseEntity<Long> addUser(@RequestBody UserToGroupDTO userToGroupDTO) {
        groupService.addUserToGroup(userToGroupDTO.groupId(), userToGroupDTO.userId());
        return new ResponseEntity<>(userToGroupDTO.groupId(), HttpStatus.OK);
    }

    @PostMapping("/remove-user")
    public ResponseEntity<Long> removeUser(@RequestBody UserToGroupDTO userToGroupDTO) {
        groupService.removeUserFromGroup(userToGroupDTO.groupId(), userToGroupDTO.userId());
        return new ResponseEntity<>(userToGroupDTO.groupId(), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<GroupDto>> getGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    private GroupContentDTO mapGroupToDTO(Group group) {
        List<GroupMemberDto> members = group.getMembers().stream()
                .map(user -> new GroupMemberDto(user.getId(), user.getUsername())).toList();
        return new GroupContentDTO(group.getName(), members);
    }



}
