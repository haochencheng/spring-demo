package controller;

import annonation.Autowired;
import annonation.Controller;
import model.User;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-11-15 18:29
 **/
@Controller
public class EchoController {

    @Autowired(name = "user")
    private User user;

    public String echo(){
        return user.toString();
    }

}
