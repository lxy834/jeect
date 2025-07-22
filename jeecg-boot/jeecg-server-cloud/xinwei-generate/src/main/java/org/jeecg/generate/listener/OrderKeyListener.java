package org.jeecg.generate.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.generate.entity.FdqOrder;
import org.jeecg.generate.entity.FdqOrderStep;
import org.jeecg.generate.service.IFdqOrderService;
import org.jeecg.generate.service.IFdqOrderStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class OrderKeyListener extends KeyExpirationEventMessageListener {

    @Autowired
    private IFdqOrderService orderService;
    @Autowired
    private IFdqOrderStepService stepService;

    public OrderKeyListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expireKey = message.toString();
        if (expireKey.contains("timeout_order_")) {
            // 工单过期
            log.info("检测到工单{}过期,开始修改状态", expireKey.substring(6));
            FdqOrder order = orderService.getOne(Wrappers.<FdqOrder>query().lambda()
                    .eq(FdqOrder::getPlateNumber, expireKey.substring(6))
                    .orderByDesc(FdqOrder::getCreateTime)
                    .last("limit 1"));
            order.setOrderStatus("已结束").setEndTime(new Date());
            orderService.updateById(order);
            List<FdqOrderStep> steps = stepService.list(Wrappers.<FdqOrderStep>query().lambda()
                    .eq(FdqOrderStep::getOrderId, order.getId()));
            steps.forEach(v -> {
                switch (v.getStep()) {
                    case 0, 1, 2:
                        v.setStatus("success");
                        break;
                    case 3:
                        v.setStatus("success");
                        v.setStepTime(new Date());
                        v.setStepInfo("工单结束");
                        break;
                }
            });
            stepService.saveBatch(steps);
        }
    }

}
