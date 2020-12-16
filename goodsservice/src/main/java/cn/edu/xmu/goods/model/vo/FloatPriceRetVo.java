package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.FloatPricePo;
import cn.edu.xmu.privilegeservice.client.IUserService;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Abin
 */
@Data
public class FloatPriceRetVo {
    private Long id;
    private Long activityPrice;
    private Integer quantity;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private SimpleAdminUser createdBy;
    private SimpleAdminUser modifiedBy;
    private Boolean valid;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    IUserService iUserService;

    public FloatPriceRetVo(FloatPricePo floatPricePo) {
        this.id = floatPricePo.getId();
        this.activityPrice = floatPricePo.getActivityPrice();
        this.quantity = floatPricePo.getQuantity();
        this.beginTime = floatPricePo.getBeginTime();
        this.endTime = floatPricePo.getEndTime();
        SimpleAdminUser simpleAdminUser = new SimpleAdminUser();
        simpleAdminUser.setId(floatPricePo.getCreatedBy());
        simpleAdminUser.setUserName(iUserService.getUserName(floatPricePo.getCreatedBy()));
        this.createdBy = simpleAdminUser;
        this.valid = floatPricePo.getValid() == 1;
        this.gmtCreate = floatPricePo.getGmtCreate();
    }
}
