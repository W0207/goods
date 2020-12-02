package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.BrandPoMapper;
import cn.edu.xmu.goods.model.bo.Brand;
import cn.edu.xmu.goods.model.po.BrandPo;
import cn.edu.xmu.goods.model.po.BrandPoExample;
import cn.edu.xmu.goods.model.vo.BrandInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BrandDao implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(BrandDao.class);

    @Autowired
    private BrandPoMapper brandPoMapper;

    /**
     * 查询所有品牌
     *
     * @param page:    页码
     * @param pageSize : 每页数量
     * @return 品牌列表
     */
    public ReturnObject<PageInfo<VoObject>> findAllBrand(Integer page, Integer pageSize) {
        BrandPoExample example = new BrandPoExample();
        BrandPoExample.Criteria criteria = example.createCriteria();
        PageHelper.startPage(page, pageSize);
        List<BrandPo> brandPos = null;
        try {
            brandPos = brandPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(brandPos.size());
            for (BrandPo po : brandPos) {
                Brand bran = new Brand(po);
                ret.add(bran);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            PageInfo<BrandPo> brandPoPage = PageInfo.of(brandPos);
            PageInfo<VoObject> brandPage = new PageInfo<>(ret);
            brandPage.setPages(brandPoPage.getPages());
            brandPage.setPageNum(brandPoPage.getPageNum());
            brandPage.setPageSize(brandPoPage.getPageSize());
            brandPage.setTotal(brandPoPage.getTotal());
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("findAllBrand: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject<Object> deleteBrandById(Long id) {
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
        if (brandPo == null) {
            logger.info("品牌不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Brand brand = new Brand(brandPo);
        BrandPo po = brand.deleteUpdateStatePo();
        ReturnObject<Object> returnObject;
        int ret = brandPoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("品牌不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * 修改品牌信息
     *
     * @param id
     * @param brandInputVo
     * @return
     */
    public ReturnObject modifyBrandById(Long id, BrandInputVo brandInputVo) {
        BrandPo brandPo = brandPoMapper.selectByPrimaryKey(id);
        if (brandPo == null) {
            logger.info("brandId = " + id + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Brand brand = new Brand(brandPo);
        BrandPo po = brand.createUpdatePo(brandInputVo);

        ReturnObject<Object> returnObject;
        int ret = brandPoMapper.updateByPrimaryKeySelective(po);
        // 检查更新有否成功
        if (ret == 0) {
            logger.info("brandId = " + id + " 不存在");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("brandId = " + id + " 的信息已更新");
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
