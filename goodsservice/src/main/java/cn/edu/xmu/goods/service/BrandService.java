package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.BrandDao;
import cn.edu.xmu.goods.model.bo.Brand;
import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.goods.model.vo.BrandInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

    @Autowired
    private BrandDao brandDao;

    private static final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    public ReturnObject<PageInfo<VoObject>> findAllBrand(Integer page, Integer pageSize) {
        return brandDao.findAllBrand(page, pageSize);
    }

    /**
     * 管理员删除品牌
     * @param id
     * @return
     */
    public ReturnObject deleteBrandById(Long id) {
        return brandDao.deleteBrandById(id);
    }

    public ReturnObject modifyBrandInfo(Long id, BrandInputVo brandInputVo) {
        return brandDao.modifyBrandById(id,brandInputVo);
    }

    /**
     * 管理员新增品牌
     *
     * @param brandInputVo 品牌详细信息
     * @return 返回对象 ReturnObject<Object>
     */
    public ReturnObject<Object> addBrand(BrandInputVo brandInputVo) {
        ReturnObject returnObject;
        BrandPo brandPo=brandDao.addBrandById(brandInputVo);
        if (brandInputVo != null) {
            returnObject = new ReturnObject(new Brand(brandPo));
            logger.debug("addBrand : " + returnObject);
        } else {
            logger.debug("addBrand : fail!");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return returnObject;

    }
}
