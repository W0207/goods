package cn.edu.xmu.otherinterface.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author issyu 30320182200070
 * @date 2020/12/14 10:56
 */
@Data
public class GoodInfo implements Serializable {
    private Long freightModelId;
    private Long weight;
    private Long shopId;

    public GoodInfo(Long freightModelId, Long weight, Long shopId) {
        this.freightModelId = freightModelId;
        this.weight = weight;
        this.shopId = shopId;
    }
}
