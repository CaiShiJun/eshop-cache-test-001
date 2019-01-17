package org.github.caishijun.eshop.inventory.service;

import org.github.caishijun.eshop.inventory.request.Request;

/**
 * 请求异步执行的service
 */
public interface RequestAsyncProcessService {

    void process(Request request);
}
