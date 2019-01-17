package org.github.caishijun.eshop.cache.service.impl;

import org.github.caishijun.eshop.cache.model.ProductInfo;
import org.github.caishijun.eshop.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service("cacheService")
public class CacheServiceImpl implements CacheService {

    public static final String CACHE_NAME = "local";

    /**
     * 从本地缓存中获取商品信息
     * @param id
     * @return
     */
    @Cacheable(value = CACHE_NAME, key = "'key_'+#id")
    public ProductInfo findById(Long id){
        return null;
    }

    /**
     * 将商品信息保存到本地缓存中
     * @param productInfo
     * @return
     */
    @CachePut(value = CACHE_NAME, key = "'key_'+#productInfo.getId()")
    public ProductInfo saveProductInfo(ProductInfo productInfo) {
        return productInfo;
    }

}
