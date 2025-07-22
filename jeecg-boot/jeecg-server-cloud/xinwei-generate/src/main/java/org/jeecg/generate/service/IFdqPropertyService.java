package org.jeecg.generate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.generate.entity.FdqBasic;
import org.jeecg.generate.entity.FdqController;
import org.jeecg.generate.entity.FdqProperty;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 应急装备
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
public interface IFdqPropertyService extends IService<FdqProperty> {

    /**
     * 添加一对多
     *
     * @param fdqProperty
     * @param fdqBasicList
     * @param fdqControllerList
     */
    public void saveMain(FdqProperty fdqProperty, List<FdqBasic> fdqBasicList, List<FdqController> fdqControllerList);

    /**
     * 修改一对多
     *
     * @param fdqProperty
     * @param fdqBasicList
     * @param fdqControllerList
     */
    public void updateMain(FdqProperty fdqProperty, List<FdqBasic> fdqBasicList, List<FdqController> fdqControllerList);

    /**
     * 删除一对多
     *
     * @param id
     */
    public void delMain(String id);

    /**
     * 批量删除一对多
     *
     * @param idList
     */
    public void delBatchMain(Collection<? extends Serializable> idList);

    Map<String, BigDecimal> queryPlateList(@RequestParam("plate") String plate);

}
