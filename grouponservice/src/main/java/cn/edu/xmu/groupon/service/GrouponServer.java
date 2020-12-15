package cn.edu.xmu.groupon.service;

import cn.edu.xmu.ininterface.service.DisableGrouponActivityService;
import cn.edu.xmu.ooad.util.encript.AES;
import com.github.pagehelper.PageHelper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import cn.edu.xmu.groupon.dao.GrouponDao;
import cn.edu.xmu.groupon.model.bo.*;
import cn.edu.xmu.groupon.model.po.*;
import cn.edu.xmu.groupon.model.vo.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@DubboService(version = "0.0.1")
public class GrouponServer implements DisableGrouponActivityService {

    private static final Logger logger = LoggerFactory.getLogger(GrouponServer.class);

    @Autowired
    GrouponDao grouponDao;


    /** 查询团购活动
     *  return ReturnObject<PageInfo<VoObject>>
     *
     * @Author zhai
     */

    public ReturnObject<PageInfo<VoObject>> findgrouponActivity(Integer timeline, Long spuId, Long shopId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        PageInfo<GrouponActivityPo> grouponActivityPos = grouponDao.findgroupons(timeline, spuId,shopId, page, pageSize);
        List<VoObject> groupons=grouponActivityPos.getList().stream().map(GrouponActivity::new).collect(Collectors.toList());

        PageInfo<VoObject> grouponAcvtivityPage = new PageInfo<>(groupons);
        grouponAcvtivityPage.setPages(grouponActivityPos.getPages());
        grouponAcvtivityPage.setPageNum(grouponActivityPos.getPageNum());
        grouponAcvtivityPage.setPageSize(grouponActivityPos.getPageSize());
        grouponAcvtivityPage.setTotal(grouponActivityPos.getTotal());
        return new ReturnObject<>(grouponAcvtivityPage) ;
    }

    public ReturnObject<PageInfo<VoObject>> findShopGroupon(Integer state, Long spuId,Long id,Integer page, Integer pageSize, String beginTime,String endTime){
        ReturnObject<PageInfo<VoObject>> returnObject = grouponDao.findShopGroupon(state, spuId,id,page, pageSize, beginTime,endTime);
        return returnObject;
        }
    /**
     * 查询Sku团购活动
     * @param id
     * @param state
     * @param shopId
     * @return
     * @author zhai
     */
    public ReturnObject<List> findSkuGroupon(Long id,Integer state,Long shopId){
        ReturnObject<List> returnObject=grouponDao.findSkuGrouponById(id,state,shopId);
        return  returnObject;
    }

    /**
     * 新增团购活动
     *  return ReturnObject<Object>
     * @Author zhai
     */
    public GrouponActivityPo addGroupon(Long id, GrouponInputVo grouponInputVo,Long shopId) {
        ReturnObject returnObject;
        GrouponActivityPo grouponActivityPo = grouponDao.addGroupon(id, grouponInputVo,shopId);
        if (grouponActivityPo != null) {
            logger.debug("addGroupon : " + grouponActivityPo);
            return  grouponActivityPo;

        } else {
            logger.debug("addGroupon : Failed");
            return grouponActivityPo;
        }

    }

    /**
     * 管理员修改SPU团购活动
     *
     * @param id              团购活动 id
     * @param grouponInputVo 可修改的团购活动信息
     * @return 返回对象 ReturnObject<Object>
     * @author zhai
     */
    public ReturnObject<Object> changeGroupon(Long id, GrouponInputVo grouponInputVo,Long shopId) {
        return grouponDao.modifyGrouponById(id, grouponInputVo,shopId);
    }


    /**
     * 管理员下架SKU团购活动
     *
     * @param id 团购活动 id
     * @return 返回对象 ReturnObject<Object>
     * @author shangzhao翟
     */
    public ReturnObject<Object> deleteGrouponState(Long shopId,Long id) {
        return grouponDao.deleteGrouponState(shopId,id);
    }

    /**
     * 下线团购活动
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject<Object> offGrouponState(Long shopId,Long id) {
        return grouponDao.offGrouponState(shopId,id);
    }

    /**
     *
     * @param shopId
     * @param id
     * @return
     */
    public ReturnObject<Object> onGrouponState(Long shopId,Long id) {
        return grouponDao.onGrouponState(shopId,id);
    }

    @Override
    public boolean disableActivity(Long shopId) {
        return grouponDao.disableActivity(shopId);
    }
}
