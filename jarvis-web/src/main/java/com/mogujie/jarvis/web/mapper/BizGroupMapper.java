package com.mogujie.jarvis.web.mapper;

import com.mogujie.jarvis.web.entity.qo.BizGroupQo;
import com.mogujie.jarvis.web.entity.vo.BizGroupVo;

import java.util.List;

/**
 * Created by hejian on 16/1/13.
 */
public interface BizGroupMapper {
    BizGroupVo getById(Integer id);                              //根据id获取业务组信息
    BizGroupVo getByName(String name);                           //根据name获取业务组信息
    List<BizGroupVo> getAllByCondition(BizGroupQo bizGroupQo);   //获取所有满足条件的业务组信息
    Integer getTotalByCondition(BizGroupQo bizGroupQo);          //获取满足条件的业务组数量
    List<BizGroupVo> getByCondition(BizGroupQo bizGroupQo);      //获取满足条件的业务组列表(分页)

    List<String> getAllName();                                   //获取所有业务名
    List<String> getAllOwner();                                  //获取所有维护人
}
