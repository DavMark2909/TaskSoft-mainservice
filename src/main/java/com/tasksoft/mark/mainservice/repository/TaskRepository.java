package com.tasksoft.mark.mainservice.repository;

import com.tasksoft.mark.mainservice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
