package com.sky.controller.admin;

import com.sky.result.Result;
import io.lettuce.core.cluster.pubsub.RedisClusterPubSubListener;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ely
 * @create 2023/10/27
 * @since 1.0.0
 */
@RestController("adminShopController")
@Slf4j
@Api(tags = "商城管理")
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate ;

    private static final String KEY = "SHOP_STATUS";
    @PutMapping("/{status}")
    @Operation (summary = "设置店铺的营业状态")
    public Result  setShopStatus(@PathVariable Integer status){
        log.info("设置店铺的营业状态为：{}",status == 1 ? "营业中" : "打烊中");
        redisTemplate.convertAndSend("shop",status);
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }
    @GetMapping("/status")
    @Operation (summary = "获取店铺的营业状态")
    public Result<Integer> getShopStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        return Result.success(status);
    }
}