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


    Map<String, Object> getStatById(@RequestParam(name = "ftuId") String ftuId);

    IPage<FtuWarnInfo> getEventById(@RequestParam(name = "ftuId") String ftuId,
                                     @RequestParam(name = "Date") String date,
                                    String event,
                                     @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize);

}
