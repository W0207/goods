package cn.edu.xmu.goods.Controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.mapper.GoodsSpuPoMapper;
import cn.edu.xmu.goods.model.po.GoodsSpuPo;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GoodsServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class GoodsControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    GoodsSpuPoMapper goodsSpuPoMapper;

    /**
     * 获得商品spu所有状态
     */
    @Test
    public void getAllSpuState() throws Exception {
        String responseString = this.mvc.perform(get("/goods/spus/states"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"未发布\", \"code\": 0 }, { \"name\": \"发布\", \"code\": 1 }, { \"name\": \"废弃\", \"code\": 2 }], \"errmsg\": \"成功\" }";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 获得商品sku的详细信息
     */
    @Test
    public void findGoodsSkuById() throws Exception {
        String responseString = this.mvc.perform(get("/goods/skus/273"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void changeSpuInfoById() throws Exception {
        String requireJson = "{\n  \"name\":\"123\",\n  \"description\":\"123\",\n  \"specs\": \"123\"\n}";
        String responseString = this.mvc.perform(put("/goods/shops/0/spus/272")
                .contentType("application/json;charset=UTF-8")
                .content(requireJson))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
}
