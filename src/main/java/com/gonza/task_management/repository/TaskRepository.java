package com.gonza.task_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gonza.task_management.model.entity.Task;
import com.gonza.task_management.model.types.TaskStatus;
import com.gonza.task_management.model.types.TaskPriority;


public interface TaskRepository extends JpaRepository<Task,Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByAssignedToId(Long userId);

}
