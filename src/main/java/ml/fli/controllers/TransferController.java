package ml.fli.controllers;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.services.MockUsersServiceImpl;
import ml.fli.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TransferController {

    @Autowired
    private UsersService usersService;

    @MessageMapping("/get")
    @SendTo("/data/userList")
    public FrontendResponse get(FrontendRequest message) throws Exception {
        Thread.sleep(30);

        return ((MockUsersServiceImpl) usersService).getSomeUsers();
    }

}
