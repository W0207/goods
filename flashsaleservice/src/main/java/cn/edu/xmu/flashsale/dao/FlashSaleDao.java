package cn.edu.xmu.flashsale.dao;

import cn.edu.xmu.ooad.model.VoObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.xml.bind.v2.TODO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import cn.edu.xmu.flashsale.mapper.*;
import cn.edu.xmu.flashsale.model.bo.*;
import cn.edu.xmu.flashsale.model.po.*;
import cn.edu.xmu.flashsale.model.vo.*;
import cn.edu.xmu.flashsale.mapper.FlashSalePoMapper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import cn.edu.xmu.external.model.MyReturn;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import cn.edu.xmu.external.service.ITimeService;
import cn.edu.xmu.external.bo.TimeSegInfo;
import cn.edu.xmu.ininterface.service.model.vo.*;
import cn.edu.xmu.ininterface.service.*;
import org.apache.dubbo.config.annotation.DubboReference;

/**
 * @author zhai
 */
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

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private ITimeService timeService;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice ITimeService;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;


    public  ReturnObject<PageInfo<VoObject>> findFlashSale(Long id,Integer page,Integer PageSize){
        FlashSalePoExample example=new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria=example.createCriteria();
        criteria.andTimeSegIdEqualTo(id);
        List<FlashSalePo>flashSalePos=flashSalePoMapper.selectByExample(example);
        List<VoObject>flashSaleItems =new ArrayList<>();
        List<FlashSaleItemPo> pos=new ArrayList<>();
        for(FlashSalePo po:flashSalePos){
            FlashSaleItemPoExample example1=new FlashSaleItemPoExample();
            FlashSaleItemPoExample.Criteria criteria1=example1.createCriteria();
            criteria1.andSaleIdEqualTo(po.getId());
            List<FlashSaleItemPo> flashSaleItemPos=flashSaleItemPoMapper.selectByExample(example1);
            pos=flashSaleItemPos;
            for(FlashSaleItemPo po1:flashSaleItemPos){
                SkuToFlashSaleVo skuToFlashSaleVo = goodservice.flashFindSku(po1.getGoodsSkuId());
                FlashSaleItem flashSaleItem=new FlashSaleItem(po1,skuToFlashSaleVo);
                flashSaleItems.add(flashSaleItem);
            }

        }
        PageHelper.startPage(page,PageSize);
        PageInfo<VoObject> flashPage=PageInfo.of(flashSaleItems);
        flashPage.setPages((PageInfo.of(pos).getPages()));
        flashPage.setPages(page);
        flashPage.setPageSize(PageSize);
        flashPage.setTotal((PageInfo.of(pos).getTotal()));
        return new ReturnObject<>(flashPage) ;

    }

    public  List findCurrentFlashSale(Long id,Integer page,Integer PageSize){


        FlashSalePoExample example1=new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria1=example1.createCriteria();
        criteria1.andTimeSegIdEqualTo(id);
        List<FlashSalePo> flashSalePos=flashSalePoMapper.selectByExample(example1);
        List<VoObject> flash=new ArrayList<>();
        for(FlashSalePo po:flashSalePos){
            FlashSaleItemPoExample example2=new FlashSaleItemPoExample();
            FlashSaleItemPoExample.Criteria criteria2=example2.createCriteria();
            criteria2.andSaleIdEqualTo(po.getId());
            List<FlashSaleItemPo> flashSaleItemPos=flashSaleItemPoMapper.selectByExample(example2);
            flash=new ArrayList<>(flashSaleItemPos.size());
            for(FlashSaleItemPo po1:flashSaleItemPos){
                SkuToFlashSaleVo skuToFlashSaleVo = goodservice.flashFindSku(po1.getGoodsSkuId());
                FlashSaleItem flashSaleItem=new FlashSaleItem(po1,skuToFlashSaleVo);
                flash.add(flashSaleItem);
            }

        }
        return flash;
//        FlashSaleItemPoExample example=new FlashSaleItemPoExample();
//        FlashSaleItemPoExample.Criteria criteria=example.createCriteria();
//        criteria.andSaleIdEqualTo(Long.valueOf(4));
//        List<FlashSaleItemPo> flashSaleItemPos=flashSaleItemPoMapper.selectByExample(example);
//        List<VoObject> flashSaleItems=new ArrayList<>(flashSaleItemPos.size());
//        for(FlashSaleItemPo po:flashSaleItemPos){
//            SkuToFlashSaleVo skuToFlashSaleVo = goodservice.flashFindSku(po.getGoodsSkuId());
//            FlashSaleItem flashSaleItem=new FlashSaleItem(po,skuToFlashSaleVo);
//            flashSaleItems.add(flashSaleItem);
//            }
//        PageHelper.startPage(page,PageSize);
//        PageInfo<VoObject> flashPage=PageInfo.of(flashSaleItems);
//        flashPage.setPages((PageInfo.of(flashSaleItemPos).getPages()));
//        flashPage.setPages(page);
//        flashPage.setPageSize(PageSize);
//        flashPage.setTotal((PageInfo.of(flashSaleItemPos).getTotal()));
//        return flashSaleItems ;

    }
    /**
     * 修改秒杀活动
     *
     * @param id
     * @param flashSaleInputVo
     * @return
     * @author zhai
     */
    public ReturnObject updateFlashSale(Long id, FlashSaleInputVo flashSaleInputVo) {
        FlashSalePo po = flashSalePoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null||po.getState()==2) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(po.getFlashDate().isBefore(LocalDateTime.now())){
            return new ReturnObject(ResponseCode.ACTIVITYALTER_INVALID);
        }
        if(flashSaleInputVo.getFlashDate().isBefore(LocalDateTime.now())){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        if (po.getState() == 1) {
            logger.info("秒杀活动已上线，无法修改 ");
            return new ReturnObject(ResponseCode.DELETE_ONLINE_NOTALLOW);
        }
        if (flashSaleInputVo.getFlashDate() == null) {
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, "秒杀日期不能为空");
        }
        FlashSale flashSale = new FlashSale(po);
        FlashSalePo flashSalePo = flashSale.createUpdatePo(flashSaleInputVo);
        int ret = flashSalePoMapper.updateByPrimaryKeySelective(flashSalePo);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            logger.info("秒杀活动修改失败：FlashSaleId = " + id);
            returnObject = new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        } else {
            logger.info("秒杀活动修改成功：FlashSaleItemId = " + id);
            returnObject = new ReturnObject(ResponseCode.OK);
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
    public ReturnObject deleteFlashSale(Long id) {
        FlashSalePo po = flashSalePoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null||po.getState()==2) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (po.getState() == 1) {
            logger.info("秒杀活动已上线，无法删除 ");
            return new ReturnObject(ResponseCode.ACTIVITYALTER_INVALID);
        }
        po.setState(Byte.valueOf("2"));
        int ret = flashSalePoMapper.updateByPrimaryKeySelective(po);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            logger.info("秒杀活动删除失败：FlashSaleId = " + id);
            returnObject = new ReturnObject(ResponseCode.ACTIVITYALTER_INVALID);
        } else {
            logger.info("秒杀活动删除成功：FlashSaleItemId = " + id);
            String key = "cp_" + po.getTimeSegId();
            if (redisTemplate.opsForSet().pop(key) != null) {
                redisTemplate.opsForSet().pop(key);
            }
            returnObject = new ReturnObject();
        }
        return returnObject;
    }

    /**
     * 上线秒杀活动
     *
     * @param id
     * @return
     */
    public ReturnObject<Object> onshelvesFlashSale(Long id) {

        FlashSalePo po = flashSalePoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (po.getState() == 1) {
            logger.info("秒杀活动已上线，无法重复上线 ");
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        } else if (po.getState() == 2) {
            logger.info("秒杀活动已删除");
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        po.setState(Byte.valueOf("1"));
        int ret = flashSalePoMapper.updateByPrimaryKeySelective(po);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            logger.info("秒杀活动上线失败：FlashSaleId = " + id);
            returnObject = new ReturnObject(ResponseCode.ACTIVITYALTER_INVALID);
        } else {
            logger.info("秒杀活动上线成功：FlashSaleItemId = " + id);
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * 下线秒杀活动
     *
     * @param id
     * @return
     */
    public ReturnObject<Object> offshelvesFlashSale(Long id) {

        FlashSalePo po = flashSalePoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (po.getState() == 0) {
            logger.info("秒杀活动已下线，无法重复上线 ");
            return new ReturnObject(ResponseCode.AUTH_NOT_ALLOW);
        } else if (po.getState() == 2) {
            logger.info("秒杀活动已删除");
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        po.setState(Byte.valueOf("0"));
        int ret = flashSalePoMapper.updateByPrimaryKeySelective(po);
        ReturnObject<Object> returnObject;
        if (ret == 0) {
            logger.info("秒杀活动下线失败：FlashSaleId = " + id);
            returnObject = new ReturnObject(ResponseCode.ACTIVITYALTER_INVALID);
        } else {
            logger.info("秒杀活动下线成功：FlashSaleItemId = " + id);
            returnObject = new ReturnObject(ResponseCode.OK);
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
    public ReturnObject addItem(Long id, SkuInputVo skuInputVo) {
        ReturnObject returnObject=null;
        FlashSaleItem flashSaleItem = new FlashSaleItem(id, skuInputVo);
        FlashSalePo po = flashSalePoMapper.selectByPrimaryKey(id);
        if (po == null || po.getState() == null||po.getState()==2) {
            logger.info("秒杀活动不存在或已被删除：FlashSaleId = " + id);
            return null;
        }
        String key = "cp_" + po.getTimeSegId();
        FlashSaleItemPo flashSaleItemPo = flashSaleItem.createItemPo();
        int ret = flashSaleItemPoMapper.insertSelective(flashSaleItemPo);
        if (ret == 0) {
            logger.info("商品新增失败：FlashSaleId = " + id);
        } else {
            SkuToFlashSaleVo skuToFlashSaleVo = goodservice.flashFindSku(skuInputVo.getSkuId());
            if (skuToFlashSaleVo == null) {
                logger.info("该商品不存在");
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                FlashSaleItem flashSaleItem1=new FlashSaleItem(flashSaleItemPo);
                FlashSaleOutputVo flashSaleOutputVo = new FlashSaleOutputVo(flashSaleItem1, skuToFlashSaleVo);
                returnObject= new ReturnObject(flashSaleOutputVo);

            }
            FlashSaleItem item = new FlashSaleItem(flashSaleItemPo, skuToFlashSaleVo);
            redisTemplate.opsForSet().add(key, item);
            logger.info("商品新增成功：FlashSaleId = " + id);

        }
        return returnObject;
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

        FlashSalePo flashSalePo = flashSalePoMapper.selectByPrimaryKey(fid);
        if (flashSalePo == null || flashSalePo.getState() == null||flashSalePo.getState()==2) {
            logger.info("该秒杀活动不存在或已被删除：FlashSaleId = " + fid);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        FlashSaleItemPo po = flashSaleItemPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            logger.info("该商品不存在或已被删除：FlashSaleItemId = " + id);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        if (po.getSaleId() == null) {
            logger.info("无权限删除商品：GrouponId = " + id);
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        if (po.getSaleId().equals(fid)) {

            int ret = flashSaleItemPoMapper.deleteByPrimaryKey(id);
            ReturnObject<Object> returnObject;
            if (ret == 0) {
                logger.info("商品删除失败：FlashSaleItemId = " + id);
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            } else {
                logger.info("商品删除成功：FlashSaleItemId = " + id);
                String key = "cp_" + flashSalePo.getTimeSegId();
                if (redisTemplate.opsForSet().pop(key) != null) {
                    redisTemplate.opsForSet().pop(key);
                }
                returnObject = new ReturnObject();
            }
            return returnObject;
        }
        logger.info("无权限删除该商品");
        return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
    }

    /**
     * 新建秒杀活动
     *
     * @param id
     * @param flashSaleInputVo
     * @return
     */
    public ReturnObject createFlash(Long id, FlashSaleInputVo flashSaleInputVo) {

        if(!timeService.isSegExist(id)){
            logger.debug("时间段不存在");
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,"时间段不存在");

        }
        MyReturn<TimeSegInfo> timeSegInfo=timeService.getTimeSeg(id);
//        if(id<0){
//            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
//        }
        FlashSalePoExample example=new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria=example.createCriteria();
        if(flashSaleInputVo.getFlashDate()==null||flashSaleInputVo.getFlashDate().isBefore(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth() + 1, 0, 0, 0))){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        criteria.andTimeSegIdEqualTo(id);
        criteria.andFlashDateEqualTo(flashSaleInputVo.getFlashDate());
        criteria.andStateNotEqualTo(Byte.valueOf((byte) 2));
        List<FlashSalePo> po=flashSalePoMapper.selectByExample(example);
        if(po.size()!=0){
                return new ReturnObject(ResponseCode.TIMESEG_CONFLICT);
        }
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
                TimeSegVo timeSegVo=new TimeSegVo(timeSegInfo.getData());
                flashSaleRetVo.setTimeSeq(timeSegVo);
                return new ReturnObject(flashSaleRetVo);
            }
        } catch (Exception e) {
            logger.error("发生了严重的数据库错误 : " + e.getMessage());
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, e.getMessage());
        }
    }






    public boolean disableActivity(Long skuId) {
        FlashSaleItemPoExample example = new FlashSaleItemPoExample();
        FlashSaleItemPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId);
        try {
            List<FlashSaleItemPo> pos = flashSaleItemPoMapper.selectByExample(example);
            for (FlashSaleItemPo po : pos) {
                flashSaleItemPoMapper.deleteByPrimaryKey(po.getId());
                FlashSalePo flashSalePo = flashSalePoMapper.selectByPrimaryKey(po.getSaleId());
                flashSalePo.setGmtModified(LocalDateTime.now());
                flashSalePo.setState((byte) 2);
                flashSalePoMapper.updateByPrimaryKey(flashSalePo);
            }
            return true;
        } catch (Exception e) {
            logger.error("发生了严重的数据库错误 : " + e.getMessage());
            return false;
        }

    }



}
