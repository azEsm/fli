package ml.fli.controllers;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.services.ProcessUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class TransferController {

    @Autowired
    private ProcessUsersService processUsersService;

    @MessageMapping("/process")
    @SendTo("/queue/userList")
    public FrontendResponse get(FrontendRequest message) throws Exception {
        System.out.println(message.getUserId());
        FrontendResponse result = processUsersService.process(message);
        System.out.println(result.getResult().size());
        return result;
    }

    @MessageExceptionHandler
    @SendTo("/queue/errors")
    public String handleException(Throwable exception) {
        System.out.println(exception.getMessage());
        return exception.getMessage();
    }

}
