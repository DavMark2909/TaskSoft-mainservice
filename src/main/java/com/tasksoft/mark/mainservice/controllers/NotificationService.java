package com.tasksoft.mark.mainservice.controllers;

import com.tasksoft.mark.mainservice.repository.NotificationRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Async
    @EventListener
    public void handleTaskCreated(){

    }

}


