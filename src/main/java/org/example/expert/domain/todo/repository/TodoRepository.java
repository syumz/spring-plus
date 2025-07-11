package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryQuery {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE (:weather IS NULL OR t.weather = :weather) AND (:startTime IS NULL OR t.modifiedAt >= :startTime) AND (:endTime IS NULL OR t.modifiedAt <= :endTime) ORDER BY t.modifiedAt DESC")
    Page<Todo> findTodosWithUserByWeatherAndModifiedAtDesc(Pageable pageable, String weather, LocalDateTime startTime, LocalDateTime endTime);
}
