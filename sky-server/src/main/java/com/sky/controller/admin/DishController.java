package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ely
 * @create 2023/10/26
 * @since 1.0.0
 */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理")
public class DishController {
    @Autowired
    DishService dishService;
    @PostMapping
    public Result<Object> save(@RequestBody DishDTO dishDTO) {
        dishService.saveWithFlavor(dishDTO);
        log.info("新增菜品");
        return  Result.success();
    }
    @GetMapping("/page")
    @Operation(summary = "分页查询菜品")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("分页查询菜品");
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }
    @DeleteMapping
    public Result delete(@RequestParam("ids") List<Long> ids){
        log.info("删除菜品");
        dishService.delete(ids);
        return Result.success();
    }
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品");
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }
    @PutMapping
    @Operation(summary = "修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品");
        dishService.updateWithFlavor(dishDTO);
        return Result.success();
    }
}