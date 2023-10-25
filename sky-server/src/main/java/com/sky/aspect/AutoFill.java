package com.sky.aspect;

import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 〈一句话功能简述〉<br>
 * 〈切面类〉
 *
 * @author ely
 * @create 2023/10/25
 * @since 1.0.0
 */
@Aspect
@Component
//TODO 使用 aop + 自定义注解实现自动填充
public class AutoFill {
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    // 修饰符可以省略，所以第一个*表示返回值，第二个*表示类，第三个*表示方法，..表示任意参数
    public void pointCut() {}

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) { // JoinPoint 用于获取方法参数
        System.out.println("开始自动字段填充");
        //通过反射获取方法上的注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        OperationType op = signature.getMethod().getAnnotation(com.sky.annotation.AutoFill.class).value();
        //获得参数
        Object[] args = joinPoint.getArgs();
        if(args.length == 0 ) return ;
        //获取第一个参数
        Object entity = args[0];
        //获取要赋值的参数
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //通过反射进行赋值

        
        if (op == OperationType.INSERT) {
            try {
                Method setCreateTime = entity.getClass() // 获取类
                        .getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME , LocalDateTime.class);// 获取方法,参数为 LocalDateTime.class
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //反射来赋值
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);

            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } else if (op == OperationType.UPDATE) {

            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //反射来赋值

                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }
    }
}