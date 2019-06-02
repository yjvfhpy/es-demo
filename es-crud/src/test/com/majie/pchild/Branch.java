package com.majie.pchild;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

/**
 * @author liujian@webull.com
 * @date 2019-05-23 20:03
 */

@Data
@Builder
@Document(indexName = "company", type = "branch")   //要加,不然报空指针异常
public class Branch implements Serializable {


    @Field(type = FieldType.Text)
    private String city;

    @Field(type = FieldType.Text)
    private String country;

    @Field(type = FieldType.Text)
    private String name;

}