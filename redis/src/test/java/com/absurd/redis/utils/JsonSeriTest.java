package com.absurd.redis.utils;

import com.absurd.redis.springdata.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by wangwenwei on 16/12/18.
 */
public class JsonSeriTest {
    @Test
    public  void seDe(){
        UserDTO userDTO = new UserDTO(32323L,"www","fdsf","福建","34234",323423532L);
        ObjectMapper ob = new ObjectMapper();
         byte[] a= new byte[1];
        try {
            a = ob.writeValueAsBytes(userDTO);
            System.out.printf("" + ob.readValue(a,Object.class).toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
          String json =  ob.writeValueAsString(userDTO);
            System.out.printf(""+json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
