package org.github.caishijun.eshop.inventory.service;

import org.github.caishijun.eshop.inventory.model.ProductInventory;

/**
 * 商品库存 Service 接口
 */
public interface ProductInventoryService {

    /**
     * 更新商品库存
     * @param productInventory 商品库存
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 删除 Redis 中的商品库存的缓存
     * @param productInventory 商品库存
     */
    void removeProductInventoryCache(ProductInventory productInventory);

    /**
     * 根据商品id查询商品库存信息
     * @param productId 商品id
     * @return 商品库存信息
     */
    ProductInventory findProductInventory(Integer productId);

    /**
     * 设置商品库存的缓存
     * @param productInventory 商品库存
     */
    void setProductInventoryCache(ProductInventory productInventory);
}
