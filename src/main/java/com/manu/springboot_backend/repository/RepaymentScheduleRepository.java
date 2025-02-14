package com.manu.springboot_backend.repository;

import com.manu.springboot_backend.model.RepaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepaymentScheduleRepository extends JpaRepository<RepaymentSchedule, Long> {
}
