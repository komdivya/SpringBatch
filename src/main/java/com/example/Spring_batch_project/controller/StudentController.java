package com.example.Spring_batch_project.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @GetMapping("/Student")
    public void loadCsvToDb() throws Exception{

        JobParameters jobParameters =
                new JobParametersBuilder().addLong("Start-A", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(job, jobParameters);




    }

}