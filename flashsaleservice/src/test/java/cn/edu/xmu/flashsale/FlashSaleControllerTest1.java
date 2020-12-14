package cn.edu.xmu.flashsale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(classes = FlashsaleServiceApplication.class)
@AutoConfigureWebTestClient
@AutoConfigureMockMvc
@Transactional
public class FlashSaleControllerTest1 {

    private WebTestClient webTestClient;

    public FlashSaleControllerTest1() {
        this.webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8090")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    @Test
    public void getFlashSaleTest() {

        webTestClient.get().uri("/flashsale/timesegments/1/flashsales").exchange()
                .expectBody()
                .jsonPath("$[?(@.id == 1)].goodsSku.imageUrl").isEqualTo("http://47.52.88.176/file/images/201612/file_586206d4c7d2f.jpg")
                .jsonPath("$[?(@.id == 2)].price").isEqualTo(24);
    }
}
