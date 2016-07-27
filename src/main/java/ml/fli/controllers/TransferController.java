package ml.fli.controllers;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.services.MockProcessUsersServiceImpl;
import ml.fli.services.ProcessUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TransferController {

    @Autowired
    private ProcessUsersService processUsersService;

    @MessageMapping("/process")
    @SendTo("/data/userList")
    public FrontendResponse get(FrontendRequest message) throws Exception {
        Thread.sleep(30);
        System.out.println("ssssssss");
        System.out.println(message.getUserId());
        System.out.println(message.getSex());

        return ((MockProcessUsersServiceImpl) processUsersService).getVkApi();
    }

}
