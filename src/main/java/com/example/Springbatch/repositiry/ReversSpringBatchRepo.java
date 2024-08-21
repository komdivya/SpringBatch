package com.example.Springbatch.repositiry;

import com.example.Springbatch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReversSpringBatchRepo extends JpaRepository<User, Integer> {
}
