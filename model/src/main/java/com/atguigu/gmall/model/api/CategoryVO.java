package com.atguigu.gmall.model.api;

import lombok.Data;

import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/10
 */
@Data
public class CategoryVO {


    private Long categoryId;
    private String categoryName;
    private List<CategoryVO> categoryChild;
}
