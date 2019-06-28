package pers.spring.demo.proxy;

import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-06-28 00:06
 **/
@Service
public class AspectJPServiceImpl implements ProxyService {

    @Override
    public ProxyMode invoke() {
        System.out.printf("proxy mode %s is invoke",ProxyMode.ASPECTJ);
        return ProxyMode.ASPECTJ;
    }
}
