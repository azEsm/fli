package ml.fli;

import ml.fli.controllers.UsersController;
import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.models.Response;
import ml.fli.models.User;
import ml.fli.utils.JsonConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.JSONLoader;
import weka.core.converters.Loader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.*;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
public class SimpleTest {
    final static Logger logger = LoggerFactory.getLogger(SimpleTest.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsersController usersController;

    @Test
    public void dummyTest() throws Exception {
        Clusterer clusterer = new SimpleKMeans();
        assertNotNull(clusterer);
        assertNotNull(restTemplate);
    }


    @Test
    public void tfIdfTest() throws Exception {
        try (
                InputStream input = this.getClass().getClassLoader().getResourceAsStream("test.csv")
                //InputStream json = this.getClass().getClassLoader().getResourceAsStream("users.json")
        ) {
            //JSONLoader loader = new JSONLoader();
            //loader.setSource(json);

            Loader csvLoader = getCsvLoader(input);

            Instances dataRaw = csvLoader.getDataSet();
            logger.info("\n\nImported data: {}\n\n", dataRaw);

            StringToWordVector filter = getVectorizer();
            filter.setInputFormat(dataRaw);
            Instances dataFiltered = Filter.useFilter(dataRaw, filter);
            logger.info("\n\nFiltered data:{}\n\n", dataFiltered);

            SimpleKMeans clusterer = new SimpleKMeans();
            clusterer.setNumClusters(3);

            //можно попробовать разные функции расстояния. По умолчанию используется евклидово
            //clusterer.setDistanceFunction(new ManhattanDistance());

            clusterer.buildClusterer(dataFiltered);

            System.out.println("\n\nNumber of clusters: " + clusterer.numberOfClusters());
            for (Instance instance : dataFiltered) {
                System.out.println(instance.toString());
                System.out.println("Cluster: " + clusterer.clusterInstance(instance));
            }
        }
    }
    @Test
    public void tfIdfJSONTest() throws Exception{
        try(InputStream sourceFile = this.getClass().getClassLoader().getResourceAsStream("person.json")){
            Response response = JsonConverter.jsonToObject(sourceFile, Response.class);
            assertNotNull(response);
            assertNotNull(response.getResponse());
            List<User> users = response.getResponse().getItems();

            String json = JsonConverter.objectToJson(users);

            System.out.println(json);

            Loader loader = getJsonLoader(new ByteArrayInputStream(json.getBytes()));

            Instances dataRaw = loader.getDataSet();
            logger.info("\n\nImported data: {}\n\n", dataRaw);
        }

    }
    @Test
    public void jsonConverterTest() throws Exception {
        try (InputStream usersStream = this.getClass().getClassLoader().getResourceAsStream("person.json")) {
            Response response = JsonConverter.jsonToObject(usersStream, Response.class);
            assertNotNull(response);
            assertNotNull(response.getResponse());
            assertNotNull(response.getResponse().getItems());
            assertEquals(1, response.getResponse().getItems().size());
        }

        Response response = JsonConverter.jsonToObject(getUsersJson(), Response.class);
        assertNotNull(response);
        assertNotNull(response.getResponse());

        List<User> users = response.getResponse().getItems();
        assertNotNull(users);
        assertEquals(1, users.size());

        String json = JsonConverter.objectToJson(response);
        logger.info("Result:\n{}", json);
    }

    @Test
    public void testUsersController() {
        FrontendRequest request = new FrontendRequest();
        request.setUserId("123");
        request.setSex("1");
        FrontendResponse users = usersController.findUsers(request);
        assertNotNull(users);
    }

    private StringToWordVector getVectorizer() {
        StringToWordVector vectorizer = new StringToWordVector();
        vectorizer.setIDFTransform(true);
        vectorizer.setTFTransform(true);
        return vectorizer;
    }

    private Loader getCsvLoader(InputStream input) throws Exception{
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setSource(input);
        String[] options = new String[1];
        options[0] = "-H"; //without headers
        csvLoader.setOptions(options);

        return csvLoader;
    }
    private Loader getJsonLoader(InputStream input) throws Exception{
        JSONLoader jsonLoader = new JSONLoader();
        jsonLoader.setSource(input);

        return jsonLoader;
    }

    private String getUsersJson() {
        return "{\n" +
            "  \"response\": {\n" +
            "    \"count\": 2308981,\n" +
            "    \"items\": [\n" +
            "      {\n" +
            "        \"id\": 30429836,\n" +
            "        \"first_name\": \"Женя\",\n" +
            "        \"last_name\": \"Дубик\",\n" +
            "        \"sex\": 2,\n" +
            "        \"bdate\": \"18.11\",\n" +
            "        \"home_town\": \"Gabberland\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
    }
}
