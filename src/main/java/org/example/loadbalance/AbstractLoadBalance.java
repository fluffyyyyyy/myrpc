package org.example.loadbalance;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance{
    @Override
    public String balance(List<String> addressList) {
        if (addressList == null || addressList.size() == 0) {
            return null;
        }
//        if (addressList.size() == 1) {
//            return addressList.get(0);
//        }
        return doLoad(addressList);
    }

    public abstract String doLoad(List<String> addressList);
}
