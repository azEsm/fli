package ml.fli.services;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.utils.VkApi;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
public class MockUsersServiceImpl implements UsersService {
    @Override
    public FrontendResponse get(@Nonnull FrontendRequest request) {
        String userId = request.getUserId();
        String choiceSex = request.getSex();

        VkApi vkApi = new VkApi();
        String resultOneUser = vkApi.getUser(userId);

        return new FrontendResponse();
    }
}
