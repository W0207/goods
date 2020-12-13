package cn.edu.xmu.presale.dao;

import cn.edu.xmu.ininterface.service.InShopService;
import cn.edu.xmu.ininterface.service.Ingoodservice;
import cn.edu.xmu.ininterface.service.model.vo.ShopToAllVo;
import cn.edu.xmu.ininterface.service.model.vo.SkuToPresaleVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.presale.mapper.PresaleActivityPoMapper;
import cn.edu.xmu.presale.model.po.PresaleActivityPo;
import cn.edu.xmu.presale.model.po.PresaleActivityPoExample;
import cn.edu.xmu.presale.model.vo.PresaleActivityRetVo;
import cn.edu.xmu.presale.model.vo.PresaleActivityVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PresaleDao {

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice goodservice;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private InShopService inShopService;

    @Autowired
    private PresaleActivityPoMapper presaleActivityPoMapper;

    public ReturnObject inserPresaleActivity(Long shopId, Long id, PresaleActivityVo presaleActivityVo) {
        ReturnObject returnObject = null;
        ShopToAllVo shopToAllVo = inShopService.presaleFindShop(shopId);
        if (!presaleActivityVo.getEndTime().isAfter(presaleActivityVo.getBeginTime())) {
            return new ReturnObject(ResponseCode.Log_Bigger);
        }
        if (shopToAllVo == null) {
            //店铺id不存在
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "inserPresaleActivity,shopId不存在");
        } else {
            SkuToPresaleVo skuToPresaleVo = goodservice.presaleFindSku(id);
            if (skuToPresaleVo == null) {
                //sku不存在
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "inserPresaleActivity,skuId不存在");
            } else {
                //判断sku在不在shop里
                if (!goodservice.skuInShopOrNot(shopId, id)) {
                    //不在商店里
                    returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "inserPresaleActivity,skuId不在shopId里");
                } else {
                    PresaleActivityPo presaleActivityPo = presaleActivityVo.creatPo();
                    presaleActivityPo.setState((byte) 0);
                    presaleActivityPo.setGmtCreate(LocalDateTime.now());
                    int retId = presaleActivityPoMapper.insert(presaleActivityPo);
                    PresaleActivityRetVo vo = new PresaleActivityRetVo(presaleActivityPo);
                    vo.setId((long) retId);
                    vo.setShop(shopToAllVo);
                    vo.setGoodsSku(skuToPresaleVo);
                    returnObject = new ReturnObject(vo);
                }
            }
        }
        return returnObject;
    }

    public ReturnObject<PageInfo<PresaleActivityRetVo>> findAllPresale(Long shopId, Integer timeLine, Long skuId, Integer page, Integer pageSize) {
        PresaleActivityPoExample presaleActivityPoExample = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = presaleActivityPoExample.createCriteria();
        if (page != null && pageSize != null) {
            PageHelper.startPage(page, pageSize);
        }
        if (shopId != null) {
            //shopId不为空
            criteria.andShopIdEqualTo(shopId);
        }
        //时间：0 还未开始的， 1 明天开始的，2 正在进行中的，3 已经结束的
        if (timeLine != null) {
            if (timeLine == 0) {
                //timeLine等于0还没开始的活动
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
            } else {
                if (timeLine == 1) {
                    //timeLine等于1明天开始的活动
                    criteria.andBeginTimeGreaterThan(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth() + 1, 0, 0, 0));
                } else {
                    if (timeLine == 2) {
                        //timeLine等于2正在进行的活动
                        criteria.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
                        criteria.andEndTimeGreaterThan(LocalDateTime.now());
                    } else {
                        //timeLine等于3已经结束的活动
                        if (timeLine == 3) {
                            criteria.andEndTimeLessThan(LocalDateTime.now());
                        }
                    }

                }
            }
        }
        if (skuId != null) {
            //spuId不为空
            criteria.andGoodsSkuIdEqualTo(skuId);
        }
        List<PresaleActivityPo> presaleActivityPos = presaleActivityPoMapper.selectByExample(presaleActivityPoExample);
        List<PresaleActivityRetVo> presaleActivityRetVos = new ArrayList<>(presaleActivityPos.size());
        for (PresaleActivityPo po : presaleActivityPos) {
            PresaleActivityRetVo vo = new PresaleActivityRetVo(po);
            vo.setShop(inShopService.presaleFindShop(po.getShopId()));
            vo.setGoodsSku(goodservice.presaleFindSku(po.getGoodsSkuId()));
            presaleActivityRetVos.add(vo);
        }
        PageInfo<PresaleActivityPo> poPageInfo = new PageInfo<>(presaleActivityPos);
        PageInfo<PresaleActivityRetVo> voPageInfo = new PageInfo<>(presaleActivityRetVos);
        voPageInfo.setPages(poPageInfo.getPages());
        voPageInfo.setPageNum(poPageInfo.getPageNum());
        voPageInfo.setPageSize(poPageInfo.getPageSize());
        voPageInfo.setTotal(poPageInfo.getTotal());
        /**
         * 还缺少关于sku的信息
         */
        return new ReturnObject<>(voPageInfo);
    }

    public ReturnObject modifyPresale(Long shopId, Long id, PresaleActivityVo vo) {
        ReturnObject returnObject;
        //判断开始时间是不是在结束时间之前
        if (vo.getEndTime().isBefore(vo.getBeginTime())) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if (vo.getPayTime().isBefore(vo.getBeginTime())) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        PresaleActivityPo presaleActivityPo = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (presaleActivityPo == null) {
            //预售活动的id不存在
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        } else {
            if (!shopId.equals(presaleActivityPo.getShopId())) {
                //预售活动的shopId与路径上的不同
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("修改预售信息的时候，预售活动的shopId与路径上的不同"));
            } else {
                if (presaleActivityPo.getState() == 2) {
                    //预售活动已经关闭
                    returnObject = new ReturnObject(ResponseCode.PRESALE_STATENOTALLOW);
                } else {
                    //修改活动信息
                    presaleActivityPo.setName(vo.getName());
                    presaleActivityPo.setAdvancePayPrice(vo.getAdvancePayPrice());
                    presaleActivityPo.setRestPayPrice(vo.getRestPayPrice());
                    presaleActivityPo.setQuantity(vo.getQuantity());
                    presaleActivityPo.setBeginTime(vo.getBeginTime());
                    presaleActivityPo.setPayTime(vo.getPayTime());
                    presaleActivityPo.setEndTime(vo.getEndTime());
                    presaleActivityPo.setGmtModified(LocalDateTime.now());
                    int ret = presaleActivityPoMapper.updateByPrimaryKey(presaleActivityPo);
                    if (ret == 0) {
                        returnObject = new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误"));
                    } else {
                        returnObject = new ReturnObject();
                    }
                }
            }
        }
        return returnObject;
    }

    public ReturnObject deletePresale(Long shopId, Long id) {
        ReturnObject returnObject = null;
        PresaleActivityPo presaleActivityPo = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (presaleActivityPo == null) {
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("删除预售活动时，预售活动的id不存在"));
        } else {
            if (!presaleActivityPo.getShopId().equals(shopId)) {
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, String.format("删除预售活动时，商店的id与活动里是商店id不一致"));
            } else {
                if (presaleActivityPo.getState().equals((byte) 1)) {
                    //处在上线拒绝操作
                    return new ReturnObject(ResponseCode.PRESALE_STATENOTALLOW);
                }
                if (presaleActivityPo.getState().equals((byte) 2)) {
                    return new ReturnObject();
                }
                presaleActivityPo.setState((byte) 2);
                presaleActivityPo.setGmtModified(LocalDateTime.now());
                int ret = presaleActivityPoMapper.updateByPrimaryKey(presaleActivityPo);
                if (ret == 0) {
                    returnObject = new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, String.format("数据库错误"));
                } else {
                    returnObject = new ReturnObject();
                }
            }
        }
        return returnObject;
    }

    public ReturnObject queryPresaleofSKU(Long shopId, Long id, Integer state) {
        ReturnObject returnObject = null;
        PresaleActivityPoExample presaleActivityPoExample = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = presaleActivityPoExample.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        if (id != null) {
            criteria.andGoodsSkuIdEqualTo(id);
        }
        if (state != null) {
            criteria.andStateEqualTo((byte) (int) state);
        }
        List<PresaleActivityPo> presaleActivityPos = presaleActivityPoMapper.selectByExample(presaleActivityPoExample);
        if (presaleActivityPos == null) {
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "没有对于shopID和skuID的优惠活动");
        } else {
            List<PresaleActivityRetVo> presaleActivityRetVos = new ArrayList<>(presaleActivityPos.size());
            for (PresaleActivityPo po : presaleActivityPos) {
                PresaleActivityRetVo vo = new PresaleActivityRetVo(po);
                vo.setShop(inShopService.presaleFindShop(po.getShopId()));
                vo.setGoodsSku(goodservice.presaleFindSku(po.getGoodsSkuId()));
                presaleActivityRetVos.add(vo);
            }
            return new ReturnObject(presaleActivityRetVos);
        }
        return returnObject;
    }

    public ReturnObject presaleOnShelves(Long shopId, Long id) {
        ReturnObject returnObject = null;
        PresaleActivityPo po = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "presaleOnShelves,活动id不存在");
        } else {
            if (!po.getShopId().equals(shopId)) {
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "presaleOnShelves,shopID不一致");
            } else {
                if (po.getState().equals((byte) 2)) {
                    returnObject = new ReturnObject(ResponseCode.PRESALE_STATENOTALLOW);
                } else {
                    if (po.getState().equals((byte) 1)) {
                        return new ReturnObject();
                    }
                    po.setState((byte) 1);
                    po.setGmtModified(LocalDateTime.now());
                    presaleActivityPoMapper.updateByPrimaryKey(po);
                    returnObject = new ReturnObject();
                }
            }
        }
        return returnObject;
    }

    public ReturnObject presaleOffShelves(Long shopId, Long id) {
        ReturnObject returnObject = null;
        PresaleActivityPo po = presaleActivityPoMapper.selectByPrimaryKey(id);
        if (po == null) {
            returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "presaleOnShelves,活动id不存在");
        } else {
            if (!po.getShopId().equals(shopId)) {
                returnObject = new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "presaleOnShelves,shopID不一致");
            } else {
                if (po.getState().equals((byte) 2)) {
                    returnObject = new ReturnObject(ResponseCode.PRESALE_STATENOTALLOW, "presaleOnShelves,预售状态不允许改变");
                } else {
                    if (po.getState().equals((byte) 0)) {
                        return new ReturnObject();
                    }
                    po.setState((byte) 0);
                    po.setGmtModified(LocalDateTime.now());
                    presaleActivityPoMapper.updateByPrimaryKey(po);
                    returnObject = new ReturnObject();
                }
            }
        }
        return returnObject;
    }
}
