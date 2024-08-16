package com.example.Spring_batch_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "Student_Table")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Students {

    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "First_Name")
    private String firstName;
    @Column(name = "Last_Name")
    private String lastName;
    @Column(name = "Email")
    private String email;
    @Column(name = "Gender")
    private String gender;
    @Column(name = "Contact_No")
    private String contactNo;
    @Column(name = "Country")
    private String country;
    @Column(name = "DOB")
    private String dob;
}
