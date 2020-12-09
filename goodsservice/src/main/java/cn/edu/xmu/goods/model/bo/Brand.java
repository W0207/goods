package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.goods.model.vo.BrandInputVo;
import cn.edu.xmu.goods.model.vo.BrandRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Brand implements VoObject {

    private Long id;

    private String name;

    private String imageUrl;

    private String detail;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;

    /**
     * 由po创建vo
     *
     * @param po
     */
    public Brand(BrandPo po) {
        this.id = po.getId();
        this.name = po.getName();
        this.imageUrl = po.getImageUrl();
        this.detail = po.getDetail();
        this.gmtCreate = po.getGmtCreate();
        this.gmtModified = po.getGmtModified();
    }

    public Brand() {

    }

    @Override
    public Object createVo() {
        return new BrandRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDetail() {
        return detail;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BrandPo createUpdatePo(BrandInputVo brandInputVo) {
        String nameEnc = brandInputVo.getName() == null ? null : brandInputVo.getName();
        String detailEnc = brandInputVo.getDetail() == null ? null : brandInputVo.getDetail();
        BrandPo brandPo = new BrandPo();
        brandPo.setId(id);
        brandPo.setName(nameEnc);
        brandPo.setDetail(detailEnc);
        brandPo.setGmtModified(LocalDateTime.now());
        return brandPo;
    }

    /**
     * 创建addPo函数
     *
     * @param brandVo 品牌信息
     */
    public BrandPo createAddPo(BrandInputVo brandVo) {
        BrandPo brandPo = new BrandPo();
        brandPo.setGmtCreate(LocalDateTime.now());
        brandPo.setName(brandVo.getName());
        brandPo.setDetail(brandVo.getDetail());
        brandPo.setGmtModified(LocalDateTime.now());
        return brandPo;
    }
}
