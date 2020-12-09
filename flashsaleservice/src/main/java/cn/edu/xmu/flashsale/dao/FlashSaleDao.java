package cn.edu.xmu.flashsale.dao;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;
import cn.edu.xmu.flashsale.mapper.*;
import cn.edu.xmu.flashsale.model.bo.*;
import cn.edu.xmu.flashsale.model.po.*;
import cn.edu.xmu.flashsale.model.vo.*;
import cn.edu.xmu.flashsale.mapper.FlashSalePoMapper;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FlashSaleDao {
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleDao.class);

    @Autowired
    FlashSalePoMapper flashSalePoMapper;

    @Autowired
    FlashSaleItemPoMapper flashSaleItemPoMapper;



    /**
     * 修改秒杀活动
     * @param id
     * @param flashSaleInputVo
     * @return
     * @author zhai
     */
    public ReturnObject<Object> updateFlashSale(Long id,FlashSaleInputVo flashSaleInputVo) {

        FlashSalePo po=flashSalePoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        FlashSale flashSale=new FlashSale(po);
        FlashSalePo flashSalePo=flashSale.createUpdatePo(flashSaleInputVo);
        int ret = flashSalePoMapper.updateByPrimaryKeySelective(flashSalePo);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            logger.info("秒杀活动修改失败：FlashSaleId = " + id);
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            logger.info("秒杀活动修改成功：FlashSaleItemId = " + id);
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }
    /**
     * 修改秒杀活动状态
     * @param id
     * @return
     * @author zhai
     */
    public ReturnObject<Object> deleteFlashSale(Long id) {

      FlashSalePo po=flashSalePoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
            int ret = flashSalePoMapper.deleteByPrimaryKey(id);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("秒杀活动删除失败：FlashSaleId = " + id);
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("秒杀活动删除成功：FlashSaleItemId = " + id);
                returnObject = new ReturnObject<>();
            }
            return returnObject;
    }

    /**
     * 增加秒杀活动商品
     * @param id
     * @param skuInputVo
     * @return
     */
    public FlashSaleItem addItem(Long id,SkuInputVo skuInputVo) {

        FlashSaleItem flashSaleItem=new FlashSaleItem(id,skuInputVo);
        FlashSaleItemPo po = flashSaleItemPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleId = " + id);
            return flashSaleItem;
        }

       FlashSaleItemPo flashSaleItemPo=flashSaleItem.createItemPo();
       int ret = flashSaleItemPoMapper.insertSelective(flashSaleItemPo);
       if (ret == 0) {
           logger.info("商品新增失败：FlashSaleId = " + id);
           flashSaleItem=null;
           return flashSaleItem;
       } else {
                logger.info("商品删除成功：FlashSaleItemId = " + id);
           return flashSaleItem;
            }

    }

    /**
     * 删除秒杀活动中sku
     * @param fid
     * @param id
     * @return
     * @author zhai
     */
    public ReturnObject<Object> deleteSku(Long fid,Long id) {

        FlashSaleItemPo po = flashSaleItemPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("该商品不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        if(po.getSaleId()==null){
            logger.info("无权限删除商品：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if( po.getSaleId().equals(fid) ){

            int ret = flashSaleItemPoMapper.deleteByPrimaryKey(id);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("商品删除失败：FlashSaleItemId = " + id);
                returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("商品删除成功：FlashSaleItemId = " + id);
                returnObject = new ReturnObject<>();
            }
            return returnObject;
        }
        logger.info("无权限修改团购活动：GrouponId = " + id);
        return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
    }
}
