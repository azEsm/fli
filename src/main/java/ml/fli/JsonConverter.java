package ml.fli;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by fab on 11.07.2016.
 */
public class JsonConverter {
    //commit
    public void run(){
        ObjectMapper mapper = new ObjectMapper();
        String filepath = System.getProperty("user.dir") +File.separator + "person.json";

        try{
            User user = mapper.readValue(new FileInputStream(filepath), User.class);
            String prettyPerson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(user);
            System.out.print(prettyPerson);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
