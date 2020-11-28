package cn.edu.xmu.goods.Controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GoodsServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class GoodsControllerTest {
    @Autowired
    MockMvc mvc;

    /**
     * 获得商品spu所有状态
     */
    @Test
    public void getAllSpuState() throws Exception {
        String responseString = this.mvc.perform(get("/goods/spus/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"未发布\", \"code\": 0 }, { \"name\": \"发布\", \"code\": 1 }, { \"name\": \"废弃\", \"code\": 2 }], \"errmsg\": \"成功\" }";
        JSONAssert.assertEquals(expectedResponse, responseString, true);

    }
}
