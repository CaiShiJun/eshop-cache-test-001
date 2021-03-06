package org.github.caishijun.eshop.storm.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.trident.util.LRUMap;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.Utils;

/**
 * 商品访问次数统计bolt
 */
public class ProductCountBolt extends BaseRichBolt {

    private static final long serialVersionUID = -8761807561458126413L;

    private LRUMap<Long, Long> productCountMap = new LRUMap<Long, Long>(1000);

    public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
        new Thread(new ProductCountThread()).start();
    }

    private class ProductCountThread implements Runnable {

        public void run() {
            List<Map.Entry<Long, Long>> topnProductList = new ArrayList<Map.Entry<Long, Long>>();

            while(true) {
                topnProductList.clear();

                int topn = 3;

                for(Map.Entry<Long, Long> productCountEntry : productCountMap.entrySet()) {
                    if(topnProductList.size() == 0) {
                        topnProductList.add(productCountEntry);
                    } else {
                        // 比较大小，生成最热topn的算法有很多种
                        // 但是我这里为了简化起见，不想引入过多的数据结构和算法的的东西
                        // 很有可能还是会有漏洞，但是我已经反复推演了一下了，而且也画图分析过这个算法的运行流程了
                        boolean bigger = false;

                        for(int i = 0; i < topnProductList.size(); i++){
                            Map.Entry<Long, Long> topnProductCountEntry = topnProductList.get(i);

                            if(productCountEntry.getValue() > topnProductCountEntry.getValue()) {
                                int lastIndex = topnProductList.size() < topn ? topnProductList.size() - 1 : topn - 2;
                                for(int j = lastIndex; j >= i; j--) {
                                    topnProductList.set(j + 1, topnProductList.get(j));
                                }
                                topnProductList.set(i, productCountEntry);
                                bigger = true;
                                break;
                            }
                        }

                        if(!bigger) {
                            if(topnProductList.size() < topn) {
                                topnProductList.add(productCountEntry);
                            }
                        }
                    }
                }

                Utils.sleep(60000);
            }
        }

    }

    public void execute(Tuple tuple) {
        Long productId = tuple.getLongByField("productId");

        Long count = productCountMap.get(productId);
        if(count == null) {
            count = 0L;
        }
        count++;

        productCountMap.put(productId, count);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

}

