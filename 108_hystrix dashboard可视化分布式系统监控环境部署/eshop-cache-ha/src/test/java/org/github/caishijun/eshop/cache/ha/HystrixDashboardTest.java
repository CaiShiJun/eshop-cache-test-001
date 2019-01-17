package org.github.caishijun.eshop.cache.ha;

import org.github.caishijun.eshop.cache.ha.http.HttpClientUtils;

public class HystrixDashboardTest {

    public static void main(String[] args) {
        HttpClientUtils.sendGetRequest("http://localhost:8081/getProductInfo?productId=1");
    }

}
