package com.laogeli.order.service;

import com.laogeli.common.core.service.CrudService;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.Clientele;
import com.laogeli.order.api.module.Driver;
import com.laogeli.order.api.vo.DriverVo;
import com.laogeli.order.mapper.ClienteleMapper;
import com.laogeli.order.mapper.DriverMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 司机信息service
 *
 * @author
 * @Date 2020-07-01
 **/
@Slf4j
@AllArgsConstructor
@Service
public class DriverService extends CrudService<DriverMapper, Driver> {

    private final DriverMapper driverMapper;

    private final ClienteleService clienteleService;

    /**
     * 通过企业标识id 获取司机与联系方式
     *
     * @param corporateIdentify
     * @return List<DriverVo>
     */
    public List<DriverVo> getListById(String corporateIdentify) {
        return dao.getListById(corporateIdentify);
    }

    /**
     * 新增司机
     * 1,判断司机姓名和手机号是否存在
     * 2,新增司机
     *
     * @param driver
     * @return
     */
    public Integer insertDriver(Driver driver) {
        Integer num = 0;
        Driver obj = dao.getByNameAndPhone(driver);
        if (obj == null) {
            //执行新增司机
            num = driverMapper.insert(driver);//
            //添加司机+姓名至往来单位
            String companyName = driver.getDriverName() + driver.getDriverPhone();
            Clientele clientele = new Clientele();
            clientele.setCompanyName(companyName);
            clientele.setBelongCompaniesId(driver.getBelongCompaniesId());
            clientele.setNature(3);
            clientele.setType(1);
            clientele.setPartner(2);
            clientele.setHowToAccount(1);
            clientele.setClearingForm(1);
            clientele.setSettlementInterval(1);
            clientele.setPaymentDays(30);
            clientele.setSalesman(driver.getCompanyName());
            clientele.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode());
            //执行添加往来单位
            clienteleService.insertClientele(clientele);

        }
        return num;
    }

    /**
     * 更新司机
     *
     * @param driver
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer updateDriver(Driver driver) {
        Driver result = driverMapper.getByNameAndPhone(driver);
        if (result == null) {
            //执行修改
            driverMapper.update(driver);
            //修改往来单位中的数据
            //  根据司机姓名查找往来单位中的数据
            String query = driver.getRawDriverName()+driver.getRawDriverPhone();
            String updateData = driver.getDriverName()+driver.getDriverPhone();
            clienteleService.updateClientByName(query,updateData);

            return 1;
        } else {
            if (result.getId().equals(driver.getId())) {
                //执行修改
                driverMapper.update(driver);
                //修改往来单位中的数据
                // 根据司机姓名查找往来单位中的数据
                String query = driver.getRawDriverName()+driver.getDriverPhone();
                String updateData = driver.getDriverName()+driver.getDriverPhone();
                clienteleService.updateClientByName(query,updateData);

                return 1;
            } else {
                return 0;
            }
        }
    }

}
