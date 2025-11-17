package com.projectmanagementapi.repository;

import com.projectmanagementapi.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {

    Page<Task> findByProject_Id(Long projectId, Pageable pageable);

}
