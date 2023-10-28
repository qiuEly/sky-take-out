package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈用户端〉
 *
 * @author ely
 * @create 2023/10/28
 * @since 1.0.0
 */
@Slf4j
@Api(tags = "用户端")
@RequestMapping("/user/user")
@RestController
public class UserController {
   @Autowired
   private UserService userService;
   @Autowired
   private JwtProperties jwtProperties;


   @PostMapping("/login")
   @Operation(summary = "用户登录")
   public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
       log.info("用户登录{}",userLoginDTO.getCode());
       //登录实现
       User user =userService.wxlogin(userLoginDTO);
       //生成jwt令牌
       Map<String, Object> claims =  new HashMap<>();
       claims.put(JwtClaimsConstant.USER_ID,user.getId());
       String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
       UserLoginVO userLoginVO = UserLoginVO.builder()
               .id(user.getId())
               .token(token)
               .openid(user.getOpenid())
               .build();
       return Result.success(userLoginVO);
   }

}