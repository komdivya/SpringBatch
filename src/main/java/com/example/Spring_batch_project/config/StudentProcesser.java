package com.example.Spring_batch_project.config;

//import ch.qos.logback.classic.Logger;
import com.example.Spring_batch_project.entity.Students;
import org.springframework.batch.item.ItemProcessor;

public class StudentProcesser implements ItemProcessor<Students, Students> {

    @Override
    public Students process(Students ieam) throws Exception {
        
        return ieam;
    }
}
