package ml.fli.utils;

import ml.fli.models.FrontendResponse;
import weka.core.Instance;

import java.util.Random;
import java.util.Set;

/**
 * Created by Александр on 29.07.2016.
 */
public class FrontendResponseConverter {

    public static FrontendResponse instanceToResponse(Set<Instance> searchedData){
        FrontendResponse frontendResponse = new FrontendResponse();
        Set<FrontendResponse.Raw> userRaw = frontendResponse.getResult();

       // double rate = 0;
        for (Instance instance:searchedData){
            FrontendResponse.Raw resultUser = new FrontendResponse.Raw();

            resultUser.setAccountUrl("https://vk.com/id" + (int) instance.value(0));
            resultUser.setName(instance.stringValue(1) + " " + instance.stringValue(2));
            //FIXME
            if (instance.stringValue(8).equals(""))
                resultUser.setPhotoUrl("http://placehold.it/300x300/");
            else
                resultUser.setPhotoUrl(instance.stringValue(8));
            //
            resultUser.setRate(new Random().nextDouble());
            userRaw.add(resultUser);
        }


        return frontendResponse;
    }

}
