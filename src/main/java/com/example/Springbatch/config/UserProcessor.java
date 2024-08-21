package com.example.Springbatch.config;

import com.example.Springbatch.entity.User;
import org.springframework.batch.item.ItemProcessor;

public class UserProcessor implements ItemProcessor<User, User> {
    @Override
    public User process(User item) throws Exception {
        System.out.println("Processing: " + item);
        return item;
    }
}
