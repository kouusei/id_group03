package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheRepository extends JpaRepository<Sche, Integer>{
	List<Sche> findByScheduledateLike(String scheduledate);
	List<Sche> findBySchedule(String schedule);
	List<Sche> findByScheduledate(String scheduledate);
	List<Sche> findByScheduledateLikeOrderByScheduledateAsc(String scheduledate);
	List<Sche> findAllByOrderByScheduledateAsc();
}
