package org.github.caishijun.eshop.inventory.thread;

import org.github.caishijun.eshop.inventory.request.ProductInventoryCacheRefreshRequest;
import org.github.caishijun.eshop.inventory.request.ProductInventoryDBUpdateRequest;
import org.github.caishijun.eshop.inventory.request.Request;
import org.github.caishijun.eshop.inventory.request.RequestQueue;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;

/**
 * 执行请求的工作线程
 */
public class RequestProcessorThread implements Callable<Boolean> {

    // 自己监控的内存队列
    private ArrayBlockingQueue<Request> queue;

    public RequestProcessorThread(ArrayBlockingQueue<Request> queue){
        this.queue = queue;
    }

    @Override
    public Boolean call() throws Exception {
        try{
            while (true){
                // ArrayBlockingQueue
                // Blocking（阻塞）就是说明，如果队列满了，或者是空的，那么都会在执行操作的时候，阻塞住
                Request request = queue.take();
                boolean forceRefresh = request.isForceRefresh();

                // 先做读请求的去重
                if (!forceRefresh) {
                    RequestQueue requestQueue = RequestQueue.getInstance();
                    Map<Integer, Boolean> flagMap = requestQueue.getFlagMap();

                    // 先做读并存redis请求的去重
                    /** 三种不同场景的处理：
                     *     1、更新数据库的写请求：将flagMap中flagMap.put(request.getProductId(),true)，将请求路由后放入到队列。
                     *     2、缓存刷新的请求（读并存redis请求）-在之前有对这个商品id的更新数据库的写请求（flagMap.get(request.getProductId())为true）：将标识修改为 false 表示队列中已经有读并存redis请求了，将请求路由后放入到队列。
                     *     3、缓存刷新的请求（读并存redis请求）-在之前有对这个商品id的更新数据库的写请求和读并存redis请求（flagMap.get(request.getProductId())为false）：直接 return ，忽略掉这个请求，不将请求放入到队列。
                     *     4、缓存刷新的请求（读并存redis请求）-在之前【没有】有对这个商品id的更新数据库的写请求和读并存redis请求（flagMap.get(request.getProductId())为null）：将标识修改为 false 表示队列中已经有读并存redis请求了，将请求路由后放入到队列。
                     */
                    if (request instanceof ProductInventoryDBUpdateRequest) {
                        // 如果是一个更新数据库的写请求，那么就将那个 productId 对应的标志设置为 true 。
                        flagMap.put(request.getProductId(), true);
                    } else if (request instanceof ProductInventoryCacheRefreshRequest) {
                        // 如果是缓存刷新的请求，那么就判断，如果标识不为空，而且是true，就说明之前有一个这个商品的数据库更新请求
                        Boolean flag = flagMap.get(request.getProductId());
                        if (flag != null && flag == true) {
                            flagMap.put(request.getProductId(), false);
                        }

                        // 如果 flag 是 null
                        if (flag == null) {
                            flagMap.put(request.getProductId(), false);
                        }

                        // 如果是缓存刷新的请求，而且发现标识不为空，但是标识是 false ，
                        // 说明前面已经有一个数据库更新请求和一个缓存刷新请求。
                        if (flag != null && flag == false) {
                            // 什么都不做直接 return ，
                            // 对于这种读并存redis请求，直接就过滤掉，不要放到后面的内存队列里面去了
                            return true;
                        }
                    }
                }

                // 执行这个 request 操作
                request.process();

                // 假如说，执行完了一个读请求之后，假设数据已经刷新到 redis 中了。
                // 但是后面可能 redis 中的数据会因为内存满了，被自动清理掉
                // 如果说数据从 redis 中被自动清理掉了以后
                // 然后后面又来一个读请求，此时如果进来，发现标志位是 false ，就不会去执行这个刷新的操作了
                // 所以在执行完这个读请求之后，实际上这个标志位是停留在 false 。
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
