package org.github.caishijun.eshop.cache.controller;

import org.github.caishijun.eshop.cache.model.ProductInfo;
import org.github.caishijun.eshop.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Ehcache 本地缓存测试Controller
 */
@RestController
public class CacheTestController {

    @Autowired
    private CacheService cacheService;

    // curl -X GET localhost:8080/testPutCache?id=1\&name=test_product\&price=55.50
    @RequestMapping("/testPutCache")
    public void testPutCache(ProductInfo productInfo) {
        System.out.println(productInfo.getId() + ":" + productInfo.getName());
        cacheService.saveProductInfo(productInfo);
    }

    // curl -X GET localhost:8080/testGetCache?id=1
    @RequestMapping("/testGetCache")
    public ProductInfo testGetCache(Long id) {
        ProductInfo productInfo = cacheService.findById(id);
        System.out.println(productInfo.getId() + ":" + productInfo.getName());
        return productInfo;
    }

}
