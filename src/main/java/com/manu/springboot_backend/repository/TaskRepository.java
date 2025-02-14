package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {


}
