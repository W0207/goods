package cn.edu.xmu.flashsale.dao;

import com.sun.xml.bind.v2.TODO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import cn.edu.xmu.ininterface.service.model.vo.*;
import cn.edu.xmu.ininterface.service.*;
import org.apache.dubbo.config.annotation.DubboReference;
@Repository
public class FlashSaleDao {
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleDao.class);

    @Autowired
    FlashSalePoMapper flashSalePoMapper;

    @Autowired
    FlashSaleItemPoMapper flashSaleItemPoMapper;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice goodservice;

    /**
     * 查找某一时段秒杀活动商品
     * @param id
     * @return
     */
    public List<FlashSaleOutputVo>findFlashSaleItemByTime(Long id){
        FlashSalePoExample example=new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria=example.createCriteria();
        criteria.andTimeSegIdEqualTo(id);
        List<FlashSalePo> flashSalePos=flashSalePoMapper.selectByExample(example);
        List<FlashSaleOutputVo> flashSaleOutputVos=new ArrayList<>();
        for(FlashSalePo flashSalePo : flashSalePos){
            FlashSaleItemPoExample example1=new FlashSaleItemPoExample();
            FlashSaleItemPoExample.Criteria criteria1=example1.createCriteria();
            criteria1.andSaleIdEqualTo(flashSalePo.getId());
            List<FlashSaleItemPo> flashSaleItemPos=flashSaleItemPoMapper.selectByExample(example1);
            for(FlashSaleItemPo flashSaleItemPo : flashSaleItemPos){
                FlashSaleItem flashSaleItem=new FlashSaleItem(flashSaleItemPo);
                SkuToFlashSaleVo skuToFlashSaleVo=goodservice.flashFindSku(flashSaleItem.getGoodsSkuId());
                FlashSaleOutputVo flashSaleOutputVo=new FlashSaleOutputVo(flashSaleItem,skuToFlashSaleVo);
                flashSaleOutputVos.add(flashSaleOutputVo);
            }
        }
        return flashSaleOutputVos;
    }

    /**
     * 修改秒杀活动
     *
     * @param id
     * @param flashSaleInputVo
     * @return
     * @author zhai
     */
    public ReturnObject<Object> updateFlashSale(Long id, FlashSaleInputVo flashSaleInputVo) {

        FlashSalePo po = flashSalePoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        FlashSale flashSale = new FlashSale(po);
        FlashSalePo flashSalePo = flashSale.createUpdatePo(flashSaleInputVo);
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
     *
     * @param id
     * @return
     * @author zhai
     */
    public ReturnObject<Object> deleteFlashSale(Long id) {

        FlashSalePo po = flashSalePoMapper.selectByPrimaryKey(id);
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
     *
     * @param id
     * @param skuInputVo
     * @return
     */
    public FlashSaleItem addItem(Long id, SkuInputVo skuInputVo) {

        FlashSaleItem flashSaleItem = new FlashSaleItem(id, skuInputVo);
        FlashSaleItemPo po = flashSaleItemPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleId = " + id);
            return flashSaleItem;
        }

        FlashSaleItemPo flashSaleItemPo = flashSaleItem.createItemPo();
        int ret = flashSaleItemPoMapper.insertSelective(flashSaleItemPo);
        if (ret == 0) {
            logger.info("商品新增失败：FlashSaleId = " + id);
            flashSaleItem = null;
            return flashSaleItem;
        } else {
            logger.info("商品新增成功：FlashSaleId = " + id);
            return flashSaleItem;
        }

    }

    /**
     * 删除秒杀活动中sku
     *
     * @param fid
     * @param id
     * @return
     * @author zhai
     */
    public ReturnObject<Object> deleteSku(Long fid, Long id) {

        FlashSaleItemPo po = flashSaleItemPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("该商品不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        if (po.getSaleId() == null) {
            logger.info("无权限删除商品：GrouponId = " + id);
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if (po.getSaleId().equals(fid)) {

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

    /**
     * 新建秒杀活动
     *
     * @param id
     * @param flashSaleInputVo
     * @return
     */
    public ReturnObject createFlash(Long id, FlashSaleInputVo flashSaleInputVo) {
        TODO 查询时间段id存不存在和具体的信息;
        FlashSalePo flashSalePo = new FlashSalePo();
        flashSalePo.setFlashDate(flashSaleInputVo.getFlashDate());
        flashSalePo.setGmtCreate(LocalDateTime.now());
        flashSalePo.setTimeSegId(id);
        flashSalePo.setState((byte) 0);
        try {
            int ret = flashSalePoMapper.insertSelective(flashSalePo);
            if (ret == 0) {
                return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
            } else {
                FlashSaleRetVo flashSaleRetVo = new FlashSaleRetVo();
                flashSaleRetVo.setId(flashSalePo.getId());
                flashSaleRetVo.setGmtCreate(flashSalePo.getGmtCreate());
                flashSaleRetVo.setFlashData(flashSalePo.getFlashDate());
                return new ReturnObject(flashSaleRetVo);
            }
        } catch (Exception e) {
            logger.error("发生了严重的数据库错误 : " + e.getMessage());
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }
}
