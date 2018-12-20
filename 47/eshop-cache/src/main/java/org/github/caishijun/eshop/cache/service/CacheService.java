package org.github.caishijun.eshop.cache.service;

import org.github.caishijun.eshop.cache.model.ProductInfo;

/**
 * 缓存 service 接口
 */
public interface CacheService {

    /**
     * 将商品信息保存到本地缓存中
     * @param productInfo
     * @return
     */
    ProductInfo saveProductInfo(ProductInfo productInfo);

    /**
     * 从本地缓存中获取商品信息
     * @param id
     * @return
     */
    ProductInfo findById(Long id);
}
