package com.absurd.kafka.springkafka;

import com.absurd.kafka.springkafka.model.UserDTO;
import com.absurd.kafka.springkafka.thread.Producter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:wangwenwei@myhexin.com">王文伟</a>
 * @Company:浙江核新同花顺网络信息股份有限公司
 * @Title: mq-demo-parent
 * @Package com.absurd.kafka.springkafka
 * @Description:
 * @date 2016/12/22 10:28
 */
public class GetStrSizeTest {
    private static Logger logger = LoggerFactory.getLogger(GetStrSizeTest.class);
    private static int count(String str) {
        if(str == null || str.length() == 0) {
            return 0;
        }
        int count = 0;
        char[] chs = str.toCharArray();
        for(int i = 0; i < chs.length; i++) {
            count += (chs[i] > 0xff) ? 2 : 1;
        }
        return count;
    }

    @Test
    public void getStr() throws JsonProcessingException {
        ObjectMapper objectMapper= new ObjectMapper();
        UserDTO userDTO = new UserDTO(1000L,"www","fdsf","福建","34234",323423532L);
       String small = objectMapper.writeValueAsString(userDTO);
        logger.info("small>>"+count(small));


        StringBuilder sbf = new StringBuilder();
        for(int i=0;i<1000;i++)
            sbf.append("www");
        userDTO.setUserName(sbf.toString());
        String mid = objectMapper.writeValueAsString(userDTO);
        logger.info("mid>>"+count(mid));

        sbf = new StringBuilder();
        for(int i=0;i<9000000;i++)
            sbf.append("www");
        userDTO.setUserName(sbf.toString());
        String big = objectMapper.writeValueAsString(userDTO);
        logger.info("big>>"+count(big));
    }


}
