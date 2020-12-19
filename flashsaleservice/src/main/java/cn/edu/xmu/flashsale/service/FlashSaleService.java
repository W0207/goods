package cn.edu.xmu.flashsale.service;

import cn.edu.xmu.flashsale.mapper.FlashSalePoMapper;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import cn.edu.xmu.flashsale.model.po.FlashSalePoExample;
import cn.edu.xmu.ininterface.service.DisableFlashActivityService;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.sun.el.stream.Stream;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import cn.edu.xmu.flashsale.model.vo.*;
import cn.edu.xmu.flashsale.model.bo.*;
import cn.edu.xmu.flashsale.dao.FlashSaleDao;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhai
 */
@Service
@DubboService(version = "0.0.1")
public class FlashSaleService implements DisableFlashActivityService {
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleService.class);

    @Autowired
    FlashSaleDao flashSaleDao;

    @Autowired
    private ReactiveRedisTemplate<String, Serializable> reactiveRedisTemplate;


    /**
     * 修改秒杀活动信息
     *
     * @param id
     * @param flashSaleInputVo
     * @return
     * @author zhai
     */
    public ReturnObject<Object> updateFlashSale(Long id, FlashSaleInputVo flashSaleInputVo) {
        return flashSaleDao.updateFlashSale(id, flashSaleInputVo);
    }

    /**
     * 删除秒杀活动
     *
     * @param id
     * @return
     * @author zhai
     */
    public ReturnObject<Object> deleteFlashSale(Long id) {
        return flashSaleDao.deleteFlashSale(id);
    }

    public ReturnObject<Object> onshelvesFlashSale(Long id) {
        return flashSaleDao.onshelvesFlashSale(id);
    }

    public ReturnObject<Object> offshelvesFlashSale(Long id) {
        return flashSaleDao.offshelvesFlashSale(id);
    }

    /**
     * 增加秒杀活动商品
     *
     * @param id
     * @param skuInputVo
     * @return
     * @author zhai
     */
    public ReturnObject addFlashSaleItem(Long id, SkuInputVo skuInputVo) {
        ReturnObject returnObject = flashSaleDao.addItem(id, skuInputVo);
        return returnObject;
    }


    /**
     * 删除SkuItem
     *
     * @param fid
     * @param id
     * @return
     * @author zhai
     */
    public ReturnObject<Object> deleteFlashSaleSku(Long fid, Long id) {
        return flashSaleDao.deleteSku(fid, id);
    }

    /**
     * 新建秒杀活动
     *
     * @param id
     * @return
     */
    public ReturnObject createFlash(Long id, FlashSaleInputVo flashSaleInputVo) {
        return flashSaleDao.createFlash(id, flashSaleInputVo);
    }

    public Flux<FlashSaleItem> getFlashSale(Long id) {
        return reactiveRedisTemplate.opsForSet().members("cp_" + id.toString()).map(x -> (FlashSaleItem) x);
    }

    @Override
    public boolean disableActivity(Long skuId) {
        return flashSaleDao.disableActivity(skuId);
    }
}

