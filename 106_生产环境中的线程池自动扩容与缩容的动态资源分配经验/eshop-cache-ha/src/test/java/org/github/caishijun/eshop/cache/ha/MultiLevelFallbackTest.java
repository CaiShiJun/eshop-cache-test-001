package org.github.caishijun.eshop.cache.ha;

import org.github.caishijun.eshop.cache.ha.hystrix.command.GetProductInfoCommand;

public class MultiLevelFallbackTest {

    public static void main(String[] args) throws Exception {
        GetProductInfoCommand getProductInfoCommand0 = new GetProductInfoCommand(-0L);
        System.out.println(getProductInfoCommand0.execute());
        System.out.println("--------------------------------------------------------------------------------------");
        GetProductInfoCommand getProductInfoCommand1 = new GetProductInfoCommand(-1L);
        System.out.println(getProductInfoCommand1.execute());
        System.out.println("--------------------------------------------------------------------------------------");
        GetProductInfoCommand getProductInfoCommand2 = new GetProductInfoCommand(-2L);
        System.out.println(getProductInfoCommand2.execute());

    }

}
