package com.yin.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yin.common.Result;
import com.yin.config.error.ExceptionEnum;
import com.yin.pojo.PagePojo;
import org.springframework.stereotype.Component;
import java.io.Serializable;
import java.util.List;

/**
 * @author yin
 * @date 2024-12-12
 * @Description serviceImpl 层帮助类
 */
@Component
public class ServiceUtils<T extends Serializable> {
    /**
     * 数据新增
     *
     * @param baseMapper mybatis-plus数据库操作对象
     * @param o          传入的实体类数据
     * @param message    参数判断
     * @return 返回
     */
    public Result<String> insert(BaseMapper<T> baseMapper,T o, String message) {
        return getStringResultInsert((BaseMapper<T>) baseMapper, o, message);
    }

    /**
     * 数据新增, 条件参数判断，禁止重复提交新增
     *
     * @param baseMapper   mybatis-plus数据库操作对象
     * @param o            传入的实体类数据
     * @param message      参数判断
     * @param queryWrapper 条件参数
     * @return 返回
     */
    public Result<String> insert(BaseMapper<T> baseMapper, T o, String message, LambdaQueryWrapper<T> queryWrapper) {
        List<T> dataS = baseMapper.selectList(queryWrapper);
        if (!dataS.isEmpty()) {
            return Result.error(ExceptionEnum.CODE_A_EXISTS, "");
        }
        return getStringResultInsert(baseMapper,o, message);
    }

    private Result<String> getStringResultInsert(BaseMapper<T> baseMapper, T data,String message) {
        if (message.equals(ExceptionEnum.CODE_CZ.getResultCode())) {
            int insert = baseMapper.insert(data);
            if (insert >= 1) {
                return Result.okInsert("");
            } else {
                return Result.errInsert("");
            }
        }
        return Result.error(message, "");
    }

    /**
     * 更新
     *
     * @param baseMapper mybatis-plus数据库操作对象
     * @param data       传入的实体类数据
     * @param message    参数判断
     * @return 返回
     */
    public Result<T> update(BaseMapper<T> baseMapper, T data, String message) {
        if (message.equals(ExceptionEnum.CODE_CZ.getResultCode())) {
            int i = baseMapper.updateById(data);
            if (i > 0) {
                return Result.okUpdate(data);
            } else {
                return Result.errUpdate(data);
            }
        }
        return Result.error(message, data);
    }

    /**
     * 根据id删除
     *
     * @param baseMapper mybatis-plus数据库操作对象
     * @param data       传入的实体类数据
     * @param message    参数判断
     * @return 返回
     */
    public Result<T> delete(BaseMapper<T> baseMapper, T data, String message) {
        if (message.equals(ExceptionEnum.CODE_CZ.getResultCode())) {
            int i = baseMapper.deleteById(data);
            if (i > 0) {
                return Result.okDelete(data);
            } else {
                return Result.errDelete(data);
            }
        }
        return Result.error(message, data);
    }


    /**
     *  查询
     * @param baseMapper mybatis-plus数据库操作对象
     * @param queryWrapper 条件
     * @return 返回
     */
    public Result<List<T>> selectList(BaseMapper<T> baseMapper,LambdaQueryWrapper<T> queryWrapper) {
        List<T> ts = baseMapper.selectList(queryWrapper);
        return Result.okSel(ts);
    }
    /**
     *  分页查询
     * @param baseMapper mybatis-plus数据库操作对象
     * @param queryWrapper 条件
     * @return 返回
     */
    public Result<Page<T>> selectPage(BaseMapper<T> baseMapper, Class<T> tClass, PagePojo pagePojo, LambdaQueryWrapper<T> queryWrapper) {
        //转换为page类
        Page<T> dictPaidItemDictionaryPojoPage = PageUtils.convertPage(pagePojo, tClass);
        if(dictPaidItemDictionaryPojoPage==null){
            return Result.error(ExceptionEnum.CODE_VERIFY_PAGE,null);
        }
        //分页查询
        Page<T> pojoPage = baseMapper.selectPage(dictPaidItemDictionaryPojoPage, queryWrapper);
        return Result.okSel(pojoPage);
    }
}
