package com.laogeli.user.mapper;

import com.laogeli.common.core.persistence.CrudMapper;
import com.laogeli.common.core.vo.CompanyVo;
import com.laogeli.user.api.module.Company;
import com.laogeli.user.api.vo.MotorcadeVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.*;

/**
 * 公司mapper
 *
 * @author yangyu
 * @date 2020-06-08
 */
@Mapper
public interface CompanyMapper extends CrudMapper<Company> {

    /**
     * 查询车队
     * @return
     */
    List<MotorcadeVo> getMotorcade();

    List<CompanyVo> findAllCompany();
}
