package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ely
 * @create 2023/10/26
 * @since 1.0.0
 */
@Slf4j
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealMapper setmealMapper;


    @Override
    @Transactional // 事务
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.Insert(dish);

        Long dishId = dish.getId();
        //插入菜品口味 dish_id name value
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
          flavors.forEach(dishFlavor -> {
              dishFlavor.setDishId(dishId);
          });
            dishFlavorMapper.InsertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //分页插件进行查询
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        //构造查询条件
        Page<DishVO> dishVOPage = dishMapper.pageQuery(dishPageQueryDTO);
        //封装结果
        return new PageResult(dishVOPage.getTotal(),dishVOPage.getResult());
    }

    @Override
    @Transactional //开启事务，防止数据不一致
    public void delete(List<Long> ids) {
        //当前菜品是否在起售中，如果起售，不能直接下架
        ids.forEach(id -> {
            Dish dish = dishMapper.selectById(id);
            log.info("{}",dish.getName());
            if(Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });
        //检测是否被套餐关联了，如果被套餐关联，不能直接删除
        List<Long> setmealIds =setmealMapper.getSetmealIdsByDishIds(ids);

        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //删除菜品
        ids.forEach(id -> {
            dishMapper.deleteById(id);
            dishFlavorMapper.delteByDishId(id);
        });


    }

    @Override
    public DishVO getById(Long id) {
        //查询菜品数据
        Dish dish = dishMapper.selectById(id);
        //查询口味
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectByDishId(id);

        //封装
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return  dishVO;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        //更新菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        //更新口味,封装dishId
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dishDTO.getId());
            });
        }
        dishFlavorMapper.InsertBatch(flavors);
    }
}