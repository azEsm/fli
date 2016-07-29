package ml.fli.controllers;

import com.wordnik.swagger.annotations.ApiOperation;
import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.services.MockProcessUsersServiceImpl;
import ml.fli.services.ProcessUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/users", produces = APPLICATION_JSON_VALUE)
public class UsersController {

    @Autowired
    private ProcessUsersService processUsersService;

    @ApiOperation("Получить сортированный список пользователей")
    @RequestMapping(value = "/get", method = {GET, POST})
    public FrontendResponse findUsers(@RequestBody FrontendRequest request) {
        return processUsersService.process(request);
    }

    /**
     * Метод нужен для отладки.
     * @return
     */
    @ApiOperation("Проверка доступности контроллера")
    @RequestMapping(value = "/test", method = {GET, POST})
    public FrontendResponse test() {
        return ((MockProcessUsersServiceImpl) processUsersService).getSomeUsers();
    }
}
