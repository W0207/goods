package cn.edu.xmu.goods.Controller;

import cn.edu.xmu.goods.GoodsServiceApplication;
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
public class CommentControllerTest {
    @Autowired
    MockMvc mvc;

    /**
     * 获得评论所有状态
     */
    @Test
    public void getCommentState() throws Exception {
        String responseString = this.mvc.perform(get("/comments/states"))
                .andReturn().getResponse().getContentAsString();
        String expectedResponse = "{ \"errno\": 0, \"data\": [ { \"name\": \"未审核\", \"code\": 0 }, { \"name\": \"评论成功\", \"code\": 1 }, { \"name\": \"未通过\", \"code\": 2 }], \"errmsg\": \"成功\" }";
        System.out.println(responseString);
        JSONAssert.assertEquals(expectedResponse, responseString, true);
    }

    /**
     * 查看sku的评价列表（已通过审核）
     */

    @Test
    public void show() throws Exception {

        String responseString = this.mvc.perform(get("/comments/skus/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        //String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(responseString);
        //JSONAssert.assertEquals(expectedResponse, responseString, true);
    }
}
