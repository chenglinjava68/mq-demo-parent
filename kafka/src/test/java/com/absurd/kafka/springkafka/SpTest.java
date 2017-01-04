package com.absurd.kafka.springkafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;


/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka
 * @Description:
 * @date 2017/1/4 11:32
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SpTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MockMvc mvc;

    @Test
    public void exampleTest() {
        Map body = this.restTemplate.getForObject("/jobs/list", Map.class);
        System.out.printf(""+body);
    }

}
