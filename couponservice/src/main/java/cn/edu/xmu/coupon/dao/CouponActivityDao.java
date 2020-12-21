package cn.edu.xmu.coupon.dao;

import cn.edu.xmu.coupon.mapper.CouponActivityPoMapper;
import cn.edu.xmu.coupon.mapper.CouponSkuPoMapper;
import cn.edu.xmu.coupon.model.bo.CouponActivity;
import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.po.CouponActivityPoExample;
import cn.edu.xmu.coupon.model.po.CouponSkuPo;
import cn.edu.xmu.coupon.model.po.CouponSkuPoExample;
import cn.edu.xmu.coupon.mapper.CouponPoMapper;
import cn.edu.xmu.coupon.model.po.*;
import cn.edu.xmu.coupon.model.vo.*;
import cn.edu.xmu.ininterface.service.InShopService;
import cn.edu.xmu.ininterface.service.Ingoodservice;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.privilegeservice.client.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author BiuBiuBiu
 */

@Repository
public class CouponActivityDao implements InitializingBean {

    @DubboReference(version = "0.0.1", check = false)
    private InShopService inShopService;

    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice ingoodservice;

    @Autowired
    CouponActivityPoMapper couponActivityPoMapper;

    @Autowired
    CouponPoMapper couponPoMapper;

    @Autowired
    CouponSkuPoMapper couponSkuPoMapper;

    private IUserService iUserService;

    private static final Logger logger = LoggerFactory.getLogger(CouponActivityDao.class);

    public CouponActivityPo findCouponActivityById(Long id) {
        return couponActivityPoMapper.selectByPrimaryKey(id);
    }

    public ReturnObject<Object> modifyCouponActivityByID(Long id, Long shopId, CouponActivityModifyVo couponActivityModifyVo,Long ShopId) {

        CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
        if (couponActivityPo == null) {
            logger.info("优惠活动id= " + id + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(couponActivityPo.getState()==2){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(!couponActivityPo.getShopId().equals(shopId)) {
            logger.debug("商店不对");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        if (couponActivityPo.getState() != 0) {
            return new ReturnObject<>(ResponseCode.COUPONACT_STATENOTALLOW);
        }
        CouponActivity couponActivity = new CouponActivity(couponActivityPo);
        CouponActivityPo po = couponActivity.createModifyPo(couponActivityModifyVo);
        couponActivityPoMapper.updateByPrimaryKeySelective(po);
        logger.info("couponActivityId = " + id + " 的信息已更新");
        return new ReturnObject<>();
    }

    public ReturnObject CouponActivityOnShelves(Long shopId, Long id) {
        ReturnObject returnObject = null;
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            //活动id不存在
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "优惠活动的id不存在");
        } else {
            if (!po.getShopId().equals(shopId)) {
                //shopId不一致
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, "上线活动的时候，路径shopId和活动的id不一致");
            } else {
                if(po.getState().equals((byte) 2) ){
                    return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
                }

                if ( po.getState().equals((byte) 1)) {
                    //优惠活动被删除了或者已在上线
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

    public ReturnObject CouponActivityOffShelves(Long shopId, Long id) {
        ReturnObject returnObject = null;
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("优惠活动的id不存在"));
        } else {
            if (!po.getShopId().equals(shopId)) {
                //shopId不一致
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, String.format("下线活动的时候，路径shopId和活动的id不一致"));
            } else {
                if (po.getState().equals((byte) 1)) {
                    //上线状态才可以进行下线
                    po.setGmtModified(LocalDateTime.now());
                    //0表示下线
                    po.setState((byte) 0);
                    couponActivityPoMapper.updateByPrimaryKey(po);
                    //如果是有优惠券将优惠券进行修改状态
                    if (po.getQuantity().equals(0)) {
                        //quantity代表不使用优惠券
                        returnObject = new ReturnObject();
                    } else {
                        //有优惠券状态改为失效
                        CouponPoExample couponPoExample = new CouponPoExample();
                        CouponPoExample.Criteria criteria = couponPoExample.createCriteria();
                        criteria.andActivityIdEqualTo(id);
                        List<CouponPo> pos = couponPoMapper.selectByExample(couponPoExample);
                        //失效所有的优惠券,根据状态图来
                        for (CouponPo po1 : pos) {
                            //3代表失效
                            po1.setState((byte) 3);
                            po1.setGmtModified(LocalDateTime.now());
                            couponPoMapper.updateByPrimaryKey(po1);
                        }
                        returnObject = new ReturnObject();
                    }
                } else {
                    if(po.getState().equals((byte)2)){
                        return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
                    }
                    returnObject = new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW);

                }
            }
        }
        return returnObject;
    }

    public ReturnObject deleteCouponActivity(Long shopId, Long id) {
        ReturnObject returnObject = null;
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(id);
        //1.判断优惠活动是否存在
        if (po == null) {
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "优惠活动id不存在");
        } else {
            //2.判断店铺id是否相同
            if (!shopId.equals(po.getShopId())) {
                //店铺id不相同
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, "店铺id和活动里的店铺id不一致");
            } else {
                //3.判断优惠活动状态是否为下线
                if (po.getState().equals((byte) 1)) {
                    //上线状态
                    returnObject = new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW);
                } else {
                    //处在删除状态就不进行任何操作
                    if (po.getState().equals((byte) 0)) {
                        //下线状态
                        //1.修改优惠活动状态
                        po.setState((byte) 2);
                        po.setGmtModified(LocalDateTime.now());
                        couponActivityPoMapper.updateByPrimaryKey(po);
                        //2.删除活动的sku
                        CouponSkuPoExample example = new CouponSkuPoExample();
                        CouponSkuPoExample.Criteria criteria = example.createCriteria();
                        criteria.andActivityIdEqualTo(id);
                        List<CouponSkuPo> pos = couponSkuPoMapper.selectByExample(example);
                        for (CouponSkuPo couponSkuPo : pos) {
                            couponSkuPoMapper.deleteByPrimaryKey(po.getId());
                        }
                        returnObject = new ReturnObject();
                    } else {
                        returnObject = new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW);
                    }
                }
            }
        }
        return returnObject;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    private void skuDeleteNotExit(List<CouponSkuPo> skuPos) {
        for (CouponSkuPo po : skuPos) {
            couponSkuPoMapper.deleteByPrimaryKey(po.getId());
        }
    }

    public ReturnObject rangeForCouponActivityById(Long id, Long shopId, Long[] skuIds) {
        CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(id);
        if (couponActivityPo == null) {
            System.out.println("************"+1+"   "+id);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(couponActivityPo.getState().equals((byte)2)){
            System.out.println("************"+2);
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (!couponActivityPo.getShopId().equals(shopId)) {
            System.out.println("asdasd");
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        List<CouponSkuPo> skuPos = new ArrayList<>();
        for (Long aId : skuIds) {
            System.out.println(aId);
            if (!ingoodservice.skuExitOrNot(aId)) {
                System.out.println(1234);
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            if (!ingoodservice.skuInShopOrNot(shopId, aId)) {
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
            CouponSkuPo couponSkuPo = new CouponSkuPo();
            couponSkuPo.setActivityId(id);
            couponSkuPo.setGmtCreate(LocalDateTime.now());
            couponSkuPo.setSkuId(aId);
            skuPos.add(couponSkuPo);
        }
        return new ReturnObject();
    }

    public ReturnObject deleteCouponSkuById(Long id, Long shopId) {
        //找到范围
        CouponSkuPo couponSkuPo = couponSkuPoMapper.selectByPrimaryKey(id);
        if (couponSkuPo == null) {
            logger.info("该范围不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //取出活动判断shopId是否相同
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(couponSkuPo.getActivityId());
        if (!po.getShopId().equals(shopId)) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        couponSkuPoMapper.deleteByPrimaryKey(id);
        return new ReturnObject();
    }

//        //从活动表中取id
//        CouponActivityPoExample example = new CouponActivityPoExample();
//        CouponActivityPoExample.Criteria criteria = example.createCriteria();
//        //当shopId相等
//        criteria.andShopIdEqualTo(shopId);
//        List<CouponActivityPo> couponActivityPo = null;
//        ReturnObject<Object> returnObject;
//        int ret = 0;
//        couponActivityPo = couponActivityPoMapper.selectByExample(example);
//        for (CouponActivityPo po1 : couponActivityPo) {
//            if (po1.getId().equals(couponSkuPo.getActivityId())) {
//                ret = couponSkuPoMapper.deleteByPrimaryKey(id);
//            }
//        }
//        if (ret == 0) {
//            logger.info("无权限操作");
//            returnObject = new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
//        } else {
//            returnObject = new ReturnObject<>();
//        }
//        return returnObject;
//    }

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
        try {
            CouponActivityPoExample example = new CouponActivityPoExample();
            CouponActivityPoExample.Criteria criteria = example.createCriteria();
            criteria.andShopIdEqualTo(shopId);
            List<Byte> states = new ArrayList<>();
            states.add((byte) 0);
            states.add((byte) 1);
            criteria.andStateIn(states);
            List<CouponActivityPo> pos = couponActivityPoMapper.selectByExample(example);
            for (CouponActivityPo po : pos) {
                //删除优惠活动
                po.setGmtModified(LocalDateTime.now());
                po.setState((byte) 2);
                couponActivityPoMapper.updateByPrimaryKey(po);
                CouponPoExample couponPoExample = new CouponPoExample();
                CouponPoExample.Criteria criteria1 = couponPoExample.createCriteria();
                criteria1.andActivityIdEqualTo(po.getId());
                List<CouponPo> pos1 = couponPoMapper.selectByExample(couponPoExample);
                for (CouponPo po1 : pos1) {
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
                for (CouponSkuPo po1 : pos2) {
                    couponSkuPoMapper.deleteByPrimaryKey(po1.getId());
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ReturnObject findCouponActivity(Long shopId, Long id) {
        CouponActivityPo po = couponActivityPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (!po.getShopId().equals(shopId)) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        AddCouponActivityRetVo vo = new AddCouponActivityRetVo(po);
        UserVo userVo = new UserVo();
        userVo.setId(po.getCreatedBy());
        //userVo.setName(iUserService.getUserName(po.getCreatedBy()));
        vo.setCreatedBy(userVo);
        if (po.getModiBy() != null) {
            UserVo userVo1 = new UserVo();
            userVo1.setId(po.getModiBy());
            //userVo1.setName(iUserService.getUserName(po.getModiBy()));
            vo.setModiBy(userVo1);
        }
        vo.setShop(inShopService.presaleFindShop(shopId));
        return new ReturnObject(vo);

    }
}
