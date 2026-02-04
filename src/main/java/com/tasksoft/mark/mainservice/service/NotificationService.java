package com.tasksoft.mark.mainservice.service;

import com.tasksoft.mark.mainservice.entity.Group;
import com.tasksoft.mark.mainservice.entity.Notification;
import com.tasksoft.mark.mainservice.entity.Task;
import com.tasksoft.mark.mainservice.entity.User;
import com.tasksoft.mark.mainservice.events.GroupTaskCreationEvent;
import com.tasksoft.mark.mainservice.repository.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Async
    @EventListener
    public void handleTaskCreatedGroup(GroupTaskCreationEvent event) {
        Task task = event.getTask();
        Group group = task.getAssigneeGroup();
        List<User> members = group.getMembers();
        List<Notification> notifications = new ArrayList<>();

        for (User member : members) {
            Notification notification = new Notification();
            notification.setTask(task);
            notification.setRecipient(member);
            notification.setMessage("A new task has been created: " + task.getName());
            notifications.add(notification);
        }

        notificationRepository.saveAll(notifications);
//        TODO: rabbitMQ part should be here

    }

}
