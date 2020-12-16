package cn.edu.xmu.flashsale;

import cn.edu.xmu.flashsale.controller.FlashSaleController;
import cn.edu.xmu.flashsale.mapper.FlashSaleItemPoMapper;
import cn.edu.xmu.flashsale.mapper.FlashSalePoMapper;
import cn.edu.xmu.flashsale.model.vo.FlashSaleInputVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug("token: " + token);
        return token;
    }

    @Test
    void updateflashsale() throws Exception {
        String requireJson = "{\n" +
                "  \"flashData\": \"2020-11-28 17:42:20\"\n" +
                "}";
        String responseString = this.mvc.perform(put("/flashsale/shops/0/flashsales/0")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
    }

    /**
     * 删除秒杀活动
     *
     * @throws Exception
     */
    @Test
    public void deleteFlashSale() throws Exception {
        String responseString = this.mvc.perform(delete("/flashsale/shops/0/flashsales/71")
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);

        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void queryTopicsByTime() throws Exception {
        String responseString = this.mvc.perform(get("/flashsale/timesegments/1/flashsales")
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 增加秒杀活动商品
     *
     * @throws Exception
     */
    @Test
    public void addSKUofTopic() throws Exception {
        String requireJson = "{\n" +
                "  \"skuId\": 273,\n" +
                "  \"price\":\"1000\",\n" +
                "  \"quantity\": \"1000\"\n" +
                "}";
        String responseString = this.mvc.perform(post("/flashsale/shops/0/flashsales/0/flashitems")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
    }

    /**
     * 删除秒杀活动中的sku
     *
     * @throws Exception
     */
    @Test
    public void deleteFlashSaleSku() throws Exception {
        String responseString = this.mvc.perform(delete("/flashsale/shops/0/flashsales/0/flashitems/0")
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);

        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void offshelvesFlashSale() throws Exception {
        String responseString = this.mvc.perform(put("/flashsale/shops/0/flashsales/1/onshelves")
                .contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);

        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    /**
     * 平台管理员在某个时间段下新建秒杀活动
     */
    @Test
    public void createFlash() throws Exception {
//        String requireJson = "{\n" +
//                "  \"flashData\": \"2020-11-28 17:42:20\"\n" +
//                "}";

        FlashSaleInputVo flashSaleInputVo = new FlashSaleInputVo();
        flashSaleInputVo.setFlashDate(LocalDateTime.now());
        String requireJson = JacksonUtil.toJson(flashSaleInputVo);

        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(post("/flashsale/shops/0/timesegments/3/flashsales")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", token)
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
    }


}
