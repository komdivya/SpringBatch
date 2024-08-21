package com.example.Springbatch.config;

import com.example.Springbatch.entity.User;
import com.example.Springbatch.repositiry.ReversSpringBatchRepo;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class ReversSpringConfig {

    @Autowired
    private ReversSpringBatchRepo reversSpringBatchRepo;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private DataSource dataSource;

    //reader
    @Bean
    public JdbcCursorItemReader<User> springReader(){
        JdbcCursorItemReader<User> itemReader = new JdbcCursorItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setSql("Select id, user_first_name, user_last_name, description from User_table");
        itemReader.setRowMapper(new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setUser_first_name(rs.getString("user_first_name"));
                user.setUser_last_name(rs.getString("user_last_name"));
                user.setDescription(rs.getString("description"));
                return user;
            }
        });
        return itemReader;
    }

    //processer

    @Bean
    public UserProcessor processor(){
        return new UserProcessor();

    }

    //writer
    @Bean
    public FlatFileItemWriter<User> itemWriter(){
        FlatFileItemWriter<User> fileItemWriter = new FlatFileItemWriter();
        fileItemWriter.setResource(new FileSystemResource("C:\\Users\\komdivya\\Desktop\\Java_Code\\STS\\ReversSpringbatch\\src\\main\\resources\\output.csv"));
        DelimitedLineAggregator<User> lineAggregator = new DelimitedLineAggregator<>();
        BeanWrapperFieldExtractor<User> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"id", "user_first_name", "user_last_name", "description"});
        lineAggregator.setFieldExtractor(fieldExtractor);
        fileItemWriter.setLineAggregator(lineAggregator);
        return fileItemWriter;

    }



    //job
    @Bean
    public Job exprotUserJob(){
        return new JobBuilder("user_job", jobRepository)
                .start(step1())
                .build();
    }


    //step
    public Step step1(){
        return new StepBuilder("step1", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(springReader())
                .processor(processor())
                .writer(itemWriter())
                .build();
    }

}
