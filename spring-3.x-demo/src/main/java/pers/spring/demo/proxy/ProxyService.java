package pers.spring.demo.proxy;


public interface ProxyService {

    ProxyMode invoke();

    enum ProxyMode {
        PROXY,
        ASPECTJ
    }


}
