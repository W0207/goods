package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.FloatPricePo;
import cn.edu.xmu.privilegeservice.client.IUserService;
import lombok.Data;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private IUserService iUserService;

    public FloatPriceRetVo(FloatPricePo floatPricePo) {
        this.id = floatPricePo.getId();
        this.activityPrice = floatPricePo.getActivityPrice();
        this.quantity = floatPricePo.getQuantity();
        this.beginTime = floatPricePo.getBeginTime();
        this.endTime = floatPricePo.getEndTime();
        SimpleAdminUser simpleAdminUser1 = new SimpleAdminUser();
        simpleAdminUser1.setId(floatPricePo.getCreatedBy());
        simpleAdminUser1.setUserName(iUserService.getUserName(floatPricePo.getCreatedBy()));
        this.createdBy = simpleAdminUser1;
        SimpleAdminUser simpleAdminUser2 = new SimpleAdminUser();
        simpleAdminUser2.setId(floatPricePo.getCreatedBy());
        simpleAdminUser2.setUserName(iUserService.getUserName(floatPricePo.getCreatedBy()));
        this.modifiedBy = simpleAdminUser2;

        this.valid = floatPricePo.getValid() == 1;
        this.gmtCreate = floatPricePo.getGmtCreate();
    }
}
