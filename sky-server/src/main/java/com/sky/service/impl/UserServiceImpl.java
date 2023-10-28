package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author ely
 * @create 2023/10/28
 * @since 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
        //判断openid是否存在
        String openid = userLoginDTO.getCode();
        if (openid == null) {
            throw new RuntimeException(MessageConstant.LOGIN_FAILED);
        }
        //根据openid查询用户
        User user = userMapper.getByOpenid(openid);

        //新用户创建即可
        if (user == null) {
           user = User.builder()
                   .openid(openid)
                   .createTime(LocalDateTime.now())
                   .build();
           userMapper.insert(user);
        }
       return user;
    }

    private String getOpenid(String code) {
        //调用微信接口服务，获得openid
        Map<String ,String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN,map);
        //解析json
        JSONObject jsonObject = JSONObject.parseObject(json);
        return jsonObject.getString("openid");
    }
}