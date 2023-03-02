package com.example.demo.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Subject {
    @Id
    private String id;
    private String subjectCode;
    private Integer numberOfCredits;

    public Subject(String subjectCode, Integer numberOfCredits) {
        this.subjectCode = subjectCode;
        this.numberOfCredits = numberOfCredits;
    }

}
