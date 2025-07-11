package org.jeecg.ftu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.ftu.entity.FtuWarnInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 告警记录
 * @Author: jeecg-boot
 * @Date:   2025-06-30
 * @Version: V1.0
 */
public interface IFtuWarnInfoService extends IService<FtuWarnInfo> {

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<FtuWarnInfo>
     */
    public List<FtuWarnInfo> selectByMainId(String mainId);


    Map<String, Object> getStatById(String ftuId);

    IPage<FtuWarnInfo> getEventById(String ftuId,String date,String event, Integer pageNo, Integer pageSize);

    Map<String,Object> eventStat();

}
