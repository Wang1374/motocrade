package com.laogeli.order.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.order.api.module.TableHeader;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TableHeaderMapper extends CrudMapper<TableHeader> {

    //查询数据表字段
    List<TableHeader> findTableHeader(@Param("tableId") String tableId,
                                      @Param("userId") String userId);

    //根据tableId删除数据
    int deleteByTableId(@Param("tableId") String tableId,
                        @Param("userId") String userId);

    //新增表头数据
    int insertListTableHeader(List<TableHeader> tableHeaders);

    List<TableHeader> findHeaderDefault(String tableId);
}
