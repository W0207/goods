package cn.edu.xmu.goods.Controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
import cn.edu.xmu.goods.model.vo.ShopVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import org.json.JSONException;
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

@SpringBootTest(classes = GoodsServiceApplication.class)   //标识本类是一个SpringBootTest
@AutoConfigureMockMvc    //配置模拟的MVC，这样可以不启动服务器测试
@Transactional
public class ShopTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void inserShopTest() {
        ShopVo vo = new ShopVo();
        vo.setName("test");
        String shopJson = JacksonUtil.toJson(vo);
        String expectedResponse = "";
        String responseString = null;
        try {
            responseString = this.mvc.perform(post("/shop/shops").contentType("application/json;charset=UTF-8").content(shopJson))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        expectedResponse = "{\"errno\":0,\"data\":{\"id\":1,\"name\":\"test\",\"gmtModified\":null},\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        try {
            JSONAssert.assertEquals(expectedResponse, responseString, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(responseString);

    }

    @Test
    public void getAllShopState() throws Exception {
        String responseString = this.mvc.perform(get("/shop/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"code\": 0,\"name\": \"未审核\" }, { \"name\": \"上线\", \"code\": 1 }, { \"name\": \"未上线\", \"code\": 2 }, { \"name\": \"关闭\", \"code\": 3 }], \"errmsg\": \"成功\" }";
        JSONAssert.assertEquals(expectedResponse, responseString, false);
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
    }
}
