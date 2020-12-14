package cn.edu.xmu.flashsale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(classes = FlashsaleServiceApplication.class)
@AutoConfigureWebTestClient
public class FlashSaleControllerTest1 {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getFlashSaleTest() {

        webTestClient.get().uri("/flashsale/timesegments/1/flashsales").exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[?(@.id == 1)].goodsSku.name").isEqualTo("+")
                .jsonPath("$[?(@.id == 2)].goodsSku.name").isEqualTo("+");
    }
}
