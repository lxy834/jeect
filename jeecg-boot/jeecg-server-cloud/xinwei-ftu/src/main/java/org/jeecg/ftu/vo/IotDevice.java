package org.jeecg.ftu.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class IotDevice implements Serializable {

    private static final long serialVersionUID = 1L;

    private String deviceName;

    private String deviceType = "direct";

    private Integer blgProductId = 3;

    private String deviceSn;

    private String ipAddr;

    private String online = "n";

    private Boolean onlineBool = Boolean.TRUE;

    private String allowAutomatic = "n";

    private Boolean allowAutomaticBool = Boolean.FALSE;

    private String blgProductName = "F411";

    private String categoryCode = "1";

}
