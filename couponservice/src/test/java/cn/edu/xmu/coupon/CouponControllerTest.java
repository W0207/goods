package cn.edu.xmu.coupon;

import cn.edu.xmu.coupon.controller.CouponController;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CouponServiceApplication.class)
@AutoConfigureMockMvc
@Transactional

public class CouponControllerTest {
    @Autowired
    MockMvc mvc;

    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug("token: " + token);
        return token;
    }

    /**
     * 获得优惠券所有状态
     */
    @Test
    public void getCouponState() throws Exception {
        String responseString = this.mvc.perform(get("/coupon/states"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"未领取\", \"code\": 0 }, { \"name\": \"已领取\", \"code\": 1 }, { \"name\": \"已使用\", \"code\": 2 }, { \"name\": \"已失效\", \"code\": 3 }], \"errmsg\": \"成功\" }";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 查看上线的优惠活动列表
     */

    @Test
    public void showOwncouponactivities() throws Exception {
        String responseString = this.mvc.perform(get("/coupon/couponactivities"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }


    /**
     * 查看本店下线的优惠活动列表
     */

    @Test
    public void showOwnInvalidcouponacitvitiesByid() throws Exception {

        String token = creatTestToken(1L, 123L, 100);
        String responseString = this.mvc.perform(get("/coupon/shops/123/couponactivities/invalid").header("authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

}
