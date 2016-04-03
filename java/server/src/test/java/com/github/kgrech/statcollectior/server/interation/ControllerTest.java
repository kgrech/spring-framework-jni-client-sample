package com.github.kgrech.statcollectior.server.interation;

import com.github.kgrech.statcollectior.server.StatCollectorServerApplication;
import com.github.kgrech.statcollectior.server.service.TestBase;
import com.github.kgrech.statcollectior.server.web.ClientStatisticsRecordController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import sun.security.acl.PrincipalImpl;

import java.security.Principal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


/**
 * Test case for rest controller
 * @author Konstantin G. (kgrech@mail.ru)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StatCollectorServerApplication.class)
@WebAppConfiguration
@IntegrationTest
public class ControllerTest extends TestBase {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private static String base = ClientStatisticsRecordController.version + "/statistics";
    private final Principal p = new PrincipalImpl("test_client_1");

    private final String[] floatTypes = new String[]{"memory", "cpu"};

    @Before
    public void setup() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Insets few records and checks the result
     */
    @Test
    public void testApis() throws Exception {
        int count = 5;
        for (int i = 0; i<5; i++) {
            for (String type: floatTypes) {
                mockMvc.perform(put(base + "/" + type)
                        .param("value", "0.05").principal(p))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json"))
                        .andExpect(jsonPath("$.type").value(type))
                        .andExpect(jsonPath("$.clientKey").value("test_client_1"))
                        .andExpect(jsonPath("$.value").value(0.05));
            }
        }
        Integer pageSize = count + 1;
        for (String type: floatTypes) {
            mockMvc.perform(get(base + "/" + type)
                    .param("page", "0")
                    .param("pageSize", pageSize.toString()).principal(p))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json"))
                    .andExpect(jsonPath("$", hasSize(count)));
        }
        pageSize = count * floatTypes.length + 1;
        mockMvc.perform(get(base)
                .param("page", "0")
                .param("pageSize", pageSize.toString()).principal(p))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(count * floatTypes.length)));
    }



}
