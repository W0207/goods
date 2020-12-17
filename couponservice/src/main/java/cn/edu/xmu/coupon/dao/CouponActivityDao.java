package cn.edu.xmu.coupon.dao;

import cn.edu.xmu.coupon.mapper.CouponActivityPoMapper;
import cn.edu.xmu.coupon.mapper.CouponSkuPoMapper;
import cn.edu.xmu.coupon.model.bo.CouponActivity;
import cn.edu.xmu.coupon.model.bo.CouponSku;
import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.po.CouponActivityPoExample;
import cn.edu.xmu.coupon.model.po.CouponSkuPo;
import cn.edu.xmu.coupon.model.po.CouponSkuPoExample;
import cn.edu.xmu.coupon.mapper.CouponPoMapper;
import cn.edu.xmu.coupon.mapper.CouponSkuPoMapper;
import cn.edu.xmu.coupon.model.bo.CouponActivity;
import cn.edu.xmu.coupon.model.po.*;
import cn.edu.xmu.coupon.model.vo.CouponActivityModifyVo;
import cn.edu.xmu.coupon.model.vo.CouponActivitySkuInputVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author BiuBiuBiu
 */

@Repository
public class CouponActivityDao implements InitializingBean {

    @Autowired
    CouponActivityPoMapper couponActivityPoMapper;

    @Autowired
    CouponPoMapper couponPoMapper;

    @Autowired
    CouponSkuPoMapper couponSkuPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(CouponActivityDao.class);

    public CouponActivityPo findCouponActivityById(Long id){return couponActivityPoMapper.selectByPrimaryKey(id);}

    public ReturnObject<Object> modifyCouponActivityByID(Long id, Long shopId, CouponActivityModifyVo couponActivityModifyVo) {

        CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
        if (couponActivityPo == null) {
            logger.info("优惠活动id= " + id + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (!couponActivityPo.getShopId().equals(shopId) && shopId != 0) {
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if(couponActivityPo.getState()!=0) {
            return new ReturnObject<>(ResponseCode.COUPONACT_STATENOTALLOW);
        }
        CouponActivity couponActivity = new CouponActivity(couponActivityPo);
        CouponActivityPo po = couponActivity.createModifyPo(couponActivityModifyVo);
        couponActivityPoMapper.updateByPrimaryKeySelective(po);
        logger.info("couponActivityId = " + id + " 的信息已更新");
        return new ReturnObject<>();
    }

    public ReturnObject CouponActivityOnShelves(Long shopId,Long id)
    {
        ReturnObject returnObject = null;
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(id);
        if(po.equals(null)){
            //活动id不存在
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("优惠活动的id不存在"));
        }else {
            if (!po.getShopId().equals(shopId)) {
                //shopId不一致
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("上线活动的时候，路径shopId和活动的id不一致"));
            } else {
                if (po.getState().equals((byte) 2)) {
                    //优惠活动被删除了
                    returnObject = new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW);
                } else {
                    //更新数据库
                    po.setState((byte) 1);
                    po.setGmtModified(LocalDateTime.now());
                    couponActivityPoMapper.updateByPrimaryKey(po);
                    returnObject = new ReturnObject();
                }
            }
        }
        return returnObject;
    }

    public ReturnObject CouponActivityOffShelves(Long shopId,Long id){
        ReturnObject returnObject = null;
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(id);
        if(po.equals(null)){
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("优惠活动的id不存在"));
        }else {
            if (!po.getShopId().equals(shopId)) {
                //shopId不一致
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("下线活动的时候，路径shopId和活动的id不一致"));
            } else {
                if (po.getState().equals((byte) 1)) {
                    //上线状态才可以进行下线
                    po.setGmtModified(LocalDateTime.now());
                    //0表示下线
                    po.setState((byte)0);
                    couponActivityPoMapper.updateByPrimaryKey(po);
                    //如果是有优惠券将优惠券进行修改状态
                    if(po.getQuantity().equals(0)){
                        //quantity代表不使用优惠券
                        returnObject = new ReturnObject();
                    }else {
                        //有优惠券状态改为失效
                        CouponPoExample couponPoExample = new CouponPoExample();
                        CouponPoExample.Criteria criteria = couponPoExample.createCriteria();
                        criteria.andActivityIdEqualTo(id);
                        List<CouponPo> pos = couponPoMapper.selectByExample(couponPoExample);
                        //失效所有的优惠券,根据状态图来
                        for(CouponPo po1:pos){
                            //3代表失效
                            po1.setState((byte)3);
                            po1.setGmtModified(LocalDateTime.now());
                            couponPoMapper.updateByPrimaryKey(po1);
                        }
                        returnObject = new ReturnObject();
                    }
                } else {
                    if (po.getState().equals((byte) 0)) {
                        //本身处在下线的状态,不进行操作
                        returnObject = new ReturnObject();
                    } else {
                        returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("下线活动的时候，状态转换失败"));
                    }
                }
            }
        }
        return returnObject;
    }

    public ReturnObject deleteCouponActivity(Long shopId,Long id){
        ReturnObject returnObject = null;
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(id);
        //1.判断优惠活动是否存在
        if(po == null){
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,"优惠活动id不存在");
        } else {
            //2.判断店铺id是否相同
            if(!shopId.equals(po.getShopId())){
                //店铺id不相同
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,"店铺id和活动里的店铺id不一致");
            } else {
                //3.判断优惠活动状态是否为下线
                if(po.getState().equals((byte)1)) {
                    //上线状态
                    returnObject = new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW);
                } else {
                    //处在删除状态就不进行任何操作
                    if(po.getState().equals((byte)0)){
                        //下线状态
                        //1.修改优惠活动状态
                        po.setState((byte)2);
                        po.setGmtModified(LocalDateTime.now());
                        couponActivityPoMapper.updateByPrimaryKey(po);
                        //2.删除活动的sku
                        CouponSkuPoExample example = new CouponSkuPoExample();
                        CouponSkuPoExample.Criteria criteria = example.createCriteria();
                        criteria.andActivityIdEqualTo(id);
                        List<CouponSkuPo> pos = couponSkuPoMapper.selectByExample(example);
                        for(CouponSkuPo couponSkuPo:pos){
                            couponSkuPoMapper.deleteByPrimaryKey(po.getId());
                        }
                    }
                    returnObject = new ReturnObject();
                }
            }
        }
        return returnObject;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public CouponSkuPo rangeForCouponActivityById(Long id, Long shopId, CouponActivitySkuInputVo couponActivitySkuInputVo) {
        CouponSku couponSku = new CouponSku();
        CouponSkuPo couponSkuPo = couponSku.createAddCouponActivityPo(couponActivitySkuInputVo, id, shopId);

        int ret = couponSkuPoMapper.insertSelective(couponSkuPo);
        if (ret == 0) {
            //检查新增是否成功
            couponSkuPo = null;
        } else {
            logger.info("新增范围成功");
        }
        return couponSkuPo;
    }

    public ReturnObject deleteCouponSkuById(Long id, Long shopId) {
        //找到范围
        CouponSkuPo couponSkuPo = couponSkuPoMapper.selectByPrimaryKey(id);
        if (couponSkuPo == null) {
            logger.info("该范围不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //从活动表中取id
        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();
        //当shopId相等
        criteria.andShopIdEqualTo(shopId);
        List<CouponActivityPo> couponActivityPo = null;
        ReturnObject<Object> returnObject;
        int ret = 0;
        couponActivityPo = couponActivityPoMapper.selectByExample(example);
        for (CouponActivityPo po : couponActivityPo) {
            if (po.getId().equals(couponSkuPo.getActivityId())) {
                ret = couponSkuPoMapper.deleteByPrimaryKey(id);
            }
        }
        if (ret == 0) {
            logger.info("无权限操作");
            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            returnObject = new ReturnObject<>();
        }
        return returnObject;
    }

    /**
     * @param id
     * @return
     * @author shibin zhan
     */
    public ReturnObject<CouponActivity> getCouponActivityById(Long id) {
        CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
        if (couponActivityPo == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            CouponActivity couponActivity = new CouponActivity(couponActivityPo);
            return new ReturnObject<>(couponActivity);
        }
    }

    /**
     * @param couponActivity
     * @return
     * @author shibin zhan
     */
    public ReturnObject updateGoodsSpuImgUrl(CouponActivity couponActivity) {
        ReturnObject returnObject = new ReturnObject();
        CouponActivityPo couponActivityPo = new CouponActivityPo();
        couponActivityPo.setId(couponActivity.getId());
        couponActivityPo.setImageUrl(couponActivity.getImage_url());
        int ret = couponActivityPoMapper.updateByPrimaryKeySelective(couponActivityPo);
        if (ret == 0) {
            logger.debug("updateGoodsSpuImgUrl: update fail. spu id: " + couponActivity.getId());
            returnObject = new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateGoodsSpuImgUrl: update sku success : " + couponActivity.toString());
            returnObject = new ReturnObject();
        }
        return returnObject;
    }

    public boolean disableActivity(Long shopId) {
        try{
            CouponActivityPoExample example = new CouponActivityPoExample();
            CouponActivityPoExample.Criteria criteria = example.createCriteria();
            criteria.andShopIdEqualTo(shopId);
            List<Byte> states = new ArrayList<>();
            states.add((byte)0);
            states.add((byte)1);
            criteria.andStateIn(states);
            List<CouponActivityPo> pos = couponActivityPoMapper.selectByExample(example);
            for(CouponActivityPo po: pos){
                //删除优惠活动
                po.setGmtModified(LocalDateTime.now());
                po.setState((byte)2);
                couponActivityPoMapper.updateByPrimaryKey(po);
                CouponPoExample couponPoExample = new CouponPoExample();
                CouponPoExample.Criteria criteria1 = couponPoExample.createCriteria();
                criteria1.andActivityIdEqualTo(po.getId());
                List<CouponPo> pos1 = couponPoMapper.selectByExample(couponPoExample);
                for(CouponPo po1:pos1){
                    //优惠券失效
                    logger.debug("失效优惠券.....");
                    po1.setGmtModified(LocalDateTime.now());
                    po1.setState((byte) 3);
                    couponPoMapper.updateByPrimaryKey(po1);
                }
                //删除商品sku
                CouponSkuPoExample example1 = new CouponSkuPoExample();
                CouponSkuPoExample.Criteria criteria2 = example1.createCriteria();
                criteria2.andActivityIdEqualTo(po.getId());
                List<CouponSkuPo> pos2 = couponSkuPoMapper.selectByExample(example1);
                for(CouponSkuPo po1 :pos2){
                    couponSkuPoMapper.deleteByPrimaryKey(po1.getId());
                }
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
