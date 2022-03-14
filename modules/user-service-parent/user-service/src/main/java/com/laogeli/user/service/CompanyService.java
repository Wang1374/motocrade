package com.laogeli.user.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.user.api.module.Company;
import com.laogeli.user.mapper.CompanyMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公司service
 *
 * @author wang
 * @date 2020-06-08
 */
@Slf4j
@AllArgsConstructor
@Service
public class CompanyService extends CrudService<CompanyMapper, Company> {

    /**
     * 添加公司
     *
     * @param company company
     * @return int
     */
    @Transactional
    public int insertCompany(Company company) {
        Integer num = 0;
        Company companyName = new Company();
        companyName.setCompanyName(company.getCompanyName());
        Company obj = dao.get(companyName);
        if (obj == null) {
            num = dao.insert(company);
        }
        return num;
    }
}
