package org.github.caishijun.eshop.inventory.mapper;

import org.apache.ibatis.annotations.Param;
import org.github.caishijun.eshop.inventory.model.ProductInventory;

/**
 * 库存数量 Mapper
 */
public interface ProductInventoryMapper {

    /**
     * 更新库存数量
     * @param productInventory 商品库存
     */
    void updateProductInventory(ProductInventory productInventory);

    /**
     * 根据商品id查询商品库存信息
     * @param productId 商品id
     * @return 商品库存信息
     */
    ProductInventory findProductInventory(@Param("productId") Integer productId);
}
