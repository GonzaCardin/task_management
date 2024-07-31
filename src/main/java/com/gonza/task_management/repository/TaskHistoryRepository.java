package com.gonza.task_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gonza.task_management.model.entity.TaskHistory;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {

}
