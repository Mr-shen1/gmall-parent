package com.atguigu.gmall.list.repository;

import com.atguigu.gmall.model.list.Goods;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/18
 */
@Repository
public interface GoodsRepository extends PagingAndSortingRepository<Goods, Long> {


}
