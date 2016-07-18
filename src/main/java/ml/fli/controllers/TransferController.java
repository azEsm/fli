package ml.fli.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import ml.fli.models.*;
import ml.fli.utils.JSONParser;
import ml.fli.utils.VkApi;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
public class TransferController {

    @MessageMapping("/get")
    @SendTo("/data/userList")
    public FrontendResponse get() throws Exception {
        //Thread.sleep(30000);
        //String userId = message.getUserId();
        //String choiceSex = message.getSex();

        String userId = "132154659";
        VkApi vkApi = new VkApi();
        String resultOneUser = vkApi.getUser(userId);

        JSONParser parser = new JSONParser();
        User oneUser = parser.parseUser(resultOneUser);
//
//        VkApiParams param = VkApiParams.create();
//        if (oneUser.getCity() instanceof String) {
//            param.add("city", oneUser.getCity());
//        }
//        if (oneUser.getBdate() instanceof String) {
//            String year = oneUser.getBdate();
//            if (year.length() > 5) {
//                Calendar calendar = Calendar.getInstance();
//
//                int age = calendar.get(Calendar.YEAR) - Integer.valueOf(year.substring(5));
//            }
//        }

        String userUrl = "vk.com/id" + userId;
        String userName = oneUser.getFirst_name() + " " + oneUser.getLast_name();
        String userPic = "https://pp.vk.me/c633221/v633221362/39410/MT0d_XiMpqs.jpg";
        int userWeight = 10;

        FrontendResponseOneUser result = new FrontendResponseOneUser(userUrl, userName, userPic, userWeight);
        ArrayList<FrontendResponseOneUser> userList = new ArrayList<>();
        userList.add(result);

        return new FrontendResponse(userList);
    }

}
