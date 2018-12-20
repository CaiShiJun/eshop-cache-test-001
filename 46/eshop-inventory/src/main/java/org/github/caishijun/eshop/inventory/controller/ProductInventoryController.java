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
            Request request = new ProductInventoryCacheRefreshRequest(productId,productInventoryService,false);
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
                // 将因查询redis没有查询到而直接去数据库中查询到的数据，重新写入redis缓存
                // productInventoryService.setProductInventoryCache(productInventory);   这个过程，实际上是一个读操作的过程，但是没有放在队列中串行去处理，还是有数据不一致的问题，应当用如下方法：
                request = new ProductInventoryCacheRefreshRequest(productId,productInventoryService,true);
                requestAsyncProcessService.process(request);

                /*
                    代码会运行到这里，只有三种情况：
                        1、就是说，上一次也是读请求，数据刷入了 redis ，但是 redis LRU 算法给清理掉了，标志位还是 false 。所以此时下一个读请求是从缓存中拿不到数据的，再放一个读 request 进队列，让数据去刷新一下。
                        2、可能在 200ms 以内，就是读请求在队列中一直积压着，没有等待到它执行（在实际生产环境中，如果出现这种情况，基本是比较坑了）。所以就直接查一次库，然后给队列里塞进去一个刷新缓存的请求（第二次塞进去的重复读请求实际不会执行到，因为会被去重掉）。
                        3、数据库里本身就没有，缓存穿透，每次都会穿透 redis 请求到达数据库。
                 */
                return productInventory;        //从缓存没有读到数据，从数据库读到数据，返回结果
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ProductInventory(productId,-1L);     //从缓存和数据库都没有读到数据，返回结果
    }
}
