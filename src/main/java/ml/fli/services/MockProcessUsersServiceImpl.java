package ml.fli.services;

import ml.fli.db.models.User;
import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.models.VkApiParams;
import ml.fli.utils.Errors;
import ml.fli.utils.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.Set;

@Service
public class MockProcessUsersServiceImpl implements ProcessUsersService {
    @Autowired
    VkService vkApi;

    @Override
    public FrontendResponse process(@Nonnull FrontendRequest request) {
        return getSomeUsers();
    }

    public FrontendResponse getSomeUsers() {
        FrontendResponse result = new FrontendResponse();
        Set<FrontendResponse.Raw> rows = result.getResult();

        FrontendResponse.Raw first = new FrontendResponse.Raw();
        first.setPhotoUrl("https://pp.vk.me/c608629/v608629069/10ca/GcoqLLRzGo4.jpg");
        first.setAccountUrl("https://vk.com/id186928069");
        first.setName("Марк Горшков");
        first.setRate(0.0);
        rows.add(first);

        FrontendResponse.Raw second = new FrontendResponse.Raw();
        second.setPhotoUrl("https://pp.vk.me/c626120/v626120705/20501/kLe17uoUAG4.jpg");
        second.setAccountUrl("https://vk.com/id269611705");
        second.setName("Алексей Царапкин");
        second.setRate(0.1);
        rows.add(second);

        FrontendResponse.Raw third = new FrontendResponse.Raw();
        third.setPhotoUrl("https://pp.vk.me/c631929/v631929744/1e8ab/rz3_QHhI86E.jpg");
        third.setAccountUrl("https://vk.com/id347750744");
        third.setName("Виталий Горышев");
        third.setRate(0.3);
        rows.add(third);

        FrontendResponse.Raw forth = new FrontendResponse.Raw();
        forth.setPhotoUrl("https://pp.vk.me/c626119/v626119383/2899/kkAF3WOwEc0.jpg");
        forth.setAccountUrl("https://vk.com/sweetkina");
        forth.setName("Лена Ленская");
        forth.setRate(0.9);
        rows.add(forth);

        return result;
    }

    public FrontendResponse getVkApi() {
        FrontendResponse result = new FrontendResponse();
        VkApiParams param = VkApiParams.create();
        JSONParser parser = new JSONParser();
        try {
            String Users = vkApi.getUsersList(param);

            Set<User> vkApiResult = parser.parseUsers(Users);

            Set<FrontendResponse.Raw> rows = result.getResult();

            double rate = 0;
            for (User oneUser: vkApiResult) {
                rate += 0.1;
                FrontendResponse.Raw resultUser = new FrontendResponse.Raw();
                resultUser.setAccountUrl("https://vk.com/id" + oneUser.getId());
                resultUser.setName(oneUser.getFirst_name() + " " + oneUser.getLast_name());
                resultUser.setPhotoUrl(oneUser.getPhoto_400_orig());
                resultUser.setRate(rate);
                rows.add(resultUser);
            }

        } catch (Exception e) {
            throw Errors.asUnchecked(e);
        }

        return result;
    }
}