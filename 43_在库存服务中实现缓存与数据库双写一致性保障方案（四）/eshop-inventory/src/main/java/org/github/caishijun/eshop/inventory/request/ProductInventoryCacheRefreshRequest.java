package org.github.caishijun.eshop.inventory.request;

import org.github.caishijun.eshop.inventory.model.ProductInventory;
import org.github.caishijun.eshop.inventory.service.ProductInventoryService;

/**
 * 重新加载商品库存的缓存
 */
public class ProductInventoryCacheRefreshRequest implements Request {

    // 商品库存
    private Integer productId;

    // 商品库存 Service
    private ProductInventoryService productInventoryService;

    // 是否强制刷新缓存
    private boolean forceRefresh;

    public ProductInventoryCacheRefreshRequest(Integer productId,ProductInventoryService productInventoryService,boolean forceRefresh) {
        this.productId = productId;
        this.productInventoryService = productInventoryService;
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void process() {
        // 从数据库中查询最新的商品库存数量
        ProductInventory productInventory = productInventoryService.findProductInventory(productId);
        // 将最新的商品库存数量，刷新到 redis 缓存中去
        productInventoryService.setProductInventoryCache(productInventory);
    }

    @Override
    public Integer getProductId() {
        return productId;
    }

    @Override
    public boolean isForceRefresh() {
        return forceRefresh;
    }

    @Override
    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }
}
