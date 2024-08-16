package com.example.Spring_batch_project.repository;

import com.example.Spring_batch_project.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository extends JpaRepository<Students, Integer> {
}
