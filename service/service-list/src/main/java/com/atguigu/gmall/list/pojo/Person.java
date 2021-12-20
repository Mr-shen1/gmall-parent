package com.atguigu.gmall.list.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/17
 */
@Data
@Document(indexName = "person", shards = 1, replicas = 1)
public class Person {


    @Id
    @Field(type = FieldType.Long)
    private Long id;

    @Field(type = FieldType.Keyword)
    private String username;

    @Field(type = FieldType.Keyword)
    private Integer age;

    @Field(type = FieldType.Text)
    private String address;

    @Field(type = FieldType.Date, pattern = "yyyy-dd-MM", format = DateFormat.custom)
    private Date birth;

}
