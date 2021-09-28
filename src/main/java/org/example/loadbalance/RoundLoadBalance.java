package org.example.loadbalance;

import java.util.List;

public class RoundLoadBalance extends AbstractLoadBalance{
    private int choose = -1;

    @Override
    public String doLoad(List<String> addressList) {
        choose++;
        choose = choose % addressList.size();
        System.out.println("选择了第" + choose+ "台服务器");
        return addressList.get(choose);
    }
}
