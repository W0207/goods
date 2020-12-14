package cn.edu.xmu.flashsale;

import cn.edu.xmu.flashsale.mapper.FlashSaleItemPoMapper;
import cn.edu.xmu.flashsale.mapper.FlashSalePoMapper;
import cn.edu.xmu.flashsale.model.bo.FlashSaleItem;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPoExample;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import cn.edu.xmu.flashsale.model.vo.FlashSaleItemRetVo;
import cn.edu.xmu.ininterface.service.Ingoodservice;
import cn.edu.xmu.ininterface.service.model.vo.SkuToFlashSaleVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;

import java.util.List;

/**
 * @author Abin
 */
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad", "cn.edu.xmu.flashsale"})
@MapperScan("cn.edu.xmu.flashsale.mapper")
@EnableDiscoveryClient//启动服务发现
public class FlashsaleServiceApplication implements ApplicationRunner {

    @Autowired
    private FlashSaleItemPoMapper flashSaleItemPoMapper;

    @Autowired
    private FlashSalePoMapper flashSalePoMapper;

    @Autowired
    @DubboReference(version = "0.0.1", check = false)
    private Ingoodservice ingoodservice;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(FlashsaleServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        FlashSaleItemPoExample example = new FlashSaleItemPoExample();
        List<FlashSaleItemPo> flashSaleItemPoList = flashSaleItemPoMapper.selectByExample(example);
        //以时间段为key载入redis
        for (FlashSaleItemPo itemPo : flashSaleItemPoList) {
            Long flashSaleId = itemPo.getSaleId();
            FlashSalePo flashSalePo = flashSalePoMapper.selectByPrimaryKey(flashSaleId);
            //获取时间段id
            Long timeSge = flashSalePo.getTimeSegId();
            Long skuId = itemPo.getGoodsSkuId();
            //获取sku信息
            SkuToFlashSaleVo skuToFlashSaleVo = ingoodservice.flashFindSku(skuId);
            //转为flashsaleitem
            FlashSaleItem item = new FlashSaleItem(itemPo, skuToFlashSaleVo);
            //将参与秒杀的sku信息载入内存(key为时间段)
            String key = "cp_" + timeSge;
            System.out.println("push : " + JacksonUtil.toJson(item));
            redisTemplate.opsForSet().add(key, item);
        }
    }
}
