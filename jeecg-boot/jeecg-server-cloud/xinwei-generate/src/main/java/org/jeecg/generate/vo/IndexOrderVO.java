package org.jeecg.generate.vo;

import lombok.Data;
import org.jeecg.generate.entity.FdqController;
import org.jeecg.generate.entity.FdqOrderStep;

import java.io.Serializable;
import java.util.List;

@Data
public class IndexOrderVO implements Serializable {

    private Integer step;

    private List<FdqOrderStep> stepList;

    private FdqController controller;

}
