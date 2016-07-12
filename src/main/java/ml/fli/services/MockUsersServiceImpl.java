package ml.fli.services;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
public class MockUsersServiceImpl implements UsersService {
    @Override
    public FrontendResponse get(@Nonnull FrontendRequest request) {
        return new FrontendResponse();
    }
}
