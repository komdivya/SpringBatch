/*
package com.example.Springbatch.config;

import com.example.Springbatch.entity.User;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.io.FileWriter;
import java.io.IOException;

public class FlatfileItemWrite<User> {


    private static final String FILE_PATH = System.getProperty("user.home") + "\\output.csv";

    @Override
    public void write(Chunk<? extends User> items) throws Exception {
        try (FileWriter fileWriter = new FileWriter(FILE_PATH, true)) {
            fileWriter.append("id, firstName, lastName, description\n");
            for (User item : items) {
                fileWriter.append(item.getId().toString())
                        .append(",")
                        .append(item.getUserFirstName())
                        .append(",")
                        .append(item.getUserLastName())
                        .append(",")
                        .append(item.getDescription())
                        .append("\n");

            }
            System.out.println("CSV file written successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
*/
