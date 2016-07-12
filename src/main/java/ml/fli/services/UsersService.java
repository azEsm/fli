package ml.fli.services;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;

import javax.annotation.Nonnull;

public interface UsersService {
    /**
     * Получить список пользователей, сортированных по степени схожести с заданным
     * @param request
     * @return
     */
    FrontendResponse get(@Nonnull FrontendRequest request);
}
