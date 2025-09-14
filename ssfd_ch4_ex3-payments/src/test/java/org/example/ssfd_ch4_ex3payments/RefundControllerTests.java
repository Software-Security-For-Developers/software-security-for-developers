package org.example.ssfd_ch4_ex3payments;

import org.example.ssfd_ch4_ex3payments.manager.HashManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RefundControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final HashManager hashManager = new HashManager();

    @Test
    void uploadRefunds_success_whenHashMatches() throws Exception {
        String body = "[\n  {\n    \"orderId\": \"10001\",\n    \"amount\": 120\n  },\n  {\n    \"orderId\": \"10002\",\n    \"amount\": 450\n  }\n]";
        String hash = hashManager.computeSha3Hex(body);

        mockMvc.perform(post("/api/refunds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Content-SHA3", hash)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].orderId").value("10001"))
                .andExpect(jsonPath("$[0].amount").value(120))
                .andExpect(jsonPath("$[1].orderId").value("10002"))
                .andExpect(jsonPath("$[1].amount").value(450));
    }

    @Test
    void uploadRefunds_failure_whenHashDoesNotMatch() throws Exception {
        String body = "[ { \"orderId\": \"10001\", \"amount\": 120 } ]";
        String wrongHash = "deadbeef"; // intentionally wrong

        mockMvc.perform(post("/api/refunds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Content-SHA3", wrongHash)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("invalid_hash"));
    }
}
