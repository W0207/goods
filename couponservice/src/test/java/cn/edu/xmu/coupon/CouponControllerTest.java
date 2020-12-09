package cn.edu.xmu.coupon;

import cn.edu.xmu.coupon.controller.CouponController;
import cn.edu.xmu.coupon.mapper.CouponActivityPoMapper;
import cn.edu.xmu.coupon.mapper.CouponPoMapper;
import cn.edu.xmu.coupon.mapper.CouponSkuPoMapper;
import cn.edu.xmu.coupon.model.po.CouponActivityPo;
import cn.edu.xmu.coupon.model.po.CouponPo;
<<<<<<< HEAD
import cn.edu.xmu.coupon.model.po.CouponSkuPo;
=======
import cn.edu.xmu.coupon.model.vo.AddCouponActivityVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
>>>>>>> 39bbcccd23906dd00d50307405a199efe0c17814
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

import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CouponServiceApplication.class)
@AutoConfigureMockMvc
@Transactional

public class CouponControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    CouponActivityPoMapper couponActivityPoMapper;

    @Autowired
    CouponSkuPoMapper couponSkuPoMapper;

    @Autowired
    CouponPoMapper couponPoMapper;

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

    /**
     * 管理员修改己方某优惠活动
     */
    @Test
    public void modifyCouponActivity() throws Exception {
        String requireJson = "{\n  \"name\":\"啊这\",\n  \"quantity\": \"20\"\n}";
        String token = creatTestToken(1L, 123L, 100);
        String responseString = this.mvc.perform(put("/coupon/shops/123/couponactivities/1")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        CouponActivityPo couponActivityPo = couponActivityPoMapper.selectByPrimaryKey(1L);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 买家使用自己某优惠券
     */
    @Test
    public void useCoupon1() throws Exception {
        String token = creatTestToken(1L, 123L, 100);
        String responseString = this.mvc.perform(put("/coupon/1")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        CouponPo couponPo = couponPoMapper.selectByPrimaryKey(1L);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void useCoupon2() throws Exception {
        String token = creatTestToken(1L, 123L, 100);
        String responseString = this.mvc.perform(put("/coupon/3")
                .header("authorization", token))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        CouponPo couponPo = couponPoMapper.selectByPrimaryKey(1L);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

<<<<<<< HEAD

    /**
     * 管理员为己方某优惠券活动新增限定范围
     *
     * @throws Exception
     */
    @Test
    public void rangeForCouponActivity() throws Exception {
        String token = creatTestToken(1L, 1L, 100);
        String requireJson = "{\n  \"skuId\":\"123456\"}";
        String responseString = this.mvc.perform(post("/coupon/shops/1/couponactivities/1/skus")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        CouponSkuPo couponPo = couponSkuPoMapper.selectByPrimaryKey(1L);
        System.out.println(responseString);
    }
=======
    @Test
    public void addCouponActivity() throws Exception {
        byte a= 10;
        AddCouponActivityVo vo = new AddCouponActivityVo();
        vo.setName("tzy");
        vo.setQuantity(10);
        vo.setQuantityType(a);
        vo.setValidTerm(a);
        vo.setCouponTime(LocalDateTime.now());
        vo.setBeginTime(LocalDateTime.now());
        vo.setEndTime(LocalDateTime.now());
        vo.setStrategy("aaaaa");
        String jsonStr = JacksonUtil.toJson(vo);

        String responseString = this.mvc.perform(post("/coupon/shops/8/couponactivities").contentType("application/json;charset=UTF-8").content(jsonStr))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

>>>>>>> 39bbcccd23906dd00d50307405a199efe0c17814
}
