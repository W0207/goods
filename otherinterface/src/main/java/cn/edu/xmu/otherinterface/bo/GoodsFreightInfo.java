package cn.edu.xmu.otherinterface.bo;


import java.io.Serializable;

/**
 * @author ASUS
 */
public class GoodsFreightInfo implements Serializable {

    private Long skuId;
    private Long freightId;

    public GoodsFreightInfo(Long skuId, Long freightId) {
        this.skuId = skuId;
        this.freightId = freightId;
    }
}
