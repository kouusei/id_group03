package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer>{
	List<Schedule> findByScheduledateLike(String scheduledate);
}
