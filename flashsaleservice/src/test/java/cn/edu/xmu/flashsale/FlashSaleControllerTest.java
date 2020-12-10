package cn.edu.xmu.flashsale;

import cn.edu.xmu.flashsale.FlashsaleServiceApplication;
import cn.edu.xmu.flashsale.controller.FlashSaleController;
import cn.edu.xmu.flashsale.mapper.FlashSaleItemPoMapper;
import cn.edu.xmu.flashsale.mapper.FlashSalePoMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(classes = FlashsaleServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class FlashSaleControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    FlashSalePoMapper flashSalePoMapper;

    @Autowired
    FlashSaleItemPoMapper flashSaleItemPoMapper;

    /**
     * 修改秒杀活动
     */
    private static final Logger logger = LoggerFactory.getLogger(FlashSaleController.class);
    @Test void updateflashsale() throws Exception{
        String requireJson = "{\n" +
                "  \"flashData\": \"2020-11-28 17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/flashsale/flashsales/0")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
    }

    /**
     * 删除秒杀活动
     * @throws Exception
     */
    @Test
    public void deleteFlashSale() throws Exception {
        String responseString = this.mvc.perform(delete("/flashsale/flashsales/0")
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);

        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 增加秒杀活动商品
     * @throws Exception
     */
    @Test
        public  void addSKUofTopic() throws Exception {
        String requireJson = "{\n" +
               "  \"skuId\": 273,\n" +
               "  \"price\":\"1000\",\n" +
                "  \"quantity\": \"1000\"\n" +
              "}";
        String responseString = this.mvc.perform(post("/flashsale/flashsales/0/flashitems")
               .contentType("application/json;charset=UTF-8")
               .content(requireJson))
               .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
    System.out.println(responseString);
}
    /**
     * 删除秒杀活动中的sku
     * @throws Exception
     */
    @Test
    public void deleteFlashSaleSku() throws Exception {
        String responseString = this.mvc.perform(delete("/flashsale/flashsales/0/flashitems/0")
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);

        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

}
