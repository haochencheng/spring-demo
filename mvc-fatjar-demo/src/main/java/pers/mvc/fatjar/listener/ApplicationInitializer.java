package pers.mvc.fatjar.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-04 23:00
 **/
@Component
public class ApplicationInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            System.out.println("=====ContextRefreshedEvent");
        }
    }
}
