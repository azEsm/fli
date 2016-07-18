package ml.fli.services;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.models.FrontendResponseOneUser;
import ml.fli.models.User;
import ml.fli.utils.JSONParser;
import ml.fli.utils.VkApi;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import javax.annotation.Nonnull;

@Service
public class MockUsersServiceImpl implements UsersService {
    @Override
    public FrontendResponse get(@Nonnull FrontendRequest request) {
        String userId = request.getUserId();
        String choiceSex = request.getSex();

        ArrayList<FrontendResponseOneUser> userList = new ArrayList<>();
        FrontendResponseOneUser user = new FrontendResponseOneUser();
        user.setUserName("Катя Клэп");
        user.setUserUrl("vk.com/id5592362");
        user.setUserPic("https:\\/\\/pp.vk.me\\/c633221\\/v633221362\\/39410\\/MT0d_XiMpqs.jpg");
        user.setWeight("10");
        userList.add(user);

        user.setUserName("Саша Спилберг");
        user.setUserUrl("vk.com/id169902419");
        user.setUserPic("https:\\/\\/pp.vk.me\\/c629217\\/v629217419\\/3acbf\\/FslTGsjOYI4.jpg");
        user.setWeight("5");
        userList.add(user);

        try {
            VkApi vkApi = new VkApi();
            String vkUser = vkApi.getUser(userId);
            JSONParser parser = new JSONParser();
            User resultUser = parser.parseUser(vkUser);

            user.setUserName(resultUser.getFirst_name() + " " + resultUser.getLast_name());
            user.setUserUrl("vk.com/id" + userId);
            user.setUserPic("https:\\/\\/pp.vk.me\\/c636420\\/v636420403\\/f914\\/-ULWasYdyNs.jpg");
            user.setWeight("3");
            userList.add(user);
        } catch (Exception e) {
            System.out.println("Ошибка! Невозможно получить данные о пользователе");
        }

        return new FrontendResponse(userList);
    }
}
