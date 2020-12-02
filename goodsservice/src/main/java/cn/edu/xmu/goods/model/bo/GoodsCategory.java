package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.GoodsCategoryPo;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import cn.edu.xmu.goods.model.vo.CategoryInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
public class GoodsCategory implements VoObject{

    private Long id;

    private Long pid;

    private String name;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;
    public GoodsCategory(){}
    /**
     * 构造函数
     *
     * @param po Po对象
     */
    public  GoodsCategory(GoodsCategoryPo po)
    {
        this.id=po.getId();
        this.pid=po.getPid();
        this.name=po.getName();
        this.gmtCreated=po.getGmtCreated();
        this.gmtModified=po.getGmtModified();

    }
    /**
     * 创建updatePo函数
     *
     * @param categoryInputVo 商品类目信息
     */
    public GoodsCategoryPo createUpdatePo(CategoryInputVo categoryInputVo){
        GoodsCategoryPo goodsCategoryPo=new GoodsCategoryPo();
        String nameModify=categoryInputVo.getName() == null ? null : categoryInputVo.getName();
        goodsCategoryPo.setGmtModified(LocalDateTime.now());
        goodsCategoryPo.setId(id);
        goodsCategoryPo.setGmtCreated(gmtCreated);
        goodsCategoryPo.setName(nameModify);
        goodsCategoryPo.setPid(pid);
        return goodsCategoryPo;
    }

    /**
     * 创建addPo函数
     *
     * @param categoryInputVo 商品类目信息
     */
    public GoodsCategoryPo createAddPo(Long id,CategoryInputVo categoryInputVo){
        GoodsCategoryPo goodsCategoryPo=new GoodsCategoryPo();
        String nameModify=categoryInputVo.getName() == null ? null : categoryInputVo.getName();
        goodsCategoryPo.setGmtModified(LocalDateTime.now());
        goodsCategoryPo.setId(id);
        goodsCategoryPo.setGmtCreated(LocalDateTime.now());
        goodsCategoryPo.setName(nameModify);
        goodsCategoryPo.setPid(pid);
        return goodsCategoryPo;
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
