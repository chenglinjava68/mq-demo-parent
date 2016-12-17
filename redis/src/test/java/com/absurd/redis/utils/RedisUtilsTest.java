package com.absurd.redis.utils;


import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasXPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.redis.utils
 * @Description:
 * @date 2016/12/12 19:05
 */
public class RedisUtilsTest {
    @Test
    public void simple_set(){
       assertEquals(RedisUtils.set("my_simple_set","b"),"OK");
    }


    @Test
    public void simple_get(){
        RedisUtils.set("my_simple_get","b");
        assertEquals( RedisUtils.get("my_simple_get"),"b");
    }

    @Test
    public void simple_mset_get(){
        Map<String,String> keyvalue = new HashMap<>();
        keyvalue.put("my_simple_mset_get1","1");
        keyvalue.put("my_simple_mset_get2","2");
        keyvalue.put("my_simple_mset_get3","3");
        keyvalue.put("my_simple_mset_get4","4");
        keyvalue.put("my_simple_mset_get5","5");
        keyvalue.put("my_simple_mset_get6","6");
        RedisUtils.mset(keyvalue);
        List<String> values = RedisUtils.mget("my_simple_mset_get1","my_simple_mset_get2","my_simple_mset_get3","my_simple_mset_get4","my_simple_mset_get5","my_simple_mset_get6");
        assertThat(values,hasItems("1","3","5"));
    }

    @Test
    public void xpath_test() throws IOException, SAXException, ParserConfigurationException {
       String str = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
               "" +
               "<bookstore>" +
               "" +
               "<book category=\"COOKING\">" +
               "  <title lang=\"en\">Everyday Italian</title>" +
               "  <author>Giada De Laurentiis</author>" +
               "  <year>2005</year>" +
               "  <price>30.00</price>" +
               "</book>" +
               "" +
               "<book category=\"CHILDREN\">" +
               "  <title lang=\"en\">Harry Potter</title>" +
               "  <author>J K. Rowling</author>" +
               "  <year>2005</year>" +
               "  <price>29.99</price>" +
               "</book>" +
               "" +
               "<book category=\"WEB\">" +
               "  <title lang=\"en\">XQuery Kick Start</title>" +
               "  <author>James McGovern</author>" +
               "  <author>Per Bothner</author>" +
               "  <author>Kurt Cagle</author>" +
               "  <author>James Linn</author>" +
               "  <author>Vaidyanathan Nagarajan</author>" +
               "  <year>2003</year>" +
               "  <price>49.99</price>" +
               "</book>" +
               "" +
               "<book category=\"WEB\">" +
               "  <title lang=\"en\">Learning XML</title>" +
               "  <author>Erik T. Ray</author>" +
               "  <year>2003</year>" +
               "  <price>39.95</price>" +
               "</book>" +
               "" +
               "</bookstore>";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new ByteArrayInputStream(str.getBytes()));
        assertThat("aa",doc,hasXPath("/bookstore/book/title"));
    }

    @Test
    public void simple_pub_sub() throws InterruptedException {
        Thread t2 =new Thread(()->{
            RedisUtils.subscribe("simple_pub_sub");
        });

        Thread t =new Thread(()->{
            RedisUtils.publish("simple_pub_sub","a");
            RedisUtils.publish("simple_pub_sub","b");
            RedisUtils.publish("simple_pub_sub","d");
            RedisUtils.publish("simple_pub_sub","e");
        });
        t2.start();

        Thread.sleep(1000);
        t.start();
    }

    @Test
    public void simple_sadd(){
        if(  RedisUtils.exists("simple_add")){
            RedisUtils.del("simple_add");
        }
      assertEquals( RedisUtils.sadd("simple_add","a","b","c").longValue(),3L);
        assertEquals( RedisUtils.scard("simple_add").longValue(),3L);
        assertThat(  RedisUtils.smembers("simple_add"),containsInAnyOrder("c","b","a"));
    }
    @Test
    public void simple_set_diff(){
        if(  RedisUtils.exists("simple_add")){
            RedisUtils.del("simple_add");
        }
        assertEquals( RedisUtils.sadd("simple_add","a","b","c").longValue(),3L);
        if(  RedisUtils.exists("simple_add2")){
            RedisUtils.del("simple_add2");
        }
        assertEquals( RedisUtils.sadd("simple_add2","b","c","d").longValue(),3L);

        assertEquals(RedisUtils.sdiff("simple_add","simple_add2"),containsInAnyOrder("a"));
    }

    @Test
    public void simple_get_with_time() throws InterruptedException {
        RedisUtils.set("my_simple_get_with_time","b",1L);
        assertEquals( RedisUtils.get("my_simple_get_with_time"),"b");
        assertNotEquals( RedisUtils.ttl("my_simple_get_with_time"),-2L);
        assertEquals( RedisUtils.pttl("my_simple_get_with_time"),999L);
        Thread.sleep(1000L);
        assertNull( RedisUtils.get("my_simple_get_with_time"));
        assertEquals( RedisUtils.ttl("my_simple_get_with_time"),-2L);
        assertEquals( RedisUtils.pttl("my_simple_get_with_time"),-2L);
        assertFalse(RedisUtils.exists("my_simple_get_with_time"));
    }

    @Test
    public void simple_incr(){
        RedisUtils.set("my_simple_incr",1);
        for(int i=0;i<100;i++)
        RedisUtils.incr("my_simple_incr");
        assertEquals( RedisUtils.get("my_simple_incr"),"101");
    }

    @Test
    public void simple_lpush_rpop(){
      if(  RedisUtils.exists("my_simple_lpush")){
          RedisUtils.del("my_simple_lpush");
      }
        RedisUtils.lpush("my_simple_lpush","a","b");
        RedisUtils.lpush("my_simple_lpush","c","d");
        assertEquals(  RedisUtils.lpop("my_simple_lpush"),"d");
        assertEquals(  RedisUtils.rpop("my_simple_lpush"),"a");
        RedisUtils.rpush("my_simple_lpush","e","f");
        assertEquals(  RedisUtils.lpop("my_simple_lpush"),"c");
        assertEquals(  RedisUtils.rpop("my_simple_lpush"),"f");
    }

    @Test
    public void simple_setbit(){
        if(  RedisUtils.exists("simple_setbit")){
            RedisUtils.del("simple_setbit");
        }
        RedisUtils.setbit("simple_setbit",1,true);
        assertFalse(RedisUtils.getbit("simple_setbit",2));
        assertTrue(RedisUtils.getbit("simple_setbit",1));
        RedisUtils.setbit("simple_setbit",2,true);
        RedisUtils.setbit("simple_setbit",3,true);
        assertEquals(RedisUtils.bitcount("simple_setbit").longValue(),3L);
    }

    @Test
    public void use_bit_in_sign(){
        RedisUtils.del("signtoken|*");
      boolean flag =  sign(1,"2016-12-01","2016-12-03","2016-12-05","2016-12-06","2016-12-07");
        assertTrue(isSign(1,"2016-12-03"));
        assertFalse(isSign(1,"2016-12-04"));
      assertEquals( accSign(1,"2016-12").longValue(),5L);
    }

    private Long accSign(int uid, String yearMonth) {
        StringBuilder sbf = new StringBuilder();
        sbf.append("signtoken|");
        sbf.append(uid);
        sbf.append("|").append(yearMonth);
        return RedisUtils.bitcount(sbf.toString());
    }

    private boolean isSign(int uid, String time) {
        StringBuilder sbf = new StringBuilder();
        sbf.append("signtoken|");
        sbf.append(uid);
        int ltIndex=  time.lastIndexOf("-");
        sbf.append("|").append(time.substring(0,ltIndex));
        return   RedisUtils.getbit(sbf.toString(),Integer.valueOf(time.substring(ltIndex+1)));
    }

    private boolean sign(int uid, String ...times) {
        int length = times.length;
        for(int i=0;i<length;i++){
           int ltIndex=  times[i].lastIndexOf("-");
           String token ="signtoken|"+ uid+"|"+times[i].substring(0,ltIndex);
            RedisUtils.setbit(token,Integer.valueOf(times[i].substring(ltIndex+1)),true);
        }

        return false;
    }

    @Test
    public void count_before() throws ParseException {
        Calendar calendarNew = Calendar.getInstance();
        calendarNew.add(Calendar.DATE, -8888);
       String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(calendarNew.getTime());
        System.out.println("8888天前：" + str);

        Calendar calendar = Calendar.getInstance();
        // 设置传入的时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 指定一个日期
        Date date = dateFormat.parse("1993-07-17 8:24:16");
        // 对 calendar 设置为 date 所定的日期
        calendar.setTime(date);
        long diff = Calendar.getInstance().getTime().getTime() - calendar.getTime().getTime();

        long days = diff / (1000 * 60 * 60 * 24);
        System.out.println("差距：" +days);
    }


}
