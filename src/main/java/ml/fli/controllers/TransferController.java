package ml.fli.controllers;

import ml.fli.db.models.User;
import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
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
        String searchingUserId = "7272824";
        JSONParser parser = new JSONParser();
        Set<User> usersList = new HashSet<>();
        //get vk data

        System.out.println("get");
        for (int counter = 0; counter <=2; counter++){
            String resultUsersList = vkApi.executeUsers();
            if (resultUsersList != null){
                Set<User> tmp = parser.parseExecuteUsers(resultUsersList);
                usersList.addAll(tmp);
            }
        }
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
