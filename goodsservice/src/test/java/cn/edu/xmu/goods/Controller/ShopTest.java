package cn.edu.xmu.goods.Controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.controller.ShopController;
import cn.edu.xmu.goods.mapper.ShopPoMapper;
import cn.edu.xmu.goods.model.po.ShopPo;
import cn.edu.xmu.goods.model.vo.ShopVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GoodsServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ShopTest {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    private final String creatTestToken(Long userId, Long departId, int expireTime) {
        String token = new JwtHelper().createToken(userId, departId, expireTime);
        logger.debug("token: " + token);
        return token;
    }

    private WebTestClient mallClient;

    @Test
    public void create() {
        logger.debug("************************" + creatTestToken(1L, 8L, 1000 * 60 * 60 * 24) + "*****************");
    }


    public ShopTest() {
        this.mallClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8084")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    @Autowired
    ShopPoMapper shopPoMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    public void insertShopTest() {
        String token = creatTestToken(1L, -1L, 100);

        ShopVo vo = new ShopVo();
        vo.setName("test");
        String shopJson = JacksonUtil.toJson(vo);
        String expectedResponse = "";
        String responseString = null;

        try {
            responseString = this.mvc.perform(post("/shop/shops").contentType("application/json;charset=UTF-8").content(shopJson).header("authorization", token))
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //expectedResponse = "{\"errno\":0,\"data\":{\"id\":1,\"name\":\"test\",\"gmtModified\":null},\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        /*try {
            JSONAssert.assertEquals(expectedResponse, responseString, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(responseString);*/
    }

    @Test
    public void getAllShopState() throws Exception {
        String responseString = this.mvc.perform(get("/shop/shops/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void updateShop() throws Exception {
        ShopVo vo = new ShopVo();
        vo.setName("test");
        String shopJson = JacksonUtil.toJson(vo);

        String responseString = this.mvc.perform(
                put("/shop/shops/1")
                        .contentType("application/json;charset=UTF-8").content(shopJson)
        ).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
        System.out.println(shopPoMapper.selectByPrimaryKey((long) 1).getName());
    }

    @Test
    public void shopOnShelves() throws Exception {

        String responseString = this.mvc.perform(
                put("/shop/shops/1/onshelves")
                        .contentType("application/json;charset=UTF-8")
        ).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void shopOffShelves() throws Exception {

        String responseString = this.mvc.perform(
                put("/shop/shops/2/offshelves")
                        .contentType("application/json;charset=UTF-8")
        ).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void deleteShop() throws Exception {

        String responseString = this.mvc.perform(
                delete("/shop/shops/1")
                        .contentType("application/json;charset=UTF-8")
        ).andExpect(content().contentType("application/json;charset=UTF-8")).andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    /**
     * 管理员审核店铺
     */
    @Test
    public void auditShop() throws Exception {
        String requireJson = "{\n" +
                "  \"state\":\"2\"\n" +
                "}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/shop/shops/0/newshops/2/audit")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        ShopPo shopPo = shopPoMapper.selectByPrimaryKey(2L);
        System.out.println(shopPo.getState());
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    @Test
    public void auditShopPass() throws Exception {
        String requireJson = "{\n" +
                "  \"state\":\"2\"\n" +
                "}";
        String token = creatTestToken(1L, 0L, 100);
        String responseString = this.mvc.perform(put("/shop/shops/0/newshops/1/audit")
                .header("authorization", token)
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        ShopPo shopPo = shopPoMapper.selectByPrimaryKey(1L);
        System.out.println(shopPo.getState());
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }



}
