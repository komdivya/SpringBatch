package com.example.Spring_batch_project.config;

import com.example.Spring_batch_project.entity.Students;
import com.example.Spring_batch_project.repository.StudentRepository;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;

import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class StudentConfig{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    // create Reader

    @Bean
    public FlatFileItemReader<Students> studentReader() {
        FlatFileItemReader<Students> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("C:\\Users\\komdivya\\Desktop\\Java_Code\\STS\\Relationship\\Spring-batch-project\\src\\main\\resources\\students.csv"));
        itemReader.setName("Student_reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    
    /*@Bean
    public FlatFileItemReader<Students> studentReader() {
        FlatFileItemReader<Students> itemReader = new FlatFileItemReader<>();
        try {
            itemReader.setResource(new FileSystemResource("src/main/resources/student.csv"));
            itemReader.setName("Student_reader");
            itemReader.setLinesToSkip(1);
            itemReader.setLineMapper(lineMapper());
        } catch (Exception e) {
            System.err.println("Failed to initialize the reader: " + e.getMessage());
            e.printStackTrace();
        }
        return itemReader;
    }*/


	/*
	 * @Bean public FlatFileItemReader<Students> studentReader(){
	 * FlatFileItemReader<Students> itemReader = new FlatFileItemReader<>();
	 * itemReader.setResource((new
	 * FileSystemResource("src/main/resources/student.csv")));
	 * itemReader.setName("Student_reader"); itemReader.setLinesToSkip(1);
	 * itemReader.setLineMapper(lineMapper()); return itemReader; }
	 */

    private LineMapper<Students> lineMapper() {

        DefaultLineMapper<Students> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);
        tokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

        BeanWrapperFieldSetMapper<Students> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Students.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    // create Processer

    @Bean
    public StudentProcesser studentProcesser(){
        return new StudentProcesser()   ;
    }

    //create Writer
    @Bean
    public RepositoryItemWriter<Students> repositoryItemWriter(){

        RepositoryItemWriter<Students> repositoryItemWriter = new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(studentRepository);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }

    // create step
    @Bean
    public Step studentStep() {
        return new StepBuilder("step-1", jobRepository)
                .<Students, Students>chunk(10, transactionManager)
                .reader(studentReader())
                .processor(studentProcesser())
                .writer(repositoryItemWriter())
                .transactionManager(transactionManager) // Set the transaction manager
                .build();
    }
    // create Job

    @Bean
    public Job studentJob() {
        return new JobBuilder("student-job", jobRepository)
                .flow(studentStep())
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(10);
        return taskExecutor;
    }
}
