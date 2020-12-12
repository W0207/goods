package cn.edu.xmu.presale.Controller;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.presale.PresaleServiceApplication;
import cn.edu.xmu.presale.model.vo.PresaleActivityVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(classes = PresaleServiceApplication.class)
@AutoConfigureMockMvc
@Transactional
public class PresaleActivityTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void getPresaleState() throws Exception {
        String responseString = this.mvc.perform(get("/presale/presales/states"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);

    }

    @Test
    public void test() throws Exception {

        PresaleActivityVo presaleActivityVo=new PresaleActivityVo();
        presaleActivityVo.setName("童振宇");
        presaleActivityVo.setAdvancePayPrice(100L);
        presaleActivityVo.setRestPayPrice(100L);
        presaleActivityVo.setQuantity(100);
        presaleActivityVo.setBeginTime(LocalDateTime.now());
        presaleActivityVo.setPayTime(LocalDateTime.now());
        presaleActivityVo.setEndTime(LocalDateTime.now());
        String shopJson = JacksonUtil.toJson(presaleActivityVo);
        String expectedResponse = "";

        String responseString = this.mvc.perform(post("/presale/shops/1/skus/273/presales").contentType("application/json;charset=UTF-8").content(shopJson))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void selectAllPresale() throws Exception {
        String responseString = this.mvc.perform(get("/presale/presales?shopId=2&timeLine=1").contentType("application/json;charset=UTF-8"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void modifyPresale() throws Exception {
        PresaleActivityVo presaleActivityVo=new PresaleActivityVo();
        presaleActivityVo.setName("童振宇");
        presaleActivityVo.setAdvancePayPrice(100L);
        presaleActivityVo.setRestPayPrice(100L);
        presaleActivityVo.setQuantity(100);
        presaleActivityVo.setBeginTime(LocalDateTime.now());
        presaleActivityVo.setPayTime(LocalDateTime.of(2020,12,15,0,0));
        presaleActivityVo.setEndTime(LocalDateTime.of(2020,12,25,0,0));
        String shopJson = JacksonUtil.toJson(presaleActivityVo);
        String expectedResponse = "";
        String responseString = this.mvc.perform(put("/presale/shops/5/presales/1").contentType("application/json;charset=UTF-8").content(shopJson))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void deletePresale() throws Exception {
        String responseString = this.mvc.perform(delete("/presale/shops/1/presales/3").contentType("application/json;charset=UTF-8"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void presaleOnShelves() throws Exception {
        String responseString = this.mvc.perform(put("/presale/shops/1/presales/1/onshelves").contentType("application/json;charset=UTF-8"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void presaleOffShelves() throws Exception {
        String responseString = this.mvc.perform(put("/presale/shops/1/presales/2/offshelves").contentType("application/json;charset=UTF-8"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

    @Test
    public void queryPresaleofSPU() throws Exception {
        String responseString = this.mvc.perform(get("/presale/shops/1/presales?state=0").contentType("application/json;charset=UTF-8"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(responseString);
    }

}
