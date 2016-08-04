package ml.fli.controllers;

import ml.fli.db.models.User;
import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.models.VkApiParams;
import ml.fli.services.VkService;
import ml.fli.utils.FrontendResponseConverter;
import ml.fli.utils.JSONParser;
import ml.fli.utils.Klusterer;
import ml.fli.utils.UsersConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import weka.core.Instance;
import weka.core.Instances;

import java.util.HashSet;
import java.util.Set;

@Controller
public class TransferController {

    @Autowired
    private VkService vkApi;

    @MessageMapping("/process")
    @SendTo("/data/userList")
    public FrontendResponse get(FrontendRequest message) throws Exception {

        JSONParser parser = new JSONParser();
        System.out.println("start");
       // String searchingUserId = message.getUserId();
       // String searchingSex = message.getSex();

        //get vk data
        System.out.println("get");

        String searchingUserId = "7272824";
        String falseUserId = "";
        Set<User> usersList = new HashSet<>();
        //get vk data
        String resultOneUser = vkApi.getUser(searchingUserId);
        User oneUser = parser.parseUser(resultOneUser);
        Thread.sleep(300);
        String audioString = vkApi.getUserAudios(Integer.parseInt(searchingUserId), 20);
        Set<String> audioCollection = parser.parseAudio(audioString);
        oneUser.setAudio(audioCollection);
        Thread.sleep(300);
        String groupString = vkApi.getUserGroups(Integer.parseInt(searchingUserId), 20);
        Set<String> groups = parser.parseVKGroups(groupString);
        oneUser.setGroups(groups);
        usersList.add(oneUser);
        Thread.sleep(300);

        VkApiParams param = VkApiParams.create().add("count","100");
        String resultUsersList = vkApi.getUsersList(param);

        Set<User> resultVkApi = parser.parseUsers(resultUsersList);

        int index = 1;
        for (User user: resultVkApi) {
            System.out.println(index);
            String audioVkApi = vkApi.getUserAudios(user.getId(), 20);
            Thread.sleep(300);
            //System.out.println(audioVkApi);

            if (parser.errorManyRequest(audioVkApi)) {
                Thread.sleep(700);
                audioVkApi = vkApi.getUserAudios(user.getId(), 20);
            }
            if (parser.errorCaptchaNeeded(audioVkApi)) {
                Thread.sleep(45000);
                audioVkApi = vkApi.getUserAudios(user.getId(), 20);
            }
            if (!parser.errorAudioClose(audioVkApi)) {
                Set<String> usersAudio = parser.parseAudio(audioVkApi);
                //System.out.println(usersAudio);
                user.setAudio(usersAudio);
            }

            String groupVkApi = vkApi.getUserGroups(user.getId(), 20);
            //System.out.println(groupVkApi);
            Thread.sleep(300);
            if (parser.errorManyRequest(groupVkApi)) {
                Thread.sleep(700);
                groupVkApi = vkApi.getUserGroups(user.getId(), 20);
            }
            if (parser.errorCaptchaNeeded(groupVkApi)) {
                Thread.sleep(45000);
                groupVkApi = vkApi.getUserGroups(user.getId(), 20);
            }
            Set<String> userGroups = parser.parseVKGroups(groupVkApi);
            user.setGroups(userGroups);
            usersList.add(user);
            Thread.sleep(350);
            index++;
        }

        Instances dataSet = UsersConverter.usersToInstances(usersList);

        Klusterer cluster = new Klusterer();
        Set<Instance> result = cluster.FindCluster(dataSet, Integer.parseInt(searchingUserId));

        for (Instance inst: result)
            System.out.println(inst);

        return FrontendResponseConverter.instanceToResponse(result);

       // return ((MockProcessUsersServiceImpl) processUsersService).getVkApi();
    }

    @MessageExceptionHandler
    @SendTo("data/errors")
    public String handleException(Throwable exception) {
        System.out.println(exception.getMessage());
        return exception.getMessage();
    }

    @SendTo("data/errorUserExist")
    public String errorUserExist() {
        System.out.println("ErrorUsers");
        return "Такого пользователя не существует";
    }

    private FrontendResponse Cluster(User searchingUser, String Sex) throws Exception {
        System.out.println("startCluster");
        JSONParser parser = new JSONParser();
        Set<User> usersList = new HashSet<>();

        String audioString = vkApi.getUserAudios(searchingUser.getId(), 20);
        Set<String> audioCollection = parser.parseAudio(audioString);
        searchingUser.setAudio(audioCollection);

        String groupString = vkApi.getUserGroups(searchingUser.getId(), 20);
        Set<String> groups = parser.parseVKGroups(groupString);
        searchingUser.setGroups(groups);
        usersList.add(searchingUser);

        //get usersList
        VkApiParams param = VkApiParams.create().add("count","25").add("sex", Sex);
        String resultUsersList = vkApi.getUsersList(param);

        Set<User> resultVkApi = parser.parseUsers(resultUsersList);

        for (User user: resultVkApi) {
            String audioVkApi = vkApi.getUserAudios(user.getId(), 20);

            if (parser.errorManyRequest(audioVkApi)) {
                Thread.sleep(700);
                audioVkApi = vkApi.getUserAudios(user.getId(), 20);
            }
            if (parser.errorCaptchaNeeded(audioVkApi)) {
                Thread.sleep(45000);
                audioVkApi = vkApi.getUserAudios(user.getId(), 20);
            }
            if (!parser.errorAudioClose(audioVkApi)) {
                Set<String> usersAudio = parser.parseAudio(audioVkApi);
                user.setAudio(usersAudio);
            }

            String groupVkApi = vkApi.getUserGroups(user.getId(), 20);

            if (parser.errorManyRequest(groupVkApi)) {
                Thread.sleep(700);
                groupVkApi = vkApi.getUserGroups(user.getId(), 20);
            }
            if (parser.errorCaptchaNeeded(groupVkApi)) {
                Thread.sleep(45000);
                groupVkApi = vkApi.getUserGroups(user.getId(), 20);
            }
            Set<String> userGroups = parser.parseVKGroups(groupVkApi);
            user.setGroups(userGroups);
            usersList.add(user);
        }

        System.out.println("instance");
        Instances dataSet = UsersConverter.usersToInstances(usersList);

        System.out.println("cluster");
        Klusterer cluster = new Klusterer();
        Set<Instance> result = cluster.FindCluster(dataSet, searchingUser.getId());
        System.out.println("done");

        return FrontendResponseConverter.instanceToResponse(result);
    }
}
