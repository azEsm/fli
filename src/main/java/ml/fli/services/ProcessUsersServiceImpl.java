package ml.fli.services;

import ml.fli.db.models.User;
import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.models.VkApiParams;
import ml.fli.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Instance;
import weka.core.Instances;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class ProcessUsersServiceImpl implements ProcessUsersService{

    @Autowired
    private VkService vkApi;

    private long userId;

    @Override
    public FrontendResponse process(@Nonnull FrontendRequest request) {

        String searchingUserId = request.getUserId();
        String searchingSex = request.getSex();

        if (searchingUserId.startsWith("id", 0) && isNumber(searchingUserId.substring(2))) {
            searchingUserId = searchingUserId.substring(2);
        }

        Set<User> usersList= resultVkApi(searchingUserId,searchingSex);

        Instances dataSet = UsersConverter.usersToInstances(usersList);

        FrontendResponse result = new FrontendResponse();
        Klusterer cluster = new Klusterer();
        try {
            Set<Instance> resultCluster = cluster.FindCluster(dataSet, this.userId);
            result = FrontendResponseConverter.instanceToResponse(resultCluster);
            Set<FrontendResponse.Raw> userList = result.getResult();
            if (userList.size() == 1) {
                for (FrontendResponse.Raw user : userList) {
                    user.setPhotoUrl("http://301-1.ru/gen-mems/img_mems/4a4c2a53661ede617bd7437b4e728cbb.jpg");
                }
            }
            if (userList.size() > 1) {
                Iterator<FrontendResponse.Raw> iterator = userList.iterator();
                while (iterator.hasNext()) {
                    FrontendResponse.Raw user = iterator.next();
                    if (user.getAccountUrl().equals("https://vk.com/id" + this.userId)) {
                        iterator.remove();
                    }
                }
            }
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
        try {
            String resultOneUser = vkApi.getUser(UserId);
            while (parser.presenseOfErrors(resultOneUser)) {
                if (parser.errorManyRequest(resultOneUser)) {
                    Thread.sleep(700);
                    resultOneUser = vkApi.getUser(UserId);
                }
                if (parser.errorCaptchaNeeded(resultOneUser)) {
                    Thread.sleep(45000);
                    resultOneUser = vkApi.getUser(UserId);
                }
            }
            //parse one user
            User searchingUser = parser.parseUser(resultOneUser);
            this.userId = searchingUser.getId();
            Thread.sleep(350);

            String audioString = vkApi.getUserAudios(searchingUser.getId(), 20);
            Set<String> audioCollection = parser.parseAudio(audioString);
            searchingUser.setAudio(audioCollection);
            Thread.sleep(350);

            String groupString = vkApi.getUserGroups(searchingUser.getId(), 20);
            Set<String> groups = parser.parseVKGroups(groupString);
            searchingUser.setGroups(groups);
            usersList.add(searchingUser);
            Thread.sleep(350);

            //get users list
            VkApiParams param = VkApiParams.create().add("count", "25").add("sex", Sex);
            String resultUsersList = vkApi.getUsersList(param);
            Thread.sleep(350);

            //parse users list
            Set<User> resultVkApi = parser.parseUsers(resultUsersList);

            for (User user : resultVkApi) {
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
                    } else {
                        break;
                    }
                }
                Thread.sleep(350);

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
                Thread.sleep(350);
            }
        }
        catch (NullPointerException npe) {
            throw Errors.asUnchecked(npe);
        }
        finally {
            return usersList;
        }
    }

    private boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }

}
