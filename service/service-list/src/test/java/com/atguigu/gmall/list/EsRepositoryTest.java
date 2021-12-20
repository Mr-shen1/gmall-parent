package com.atguigu.gmall.list;
import com.atguigu.gmall.list.pojo.Person;
import com.atguigu.gmall.list.repository.PersonRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Date;
import java.util.Optional;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/17
 */
@SpringBootTest
public class EsRepositoryTest {
    
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Test
    public void test01() throws Exception {


        System.out.println(personRepository);
    }


    @Test
    public void personRepositoryTest() throws Exception {


        Person person = new Person();
        person.setId(1L);
        person.setUsername("沈珂锋");
        person.setAge(18);
        person.setAddress("湖南湘乡");
        person.setBirth(new Date());

        //Person save = personRepository.save(person);
        //System.out.println(save);

        Optional<Person> result = personRepository.findById(1L);
        System.out.println(result.get());

    }

    @Test
    public void elasticsearchRestTemplateTest() throws Exception {


        /**
         * Query query,
         * Class<T> clazz,
         * IndexCoordinates index
         */


        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("userName", "沈珂锋");

        //System.out.println(matchQueryBuilder.toString());
        Query query = new NativeSearchQuery(matchQueryBuilder);

        elasticsearchRestTemplate.search(query, Person.class, IndexCoordinates.of("person"));


    }
}
