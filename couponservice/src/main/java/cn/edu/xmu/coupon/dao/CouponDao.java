package cn.edu.xmu.coupon.dao;

import cn.edu.xmu.coupon.mapper.CouponActivityPoMapper;
import cn.edu.xmu.coupon.mapper.CouponPoMapper;
import cn.edu.xmu.coupon.model.bo.Coupon;
import cn.edu.xmu.coupon.model.bo.CouponActivity;
import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.po.CouponActivityPoExample;
import cn.edu.xmu.coupon.model.po.CouponPo;
import cn.edu.xmu.coupon.model.vo.AddCouponActivityRetVo;
import cn.edu.xmu.coupon.model.vo.AddCouponActivityVo;
import cn.edu.xmu.ininterface.service.model.vo.ShopToAllVo;
import cn.edu.xmu.coupon.model.vo.CouponActivityRetVo;
import cn.edu.xmu.ininterface.service.InShopService;
import cn.edu.xmu.ininterface.service.model.vo.SkuToCouponVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CouponDao {

    @DubboReference(version = "0.0.1", check = false)
    private InShopService inShopService;

    @Autowired
    private CouponActivityPoMapper couponActivityPoMapper;

    @Autowired
    private CouponPoMapper couponPoMapper;


    private static final Logger logger = LoggerFactory.getLogger(CouponDao.class);

    public ReturnObject<PageInfo<VoObject>> showCouponactivities(Integer page, Integer pageSize, Long shopId, Long timeline) {
        CouponActivityPoExample couponActivityPoExample = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = couponActivityPoExample.createCriteria();
        List<CouponActivityPo> couponActivityPos = null;
        PageHelper.startPage(page, pageSize);
        if(shopId!=null) {
            //shopId不为空
            criteria.andShopIdEqualTo(shopId);
        }
        //时间：0 还未开始的， 1 明天开始的，2 正在进行中的，3 已经结束的
        if(timeline!=null){
            if(timeline==0){
                //timeLine等于0还没开始的活动
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
            } else {
                if(timeline==1){
                    //timeLine等于1明天开始的活动
                    criteria.andBeginTimeGreaterThan(LocalDateTime.of(LocalDateTime.now().getYear(),LocalDateTime.now().getMonth(),LocalDateTime.now().getDayOfMonth()+1,0,0,0));
                } else {
                    if(timeline==2){
                        //timeLine等于2正在进行的活动
                        criteria.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
                        criteria.andEndTimeGreaterThan(LocalDateTime.now());
                    } else {
                        //timeLine等于3已经结束的活动
                        if(timeline == 3){
                            criteria.andEndTimeLessThan(LocalDateTime.now());
                        }
                    }

                }
            }
        }
        try {
            couponActivityPos = couponActivityPoMapper.selectByExample(couponActivityPoExample);
            List<VoObject> ret = new ArrayList<>(couponActivityPos.size());
            for (CouponActivityPo po : couponActivityPos) {
                CouponActivity com = new CouponActivity(po);
                ret.add(com);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            PageInfo<CouponActivityPo> commentPoPage = PageInfo.of(couponActivityPos);
            PageInfo<VoObject> commentPage = new PageInfo<>(ret);
            commentPage.setPages(commentPoPage.getPages());
            commentPage.setPageNum(commentPoPage.getPageNum());
            commentPage.setPageSize(commentPoPage.getPageSize());
            commentPage.setTotal(commentPoPage.getTotal());
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("showCouponactivities: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject<PageInfo<VoObject>> showOwnInvalidcouponacitvitiesByid(Integer page, Integer pageSize, Long id) {
        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andStateEqualTo((byte) 1);
        criteria.andShopIdEqualTo(id);
        PageHelper.startPage(page, pageSize);
        List<CouponActivityPo> couponActivityPos = null;
        try {
            couponActivityPos = couponActivityPoMapper.selectByExample(example);
            List<VoObject> ret = new ArrayList<>(couponActivityPos.size());
            for (CouponActivityPo po : couponActivityPos) {
                CouponActivity com = new CouponActivity(po);
                ret.add(com);
            }
            PageInfo<VoObject> rolePage = PageInfo.of(ret);
            PageInfo<CouponActivityPo> commentPoPage = PageInfo.of(couponActivityPos);
            PageInfo<VoObject> commentPage = new PageInfo<>(ret);
            commentPage.setPages(commentPoPage.getPages());
            commentPage.setPageNum(commentPoPage.getPageNum());
            commentPage.setPageSize(commentPoPage.getPageSize());
            commentPage.setTotal(commentPoPage.getTotal());
            return new ReturnObject<>(rolePage);
        } catch (DataAccessException e) {
            logger.error("showOwnInvalidcouponacitvitiesByid: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject useCouponByCouponId(Long id, Long userId) {
        CouponPo couponPo = couponPoMapper.selectByPrimaryKey(id);
        if (couponPo == null) {
            logger.info("优惠券id= " + id + " 不存在");
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(!couponPo.getCustomerId().equals(userId))
        {
            return new ReturnObject<>(ResponseCode.AUTH_NOT_ALLOW);
        }
        if(couponPo.getState()!=1)
        {
            return new ReturnObject<>(ResponseCode.COUPON_STATENOTALLOW);
        }
        Coupon coupon = new Coupon(couponPo);
        CouponPo po = coupon.createCouponUserPo();
        couponPoMapper.updateByPrimaryKeySelective(po);
        logger.info("couponId = " + id + " 的优惠券已使用");
        return new ReturnObject<>();
    }


    /**
     * by 宇
     * 新建己方优惠活动
     * @param shopId
     * @param addCouponActivityVo
     * @return
     */
    public ReturnObject addCouponActivity(Long shopId, AddCouponActivityVo addCouponActivityVo){
        ReturnObject returnObject= null;
        ShopToAllVo shopToAllVo = inShopService.presaleFindShop(shopId);
        System.out.println(shopToAllVo.getName());
        if(shopToAllVo.equals(null)){
            returnObject= new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST,String.format("新建优惠活动shopId不存在"));
        }
        else {
            CouponActivityPo po= addCouponActivityVo.createPo();
            po.setGmtCreate(LocalDateTime.now());
            int retId = couponActivityPoMapper.insert(po);
            if(retId==0){
                returnObject = new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR,String.format("数据库发生错误"));
            }
            else {
                AddCouponActivityRetVo vo = new AddCouponActivityRetVo(po);
                vo.setShop(shopToAllVo);
                vo.setId(Long.parseLong(String.valueOf(retId)));
                returnObject = new ReturnObject(vo);
            }
        }
        return returnObject;
    }


    public ReturnObject<PageInfo<VoObject>> viewGoodsInCouponById(Integer page, Integer pageSize, List<SkuToCouponVo> skuToCouponVos) {
        PageHelper.startPage(page, pageSize);
        List<VoObject> ret = new ArrayList<>(skuToCouponVos.size());
        for (SkuToCouponVo vo : skuToCouponVos) {
            SkuToCouponVo com = new SkuToCouponVo(vo);
            ret.add((VoObject) com);
        }
        PageInfo<VoObject> rolePage = PageInfo.of(ret);
        PageInfo<SkuToCouponVo> commentPoPage = PageInfo.of(skuToCouponVos);
        PageInfo<VoObject> commentPage = new PageInfo<>(ret);
        commentPage.setPages(commentPoPage.getPages());
        commentPage.setPageNum(commentPoPage.getPageNum());
        commentPage.setPageSize(commentPoPage.getPageSize());
        commentPage.setTotal(commentPoPage.getTotal());
        return new ReturnObject<>(rolePage);
    }
}
