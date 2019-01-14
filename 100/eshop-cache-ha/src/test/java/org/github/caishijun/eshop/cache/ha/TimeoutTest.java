package org.github.caishijun.eshop.cache.ha;

import org.github.caishijun.eshop.cache.ha.http.HttpClientUtils;

public class TimeoutTest {

    public static void main(String[] args) throws Exception {
        HttpClientUtils.sendGetRequest("http://localhost:8081/getProductInfo?productId=-3");

        Thread.sleep(2000);
        System.out.println("==========================");

        HttpClientUtils.sendGetRequest("http://localhost:8081/getProductInfo?productId=1");

    }

}
