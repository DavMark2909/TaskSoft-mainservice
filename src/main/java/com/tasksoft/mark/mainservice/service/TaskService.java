package com.tasksoft.mark.mainservice.service;

import com.tasksoft.mark.mainservice.dto.TaskNotificationDto;
import com.tasksoft.mark.mainservice.entity.Group;
import com.tasksoft.mark.mainservice.entity.enums.NotificationType;
import com.tasksoft.mark.mainservice.entity.enums.TaskType;
import com.tasksoft.mark.mainservice.events.GroupTaskCreationEvent;
import com.tasksoft.mark.mainservice.events.SingleTaskCreationEvent;
import com.tasksoft.mark.mainservice.exception.TaskNotFoundException;
import com.tasksoft.mark.mainservice.dto.TaskCreateDto;
import com.tasksoft.mark.mainservice.entity.Task;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.repository.TaskRepository;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    private final GroupService groupService;
    private final StreamBridge streamBridge;

    public TaskService(TaskRepository taskRepository, UserService userService, GroupService groupService, StreamBridge streamBridge) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.groupService = groupService;
        this.streamBridge = streamBridge;
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task createTask(TaskCreateDto dto) {
        User assigner = userService.getUserById(dto.assignerId());
        Task task = new Task();
        task.setAssigner(assigner);
        task.setTaskType(TaskType.CREATED);
        task.setName(dto.name());
        task.setDescription(dto.description());
        task.setDueDate(dto.dueDate());
        switch (dto.type()){
            case GROUP:
                Group group = groupService.getGroup(dto.assigneeId());
                task.setGroup(group);
                break;
            case SINGLE:
                User user = userService.getUserById(dto.assigneeId());
                task.setUser(user);
                break;
        }
        Task savedTask = taskRepository.save(task);

        boolean isPersonal = dto.type() == NotificationType.SINGLE;
        TaskNotificationDto payload = new TaskNotificationDto(
                savedTask.getId(), dto.assigneeId(), isPersonal, dto.name());

        streamBridge.send("taskEvents-out-0", payload);
        return savedTask;
    }

    @Transactional
    public Task updateStatus(Long id, String name, String description, TaskType type) {
        Task task = getTaskById(id);
        task.setName(name);
        task.setDescription(description);
        task.setTaskType(type);
        return task;
    }

    public Long deleteTask(Long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
        return id;
    }

}
