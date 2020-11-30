package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.encript.SHA256;
import lombok.Data;

import javax.swing.plaf.nimbus.State;
import java.time.LocalDateTime;

@Data
public class Brand implements VoObject {

    private Long id;

    private String name;

    private String imageUrl;

    private String detail;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    public Brand(BrandPo po) {
        this.id=po.getId();
        this.name=po.getName();
        this.imageUrl=po.getImageUrl();
        this.detail=po.getDetail();
        this.gmtCreate=po.getGmtCreate();
        this.gmtModified=po.getGmtModified();


    }


    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}