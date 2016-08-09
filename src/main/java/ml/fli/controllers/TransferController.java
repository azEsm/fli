package ml.fli.controllers;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.services.ProcessUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class TransferController {

    @Autowired
    private ProcessUsersService processUsersService;

    @MessageMapping("/process")
    @SendToUser("/queue/userList")
    public FrontendResponse get(FrontendRequest message) throws Exception {
        FrontendResponse result = processUsersService.process(message);
        return result;
    }

    @MessageExceptionHandler
    @SendToUser(destinations = "/queue/errors", broadcast = false)
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}
