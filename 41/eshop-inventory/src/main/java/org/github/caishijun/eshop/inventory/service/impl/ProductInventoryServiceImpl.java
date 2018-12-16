package org.github.caishijun.eshop.inventory.service.impl;

import org.github.caishijun.eshop.inventory.dao.RedisDAO;
import org.github.caishijun.eshop.inventory.mapper.ProductInventoryMapper;
import org.github.caishijun.eshop.inventory.model.ProductInventory;
import org.github.caishijun.eshop.inventory.service.ProductInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品库存 Service 实现类
 */
@Service
public class ProductInventoryServiceImpl implements ProductInventoryService {

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Autowired
    private RedisDAO redisDAO;

    @Override
    public void updateProductInventory(ProductInventory productInventory) {
        productInventoryMapper.updateProductInventory(productInventory);
    }

    @Override
    public void removeProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:"+productInventory.getProductId();
        redisDAO.delete(key);
    }

    @Override
    public ProductInventory findProductInventory(Integer productId) {
        return productInventoryMapper.findProductInventory(productId);
    }

    @Override
    public void setProductInventoryCache(ProductInventory productInventory) {
        String key = "product:inventory:"+productInventory.getProductId();
        redisDAO.set(key,String.valueOf(productInventory.getInventoryCnt()));
    }
}
