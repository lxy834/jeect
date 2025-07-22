package org.jeecg.generate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.generate.dto.FdqOrderDTO;
import org.jeecg.generate.entity.FdqOrder;
import org.jeecg.generate.entity.FdqOrderStep;
import org.jeecg.generate.vo.IndexOrderVO;
import org.jeecg.generate.vo.IndexStatVO;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 工单台账
 * @Author: jeecg-boot
 * @Date: 2025-07-21
 * @Version: V1.0
 */
public interface IFdqOrderService extends IService<FdqOrder> {

    /**
     * 添加一对多
     *
     * @param fdqOrder
     * @param fdqOrderStepList
     */
    public void saveMain(FdqOrder fdqOrder, List<FdqOrderStep> fdqOrderStepList);

    /**
     * 修改一对多
     *
     * @param fdqOrder
     * @param fdqOrderStepList
     */
    public void updateMain(FdqOrder fdqOrder, List<FdqOrderStep> fdqOrderStepList);

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

    IndexOrderVO indexOrder(@RequestBody FdqOrderDTO fdqOrderPage);

    IndexStatVO statVOResult();

}
