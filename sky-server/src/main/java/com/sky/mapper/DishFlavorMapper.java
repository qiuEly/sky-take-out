package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ely
 * @create 2023/10/26
 * @since 1.0.0
 */
@Mapper
public interface DishFlavorMapper  {

    public void InsertBatch(List<DishFlavor> flavors) ;


    void delteByDishId(Long id);

    List<DishFlavor> selectByDishId(Long id);

}