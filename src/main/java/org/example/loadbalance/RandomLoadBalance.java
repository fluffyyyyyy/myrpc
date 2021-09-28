package org.example.loadbalance;

import java.util.List;
import java.util.Random;

public class RandomLoadBalance extends AbstractLoadBalance{
    @Override
    public String doLoad(List<String> addressList) {
        Random random = new Random();
        int choose = random.nextInt(addressList.size());
        System.out.println("选择了第" + choose+ "台服务器");
        return addressList.get(choose);
    }
}
