package cn.edu.xmu.ininterface.service.model.vo;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Abin
 */
@Data
@AllArgsConstructor
public class SkuToPresaleVo implements Serializable {

    private Long id;

    private String name;

    private String skuSn;

    private String imageUrl;

    private Integer inventory;

    private Long originalPrice;

    private Long price;

    private Boolean disable = false;

}
