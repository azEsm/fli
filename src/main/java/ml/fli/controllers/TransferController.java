package ml.fli.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import ml.fli.models.*;
import ml.fli.utils.JSONParser;
import ml.fli.utils.VkApi;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Calendar;
import java.util.List;

@Controller
public class TransferController {

    @MessageMapping("/get")
    @SendTo("/data/userList")
    public FrontendResponse get(FrontendRequest message) throws Exception {
        Thread.sleep(30000);
        String userId = message.getUserId();
        String choiceSex = message.getSex();

        VkApi vkApi = new VkApi();
        String resultOneUser = vkApi.getUser(userId);

        JSONParser parser = new JSONParser();
        User oneUser = parser.parseUser(resultOneUser);

        VkApiParams param = VkApiParams.create();
        if (oneUser.getCity() instanceof String) {
            param.add("city", oneUser.getCity());
        }
        if (oneUser.getBdate() instanceof String) {
            String year = oneUser.getBdate();
            if (year.length() > 4) {
                Calendar calendar = Calendar.getInstance();

                int age = calendar.get(Calendar.YEAR) - Integer.valueOf(year.substring(6));
            }

        }


        return new FrontendResponse();
    }

}
