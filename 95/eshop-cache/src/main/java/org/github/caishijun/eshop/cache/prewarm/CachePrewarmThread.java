package org.github.caishijun.eshop.cache.prewarm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.github.caishijun.eshop.cache.model.ProductInfo;
import org.github.caishijun.eshop.cache.service.CacheService;
import org.github.caishijun.eshop.cache.spring.SpringContext;
import org.github.caishijun.eshop.cache.zk.ZooKeeperSession;

/**
 * 缓存预热线程
 */
public class CachePrewarmThread extends Thread {

    @Override
    public void run() {
        CacheService cacheService = (CacheService) SpringContext.
                getApplicationContext().getBean("cacheService");
        ZooKeeperSession zkSession = ZooKeeperSession.getInstance();

        // 获取storm taskid列表
        String taskidList = zkSession.getNodeData("/taskid-list");

        System.out.println("【CachePrwarmThread获取到taskid列表】taskidList=" + taskidList);

        if(taskidList != null && !"".equals(taskidList)) {
            String[] taskidListSplited = taskidList.split(",");
            for(String taskid : taskidListSplited) {
                String taskidLockPath = "/taskid-lock-" + taskid;

                // 如果获取不到 taskidLockPath 的锁，说明该 taskidLockPath 已经有其他应用在预热了，就可以跳过本轮循环。
                boolean result = zkSession.acquireFastFailedDistributedLock(taskidLockPath);
                if(!result) {
                    continue;
                }

                String taskidStatusLockPath = "/taskid-status-lock-" + taskid;
                zkSession.acquireDistributedLock(taskidStatusLockPath);

                String taskidStatus = zkSession.getNodeData("/taskid-status-" + taskid);
                System.out.println("【CachePrewarmThread获取task的预热状态】taskid=" + taskid + ", status=" + taskidStatus);

                if("".equals(taskidStatus)) {
                    String productidList = zkSession.getNodeData("/task-hot-product-list-" + taskid);
                    System.out.println("【CachePrewarmThread获取到task的热门商品列表】productidList=" + productidList);
                    JSONArray productidJSONArray = JSONArray.parseArray(productidList);

                    for(int i = 0; i < productidJSONArray.size(); i++) {
                        Long productId = productidJSONArray.getLong(i);
                        String productInfoJSON = "{\"id\": " + productId + ", \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modifiedTime\": \"2017-01-01 12:00:00\"}";
                        ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
                        cacheService.saveProductInfo2LocalCache(productInfo);
                        System.out.println("【CachePrwarmThread将商品数据设置到本地缓存中】productInfo=" + productInfo);
                        cacheService.saveProductInfo2ReidsCache(productInfo);
                        System.out.println("【CachePrwarmThread将商品数据设置到redis缓存中】productInfo=" + productInfo);
                    }

                    zkSession.createNode("/taskid-status-" + taskid);
                    zkSession.setNodeData("/taskid-status-" + taskid, "success");
                }

                zkSession.releaseDistributedLock(taskidStatusLockPath);

                zkSession.releaseDistributedLock(taskidLockPath);
            }
        }
    }

}
