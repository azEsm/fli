package ml.fli.services;

import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;

import javax.annotation.Nonnull;

public interface ProcessUsersService {
    /**
     * Получить список пользователей, сортированных по степени схожести с заданным
     * @param request
     * @return
     */
    FrontendResponse process(@Nonnull FrontendRequest request);
}
