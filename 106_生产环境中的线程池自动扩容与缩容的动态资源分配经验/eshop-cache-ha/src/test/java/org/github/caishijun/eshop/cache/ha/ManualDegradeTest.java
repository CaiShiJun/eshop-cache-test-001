package org.github.caishijun.eshop.cache.ha;

import org.github.caishijun.eshop.cache.ha.degrade.IsDegrade;
import org.github.caishijun.eshop.cache.ha.hystrix.command.GetProductInfoFacadeCommand;

public class ManualDegradeTest {

    public static void main(String[] args) throws Exception {
        GetProductInfoFacadeCommand getProductInfoFacadeCommand0 = new GetProductInfoFacadeCommand(1L);
        System.out.println("getProductInfoFacadeCommand0:" + getProductInfoFacadeCommand0.execute());

        System.out.println("------------------------------------------------------------------------");

        GetProductInfoFacadeCommand getProductInfoFacadeCommand1 = new GetProductInfoFacadeCommand(1L);
        System.out.println("getProductInfoFacadeCommand1:" + getProductInfoFacadeCommand1.execute());

        System.out.println("------------------------------------------------------------------------");

        IsDegrade.setDegrade(true);
        GetProductInfoFacadeCommand getProductInfoFacadeCommand2 = new GetProductInfoFacadeCommand(1L);
        System.out.println("getProductInfoFacadeCommand2:" + getProductInfoFacadeCommand2.execute());
    }

}
