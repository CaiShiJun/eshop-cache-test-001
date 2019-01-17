package org.github.caishijun.eshop.cache.ha;

import org.github.caishijun.eshop.cache.ha.http.HttpClientUtils;

public class RequestCollapserTest {

    public static void main(String[] args) throws Exception {
        HttpClientUtils.sendGetRequest("http://localhost:8081/getProductInfos?productIds=1,1,2,2,3,4");
    }

}