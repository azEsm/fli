package ml.fli.services;

import ml.fli.db.models.User;
import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.models.VkApiParams;
import ml.fli.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import weka.core.Instance;
import weka.core.Instances;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Service
public class ProcessUsersServiceImpl implements ProcessUsersService{

    @Autowired
    private VkService vkApi;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ProcessUsersServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public FrontendResponse process(@Nonnull FrontendRequest request) {

        String searchingUserId = request.getUserId();
        String searchingSex = request.getSex();

        Set<User> usersList= resultVkApi(searchingUserId,searchingSex);

        Instances dataSet = UsersConverter.usersToInstances(usersList);

        FrontendResponse result = new FrontendResponse();
        Klusterer cluster = new Klusterer();
        try {
            Set<Instance> resultCluster = cluster.FindCluster(dataSet, Long.valueOf(searchingUserId));
            result = FrontendResponseConverter.instanceToResponse(resultCluster);
        } catch (Exception e) {
            throw Errors.asUnchecked(e);
        }
        finally {
            return result;
        }
    }

    private Set<User> resultVkApi(String UserId, String Sex) {
        JSONParser parser = new JSONParser();
        Set<User> usersList = new HashSet<>();

        //get one user
        String resultOneUser = vkApi.getUser(UserId);

        try {
            if (parser.errorUserExist(resultOneUser)) {
                errorUserExist();
            }
            else {
                //parse one user
                User searchingUser = parser.parseUser(resultOneUser);

                String audioString = vkApi.getUserAudios(searchingUser.getId(), 20);
                Set<String> audioCollection = parser.parseAudio(audioString);
                searchingUser.setAudio(audioCollection);

                String groupString = vkApi.getUserGroups(searchingUser.getId(), 20);
                Set<String> groups = parser.parseVKGroups(groupString);
                searchingUser.setGroups(groups);
                usersList.add(searchingUser);

                //get users list
                VkApiParams param = VkApiParams.create().add("count","25").add("sex", Sex);
                String resultUsersList = vkApi.getUsersList(param);

                //parse users list
                Set<User> resultVkApi = parser.parseUsers(resultUsersList);

                for (User user: resultVkApi) {
                    //get and parse audio
                    String audioVkApi = vkApi.getUserAudios(user.getId(), 20);

                    while (parser.presenseOfErrors(audioVkApi)) {
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
                        else {
                            break;
                        }
                    }

                    //get and parse group
                    String groupVkApi = vkApi.getUserGroups(user.getId(), 20);

                    while (parser.presenseOfErrors(groupVkApi)) {
                        if (parser.errorManyRequest(groupVkApi)) {
                            Thread.sleep(700);
                            groupVkApi = vkApi.getUserGroups(user.getId(), 20);
                        }
                        if (parser.errorCaptchaNeeded(groupVkApi)) {
                            Thread.sleep(45000);
                            groupVkApi = vkApi.getUserGroups(user.getId(), 20);
                        }
                    }
                    Set<String> userGroups = parser.parseVKGroups(groupVkApi);
                    user.setGroups(userGroups);

                    usersList.add(user);
                }
            }
        }
        catch (NullPointerException npe) {
            throw Errors.asUnchecked(npe);
        }
        finally {
            return usersList;
        }
    }

    @RequestMapping(path="/errorUserExist", method=POST)
    public void errorUserExist() {
        String text = "Пользователя с таким id не существует";
        this.messagingTemplate.convertAndSend("/queue/errorUserExist", text);
    }

}
