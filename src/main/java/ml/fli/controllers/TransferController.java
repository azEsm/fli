package ml.fli.controllers;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.models.User;
import ml.fli.services.MockUsersServiceImpl;
import ml.fli.services.UsersService;
import ml.fli.utils.JSONParser;
import ml.fli.utils.VkApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;

@Controller
public class TransferController {

    @Autowired
    private UsersService usersService;

    @MessageMapping("/get")
    @SendTo("/data/userList")
    public FrontendResponse get(FrontendRequest message) throws Exception {
        Thread.sleep(30000);
/*        String userId = message.getUserId();
        String choiceSex = message.getSex();

        VkApi vkApi = new VkApi();
        String resultOneUser = vkApi.getUser(userId);

        JSONParser parser = new JSONParser();
        User oneUser = parser.parseUser(resultOneUser);
//*/
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

/*        String userUrl = "vk.com/id" + userId;
        String userName = oneUser.getFirst_name() + " " + oneUser.getLast_name();
        String userPic = "https://pp.vk.me/c633221/v633221362/39410/MT0d_XiMpqs.jpg";
        int userWeight = 10;*/

        return ((MockUsersServiceImpl) usersService).getSomeUsers();
    }

}
