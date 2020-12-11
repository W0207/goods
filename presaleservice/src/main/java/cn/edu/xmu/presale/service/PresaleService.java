package cn.edu.xmu.presale.service;

import cn.edu.xmu.ininterface.service.InShopService;
import cn.edu.xmu.ininterface.service.Ingoodservice;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.presale.dao.PresaleDao;
import cn.edu.xmu.presale.model.bo.PresaleActivity;
import cn.edu.xmu.presale.model.po.PresaleActivityPo;
import cn.edu.xmu.presale.model.vo.PresaleActivityRetVo;
import cn.edu.xmu.presale.model.vo.PresaleActivityVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Service
public class PresaleService {

    @Autowired
    private PresaleDao presaleDao;


    /**
     * 新增预售活动
     * @param presaleActivityVo
     * @return
     */
    public ReturnObject AddPresaleActivity(Long shopId,Long id, PresaleActivityVo presaleActivityVo) {
        return presaleDao.inserPresaleActivity(shopId,id,presaleActivityVo);
    }

    public ReturnObject selectAllPresale(Long shopId, Integer timeLine, Long skuId, Integer page,Integer pageSize)
    {
        return presaleDao.findAllPresale(shopId, timeLine, skuId, page, pageSize);
    }

    public ReturnObject modifyPresale(Long shopId,Long id,PresaleActivityVo vo){
        return presaleDao.modifyPresale(shopId, id, vo);
    }

    public ReturnObject deletePresale( Long shopId, Long id){
        return presaleDao.deletePresale(shopId, id);
    }

    public ReturnObject queryPresaleofSKU( Long shopId,  Long id, Integer state){
        return presaleDao.queryPresaleofSKU(shopId, id, state);
    }

    public ReturnObject presaleOnShelves(Long shopId,Long id) {
        return presaleDao.presaleOnShelves(shopId, id);
    }

    public ReturnObject presaleOffShelves(Long shopId,Long id) {
        return presaleDao.presaleOffShelves(shopId, id);
    }
}
