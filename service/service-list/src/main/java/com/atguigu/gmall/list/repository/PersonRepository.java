package com.atguigu.gmall.list.repository;

import com.atguigu.gmall.list.pojo.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/17
 */
@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, Long> {


}
