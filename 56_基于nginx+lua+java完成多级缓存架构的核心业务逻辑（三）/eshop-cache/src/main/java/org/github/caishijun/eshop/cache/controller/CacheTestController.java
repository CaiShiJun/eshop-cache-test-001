package org.github.caishijun.eshop.cache.controller;

import org.github.caishijun.eshop.cache.model.ProductInfo;
import org.github.caishijun.eshop.cache.model.ShopInfo;
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

    /**
     * 获取商品信息
     * @param productId
     * @return
     */
    @RequestMapping("/getProductInfo")
    public ProductInfo getProductInfo(Long productId){

        //从 Redis 中获取商品数据
        ProductInfo productInfo = cacheService.getProductInfoFromReidsCache(productId);
        System.out.println("=================从redis中获取缓存，商品信息=" + productInfo);

        //从 本地ehcache缓存 中获取商品数据
        if(productInfo == null){
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            System.out.println("=================从ehcache中获取缓存，商品信息=" + productInfo);
        }

        if(productInfo == null){
            // 就需要从数据源重新拉取数据，重建缓存，但是这里先不讲
        }

        return productInfo;
    }

    /**
     * 获取店铺信息
     * @param shopId
     * @return
     */
    @RequestMapping("/getShopInfo")
    public ShopInfo getShopInfo(Long shopId){

        //从 Redis 中获取店铺数据
        ShopInfo shopInfo = cacheService.getShopInfoFromReidsCache(shopId);
        System.out.println("=================从redis中获取缓存，店铺信息=" + shopInfo);

        //从 本地ehcache缓存 中获取店铺数据
        if(shopInfo == null){
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
            System.out.println("=================从ehcache中获取缓存，店铺信息=" + shopInfo);
        }

        if(shopInfo == null){
            // 就需要从数据源重新拉取数据，重建缓存，但是这里先不讲
        }

        return shopInfo;
    }


}
