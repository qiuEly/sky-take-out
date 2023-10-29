package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import com.zaxxer.hikari.util.PropertyElf;
import lombok.Setter;
import org.apache.ibatis.executor.statement.StatementUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ely
 * @create 2023/10/29
 * @since 1.0.0
 */
@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetMealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
    @Override
    @Transactional
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        //获取套餐Id
        Long setmealId = setmeal.getId();
        //插入套餐菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
       setmealDishes.forEach(setmealDish -> {
           setmealDish.setSetmealId(setmealId);
       });
        setmealDishMapper.insertBatch(setmealDishes);

    }

    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        int pageNum = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();

        PageHelper.startPage(pageNum,pageSize);
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        //判断套餐是否上架
       ids.forEach(id -> {
           Setmeal setmeal = setmealMapper.getById(id);
           if (StatusConstant.ENABLE.equals(setmeal.getStatus())){
               throw new RuntimeException(MessageConstant.SETMEAL_ON_SALE);
           }
       });

       ids.forEach(setmealId -> {
           setmealMapper.deleteById(setmealId);
           //删除菜品关系表中的数据
           setmealDishMapper.deleteBySetmealId(setmealId);
       });
    }

    @Override
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        //获取套餐菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }
}