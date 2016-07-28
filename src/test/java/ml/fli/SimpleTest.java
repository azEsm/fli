package ml.fli;


import com.google.common.base.Strings;
import ml.fli.controllers.UsersController;
import ml.fli.db.models.User;
import ml.fli.models.FrontendRequest;
import ml.fli.models.FrontendResponse;
import ml.fli.models.Response;
import ml.fli.models.VkApiParams;
import ml.fli.services.VkService;
import ml.fli.utils.JSONParser;
import ml.fli.utils.JsonConverter;
import ml.fli.utils.UsersConverter;
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
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.JSONLoader;
import weka.core.converters.Loader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
public class SimpleTest {
    final static Logger logger = LoggerFactory.getLogger(SimpleTest.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsersController usersController;

    @Autowired
    VkService vkApi;

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
    public void usersClassificationTest() throws Exception {
        Set<User> users = buildSomeUsers();

        Instances rowData = UsersConverter.usersToInstances(users);
        logger.info("\n\nImported data: \n{}\n\n", rowData);

        EM clusterer = buildEMClusterer();

        StringToWordVector filter = getVectorizer();
        filter.setInputFormat(rowData);
        Instances dataFiltered = Filter.useFilter(rowData, filter);
        logger.info("\n\nFiltered data: \n{}\n\n", dataFiltered);

        clusterer.buildClusterer(dataFiltered);

        System.out.println("\n\nNumber of clusters: " + clusterer.numberOfClusters());
        for (Instance instance : dataFiltered) {
            System.out.println(instance.toString());
            System.out.println("Cluster: " + clusterer.clusterInstance(instance));
        }

//        ClusterEvaluation evaluation = new ClusterEvaluation();
//        evaluation.setClusterer(clusterer);
//        evaluation.evaluateClusterer();
    }

    private EM buildEMClusterer() throws Exception {
        EM clusterer = new EM();
        clusterer.setNumClusters(4);

        String[] options = new String[2];
        options[0] = "-I";// max. iterations
        options[1] = "100";

        clusterer.setOptions(options);
        return clusterer;
    }

    private Set<User> buildSomeUsers() {
        Set<User> users = new HashSet<>();

        User user1 = new User();
        user1.setBdate("01.01.1999");
        user1.setCity("Penza");
        user1.setFirst_name("Anton");
        user1.setLast_name("Kolobok");
        user1.setSex("1");
        //
        User user2 = new User();
        user2.setBdate("22.06.1988");
        user2.setCity("Penza");
        user2.setFirst_name("Asad");
        user2.setLast_name("Posget");
        user2.setSex("2");

        User user3 = new User();
        user3.setBdate("21.03.1981");
        user3.setCity("Penza");
        user3.setFirst_name("Asadgdsd");
        user3.setLast_name("Posggdset");
        user3.setSex("2");

        User user4 = new User();
        user4.setBdate("02.02.1918");
        user4.setCity("Penza");
        user4.setFirst_name("Asssfad");
        user4.setLast_name("Pogfdsget");
        user4.setSex("1");

        User user5 = new User();
        user5.setBdate("12.05.1928");
        user5.setCity("Penza");
        user5.setFirst_name("Awerwesad");
        user5.setLast_name("Posgeetdgdt");
        user5.setSex("1");

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);

        return users;
    }

    @Test
    public void tfIdfJSONTest() throws Exception {
        try (InputStream sourceFile = this.getClass().getClassLoader().getResourceAsStream("person.json")) {
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
    public void vkApiExecuteTest() throws Exception {
        String listId = "90601787,63596945,7708047,1545367,32048608,5273940,4501721,64129056,61245909,113393454,52014407,11329613";
        String vkApiResult = vkApi.executeAudioAndGroup(listId);
        logger.info("\nResult:\n{}", vkApiResult);
    }

    @Test
    public void parserExecute() throws Exception {
//        String request = "https://api.vk.com/method/execute.GetAudioAndGroup" +
//                "?user=2683946,3925445,182200279,98506897,161060811,421848,102544966,6773527,11100369,1420471,68143899,66437505" +
//                "&access_token=03ffa07f3a99fee7ce0bad8851ec004f7a516b48da91b4c02c7239aaa4acca65f90a5d5e918e5f19a0e1b&v=5.53";
//
//        RestTemplate restTemplate = new RestTemplate();
//        String result = restTemplate.getForObject(request,String.class);
//        logger.info("\nResult:\n{}", result);

//        String listId = "2683946,3925445,182200279,98506897,161060811,421848,102544966,6773527,11100369,1420471,68143899,66437505";
        JSONParser parser = new JSONParser();

        VkApiParams param = VkApiParams.create().add("count", "24");
        String vkApiExecute = vkApi.getUsersList(param);

        Set<User> usersList = parser.parseUsers(vkApiExecute);
        logger.info("\nResult:\n{}", usersList.size());
//        JSONParser parser = new JSONParser();
//        Set<User> userList = parser.parseExecuteUsers(vkApiExecute);
//        logger.info("\nResult:\n{}", vkApiExecute);
    }

    @Test
    public void vkApiGetUserTest() throws Exception {
        String userId = "5592362";
        String resultOneUser = vkApi.getUser(userId);
        assertNotNull(resultOneUser);
        logger.info("\nUserData: {}\n", resultOneUser);
    }

    @Test
    public void vkApiGetUsersListTest() throws Exception {
        VkApiParams params = VkApiParams.create();
        params.add("count","10");
        String resultUsersList = vkApi.getUsersList(params);
        logger.info("\nUsersList: {}\n", resultUsersList);
    }

    @Test
    public void vkApiExecuteUsersListTest() throws Exception {
        String resultUsersList = vkApi.executeUsers();
        logger.info("\nUsersList: {}\n", resultUsersList);
    }

    @Test
    public void JSONParserExecuteListParse() throws Exception {
        String resultUsersList = vkApi.executeUsers();

        JSONParser parser = new JSONParser();
        Set<User> usersList = parser.parseExecuteUsers(resultUsersList);

        assertEquals(12,usersList.size());
        for(User user : usersList) {
            assertNotNull(user.getId());
            assertNotNull(user.getFirst_name());
            assertNotNull(user.getLast_name());
            assertNotNull(user.getSex());
        }
    }

    @Test
    public void JSONParserUsersListParse() throws Exception {
        try (InputStream sourceFile = this.getClass().getClassLoader().getResourceAsStream("UsersListData.json")) {
            int data = sourceFile.read();
            char content;
            String jsonString = "";
            while (data != -1) {
                content = (char) data;
                jsonString += content;
                data = sourceFile.read();
            }
            logger.info("\nJsonString: {}\n", jsonString);

            JSONParser parser = new JSONParser();
            Set<User> usersList = parser.parseUsers(jsonString);

            assertEquals(10,usersList.size());
        }
    }

    @Test
    public void JSONParserOneUserParse() throws Exception {
        try (InputStream sourceFile = this.getClass().getClassLoader().getResourceAsStream("UserData.json")) {
            int data = sourceFile.read();
            char content;
            String jsonString = "";
            while(data != -1) {
                content = (char) data;
                jsonString += content;
                data = sourceFile.read();
            }
            logger.info("\nJsonString: {}\n", jsonString);

            JSONParser parser = new JSONParser();

            User parseUser = parser.parseUser(jsonString);
            assertNotNull(parseUser);
            assertNotNull(parseUser.getId());
            assertNotNull(parseUser.getFirst_name());
            assertNotNull(parseUser.getLast_name());
            assertNotNull(parseUser.getCity());
            assertNotNull(parseUser.getAbout());
            assertNotNull(parseUser.getBooks());
            assertNotNull(parseUser.getMovies());
            assertNotNull(parseUser.getActivities());
            assertNotNull(parseUser.getBdate());
            assertNotNull(parseUser.getGames());
            assertNotNull(parseUser.getInterests());
            assertNotNull(parseUser.getMusic());
            assertNotNull(parseUser.getSex());
            logger.info("\n\nUserFirstName: {}\n\n", parseUser.getFirst_name());
        }
    }

    @Test
    public void vkApiTest() throws Exception {
        String userId = "5592362";

        String resultOneUser = vkApi.getUser(userId);

        JSONParser parser = new JSONParser();
        User oneUser = parser.parseUser(resultOneUser);
        logger.info("\nResultUser:\n{}", oneUser.getId() + " " + oneUser.getFirst_name() + " "
                + oneUser.getLast_name() + " " + oneUser.getSex() + " " + oneUser.getBdate() + " " + oneUser.getCity());
        logger.info("\nResultOneUser:\n{}", resultOneUser);

        String resultGroup = vkApi.getUserGroups(132154659,10);
        logger.info("\nResultGroup:\n{}", resultGroup);
        String resultAudio = vkApi.getUserAudios(132154659,10);
        logger.info("\nResultAudio:\n{}", resultAudio);

        String choiceSex = "1";
        VkApiParams param = VkApiParams.create();
        if (!Strings.isNullOrEmpty(oneUser.getCity())) {
            param.add("city", oneUser.getCity());
        }
        if (!Strings.isNullOrEmpty(oneUser.getBdate())) {
            String year = oneUser.getBdate();
            if (year.length() > 5) {
                Calendar calendar = Calendar.getInstance();

                int age = calendar.get(Calendar.YEAR) - Integer.valueOf(year.substring(5));
                param.add("age", String.valueOf(age));
            }
        }
        param.add("sex", choiceSex);
        param.add("count", "100");
        String resultUserList = vkApi.getUsersList(param);
        Set<User> listUsers = parser.parseUsers(resultUserList);
        logger.info("\nResult:\n{}", listUsers.size());
        logger.info("\nResultUserList:\n{}", resultUserList);
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

    @Test
    public void usersLoaderTest() throws Exception {
        Set<User> users = buildUsers();
        Instances instances = UsersConverter.usersToInstances(users);
        assertNotNull(instances);
        assertEquals(users.size(), instances.size());
    }

    private Set<User> buildUsers() {
        User user = new User();
        user.setId(123);
        user.setFirst_name("Иван");
        user.setLast_name("Петров");
        user.setCity("456");
        user.setSex("3");
        user.setBdate("2016");
        Set<User> result = new HashSet<>();
        result.add(user);

        return result;
    }

    private StringToWordVector getVectorizer() {
        StringToWordVector vectorizer = new StringToWordVector();
        vectorizer.setIDFTransform(true);
        vectorizer.setTFTransform(true);
        return vectorizer;
    }

    private Loader getCsvLoader(InputStream input) throws Exception {
        CSVLoader csvLoader = new CSVLoader();
        csvLoader.setSource(input);
        String[] options = new String[1];
        options[0] = "-H"; //without headers
        csvLoader.setOptions(options);

        return csvLoader;
    }

    private Loader getJsonLoader(InputStream input) throws Exception {
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
