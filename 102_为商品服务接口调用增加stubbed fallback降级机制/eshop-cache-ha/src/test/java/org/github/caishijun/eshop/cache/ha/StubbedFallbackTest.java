package org.github.caishijun.eshop.cache.ha;

import org.github.caishijun.eshop.cache.ha.hystrix.command.GetProductInfoCommand;

public class StubbedFallbackTest {

    public static void main(String[] args) {
        GetProductInfoCommand getProductInfoCommand = new GetProductInfoCommand(-1L);
        System.out.println(getProductInfoCommand.execute());
    }

}
