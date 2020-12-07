package cn.edu.xmu.presale.Controller;

import cn.edu.xmu.presale.PresaleServiceApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}
