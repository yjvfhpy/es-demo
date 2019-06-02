package com.majie.pchild;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Parent;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
@Data
@Builder
@Document(indexName = "company", type = "employee")   //要加,不然报空指针异常
public class Employee implements Serializable {

    @Field(type = FieldType.Date)
    private Date dob;

    @Field(type = FieldType.Text)
    private String hobby;

    @Field(type = FieldType.Text)
    private String name;

    @Parent(type = "branch")
    private String parent;
}