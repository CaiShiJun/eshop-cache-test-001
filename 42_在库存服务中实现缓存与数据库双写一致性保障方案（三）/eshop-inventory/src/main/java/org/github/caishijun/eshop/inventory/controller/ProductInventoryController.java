package org.github.caishijun.eshop.inventory.controller;

import org.github.caishijun.eshop.inventory.model.ProductInventory;
import org.github.caishijun.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import org.github.caishijun.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import org.github.caishijun.eshop.inventory.request.Request;
import org.github.caishijun.eshop.inventory.service.ProductInventoryService;
import org.github.caishijun.eshop.inventory.service.RequestAsyncProcessService;
import org.github.caishijun.eshop.inventory.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品库存Controller
 */
@RestController
public class ProductInventoryController {

    @Autowired
    private RequestAsyncProcessService requestAsyncProcessService;

    @Autowired
    private ProductInventoryService productInventoryService;

    /**
     * 更新商品库存（写请求）
     * @param productInventory
     * @return
     */
    @RequestMapping("updateProductInventory")
    public Response updateProductInventory(ProductInventory productInventory){
        Response response = null;
        try {
            Request request = new ProductInventoryDBUpdateRequest(productInventory,productInventoryService);
            requestAsyncProcessService.process(request);
            response = new Response(Response.SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            response = new Response(Response.FAILURE);
        }
        return response;
    }

    /**
     * 获取商品库存（读请求）
     * @param productId
     * @return
     */
    @RequestMapping("getProductInventory")
    public ProductInventory getProductInventory(Integer productId){
        ProductInventory productInventory = null;
        try {
            Request request = new ProductInventoryCacheRefreshRequest(productId,productInventoryService);
            requestAsyncProcessService.process(request);

            // 将请求扔给 service 异步去处理以后，就需要轮询 while(true) 一会儿，在这里 hang 住
            // 去尝试等待前面有商品库存更新的操作，同时缓存刷新的操作，将最新的数据刷新到缓存中
            long startTime = System.currentTimeMillis();
            long endTime = 0L;
            long waitTime = 0L;

            // 等待超过 200ms 没有从缓存中获取到结果。
            while(true){
                if(waitTime > 200){
                    break;
                }
                // 尝试去 redis 中读取一次商品库存的缓存数据
                productInventory = productInventoryService.getProductInventoryCache(productId);

                // 如果读取到了结果，那么就返回
                if(productInventory != null){
                    return productInventory;        //从缓存读到数据，返回结果
                }else{
                    // 如果没有读取到结果，那么等待一段时间
                    Thread.sleep(20);
                    endTime = System.currentTimeMillis();
                    waitTime = endTime - startTime;
                }
            }
            // 直接尝试从数据库中读取数据
            productInventory = productInventoryService.findProductInventory(productId);
            if(productInventory != null){
                return productInventory;        //从缓存没有读到数据，从数据库读到数据，返回结果
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ProductInventory(productId,-1L);     //从缓存和数据库都没有读到数据，返回结果
    }
}
