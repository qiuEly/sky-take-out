package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈套餐管理〉
 *
 * @author ely
 * @create 2023/10/29
 * @since 1.0.0
 */
@RestController
@Slf4j
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐管理")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @PostMapping
    @Operation(summary = "新增套餐")
    @CacheEvict(cacheNames = "setmealCache",key = "#setmeal.categoryId")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐");
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询套餐");
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    @DeleteMapping
    @Operation(summary = "删除套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)//allEntries = true表示删除所有缓存
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除套餐");
        setmealService.deleteBatch(ids);
        return Result.success();
    }
    //获得对应id的套餐参数
    @GetMapping("/{id}")
    @Operation(summary = "获得对应id的套餐参数")
    public Result<SetmealVO> get(@PathVariable Long id){
        log.info("获得对应id的套餐参数");
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }
    //修改对应的套餐
    @PutMapping
    @ApiOperation("修改套餐")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售停售")
    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }
}