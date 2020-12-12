package cn.edu.xmu.flashsale.service;

import org.springframework.stereotype.Service;
import cn.edu.xmu.ooad.util.encript.AES;
import com.github.pagehelper.PageHelper;
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
import cn.edu.xmu.flashsale.model.vo.*;
import cn.edu.xmu.flashsale.model.bo.*;
import cn.edu.xmu.flashsale.model.po.*;
import cn.edu.xmu.flashsale.dao.FlashSaleDao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlashSaleService {
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleService.class);

    @Autowired
    FlashSaleDao flashSaleDao;

    /**
     * 查找某时段秒杀活动
     * @param id
     * @return
     */
    public  List<FlashSaleOutputVo> findFlashSaleByTime(Long id){
        List<FlashSaleOutputVo> returnObject=flashSaleDao.findFlashSaleItemByTime( id);
        return returnObject;
    }



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


    /**
     * 增加秒杀活动商品
     *
     * @param id
     * @param skuInputVo
     * @return
     * @author zhai
     */
    public FlashSaleItem addFlashSaleItem(Long id, SkuInputVo skuInputVo) {
        ReturnObject returnObject;
        FlashSaleItem flashSaleItem = flashSaleDao.addItem(id, skuInputVo);
        return flashSaleItem;
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
}

