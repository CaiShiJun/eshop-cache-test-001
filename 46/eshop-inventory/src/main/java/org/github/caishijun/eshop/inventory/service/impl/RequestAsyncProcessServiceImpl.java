package org.github.caishijun.eshop.inventory.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.github.caishijun.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import org.github.caishijun.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import org.github.caishijun.eshop.inventory.request.Request;
import org.github.caishijun.eshop.inventory.request.RequestQueue;
import org.github.caishijun.eshop.inventory.service.RequestAsyncProcessService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * 请求异步处理的service实现
 */
@Service
public class RequestAsyncProcessServiceImpl implements RequestAsyncProcessService {

    @Override
    public void process(Request request) {
        try {
            // 做请求的路由，根据每个请求的商品id，路由到对应的内存队列中去
            ArrayBlockingQueue<Request> queue = getRoutingQueue(request.getProductId());

            // 将请求放入对应的队列中，完成路由操作
            queue.put(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取路由到的内存队列
     * @param productId 商品id
     * @return 内存队列
     */
    private ArrayBlockingQueue<Request> getRoutingQueue(Integer productId){
        RequestQueue requestQueue = RequestQueue.getInstance();

        // 先获取 productId 的 hash 值
        String key = String.valueOf(productId);
        int h;
        int hash = (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);

        // 将 hash 值取模，将 hash 值路由到指定的内存队列中，比如内存队列大小 8
        // 用内存队列的数量对 hash 值取模之后，结果一定是在 0~7 之间
        // 所以任何一个商品id都会被固定路由到同样的一个内存队列中去的
        int index = (requestQueue.queueSize() - 1) & hash;

        return requestQueue.getQueue(index);
    }

}
