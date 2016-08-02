package ml.fli.controllers;

import ml.fli.db.models.User;
import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.models.VkApiParams;
import ml.fli.services.ProcessUsersService;
import ml.fli.services.VkService;
import ml.fli.utils.FrontendResponseConverter;
import ml.fli.utils.JSONParser;
import ml.fli.utils.Klusterer;
import ml.fli.utils.UsersConverter;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProcessUsersService processUsersService;
    @Autowired
    private VkService vkApi;

    @MessageMapping("/process")
    @SendTo("/data/userList")

    public FrontendResponse get(FrontendRequest message) throws Exception {

        System.out.println("start");
        String searchingUserId = message.getUserId();
        JSONParser parser = new JSONParser();
        Set<User> usersList = new HashSet<>();

        //get vk data
        System.out.println("get");
        //get one user
        String resultOneUser = vkApi.getUser(searchingUserId);

        User oneUser = parser.parseUser(resultOneUser);

        String audioString = vkApi.getUserAudios(Integer.parseInt(searchingUserId), 20);
        Set<String> audioCollection = parser.parseAudio(audioString);
        oneUser.setAudio(audioCollection);

        String groupString = vkApi.getUserGroups(Integer.parseInt(searchingUserId), 20);
        Set<String> groups = parser.parseVKGroups(groupString);
        oneUser.setGroups(groups);
        usersList.add(oneUser);

        //get usersList
        VkApiParams param = VkApiParams.create().add("count","1000").add("sex", message.getSex());
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
        /*for (int counter = 0; counter <=2; counter++){
            String resultUsersList = vkApi.executeUsers();
            if (resultUsersList != null){
                Set<User> tmp = parser.parseExecuteUsers(resultUsersList);
                usersList.addAll(tmp);
            }
        }*/
        System.out.println("instance");
        Instances dataSet = UsersConverter.usersToInstances(usersList);

        System.out.println("cluster");
        Klusterer cluster = new Klusterer();
        Set<Instance> result = cluster.FindCluster(dataSet, Integer.parseInt(searchingUserId));
        System.out.println("done");
        return FrontendResponseConverter.instanceToResponse(result);

       // return ((MockProcessUsersServiceImpl) processUsersService).getVkApi();
    }

}
