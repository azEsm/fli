package ml.fli;

import ml.fli.controllers.UsersController;
import ml.fli.models.*;
import ml.fli.utils.JSONParser;
import ml.fli.utils.DataGenerator;
import ml.fli.utils.JsonConverter;
import ml.fli.utils.UsersConverter;
import ml.fli.utils.VkApi;
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
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.JSONLoader;
import weka.core.converters.Loader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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

    @Test
    public void dummyTest() throws Exception {
        Clusterer clusterer = new SimpleKMeans();
        assertNotNull(clusterer);
        assertNotNull(restTemplate);
    }

    @Test
    // on deck of cards
    public void ClusteringTest() throws Exception {

        Instances data = DataGenerator.Generate();
        data.attribute(0).setWeight(0.0);
        data.attribute(1).setWeight(1.0);

        SimpleKMeans clusterer = new SimpleKMeans();
        clusterer.setNumClusters(4);
        clusterer.buildClusterer(data);

        //region output
        double[] arr = clusterer.getClusterSizes();
        for (int i = 0; i <= arr.length - 1; i++) {
            System.out.println("Cluster #" + Integer.toString(i) + ":" + Double.toString(arr[i]));
        }
        System.out.println("Error " + Double.toString(clusterer.getSquaredError()));

        Set<Instance> firstCluster = new HashSet<>();
        Set<Instance> secondCluster = new HashSet<>();
        Set<Instance> thirdCluster = new HashSet<>();
        Set<Instance> fourCluster = new HashSet<>();

        for (Instance instance : data) {
            if (clusterer.clusterInstance(instance) == 0) {
                firstCluster.add(instance);
            }
            if (clusterer.clusterInstance(instance) == 1) {
                secondCluster.add(instance);
            }
            if (clusterer.clusterInstance(instance) == 2) {
                thirdCluster.add(instance);
            }
            if (clusterer.clusterInstance(instance) == 3) {
                fourCluster.add(instance);
            }
        }


        System.out.println("first");
        for (Instance instance : firstCluster) {
            System.out.print(instance + " ");
        }
        System.out.println();
        System.out.println("second");
        for (Instance instance : secondCluster) {
            System.out.print(instance + " ");
        }
        System.out.println();
        System.out.println("third");
        for (Instance instance : thirdCluster) {
            System.out.print(instance + " ");
        }
        System.out.println();
        System.out.println("four");
        for (Instance instance : fourCluster) {
            System.out.print(instance + " ");
        }
        System.out.println();


        //endregion

        double error = 1.0;
        int n = 2;
        while (error != 0.0) {
            SimpleKMeans cluster = new SimpleKMeans();

            cluster.setNumClusters(n);
            cluster.buildClusterer(data);

            System.out.println(cluster.getNumClusters());

            error = cluster.getSquaredError();
            System.out.println(error);
            n++;
        }

    }

    @Test
    // on some mock-users
    public void ClusteringUserTest() throws Exception {

        //OMG
        User u1 = new User(30429836, "Женя", "Дубик", "2", "18.11", "Gabberland");
        User u2 = new User(32955433, "Алексей", "Митенев", "2", "23.11.1992", "Тюмень");
        User u3 = new User(32955433, "Кристина", "Клайд ", "1", "1.4", "Братск");
        User u4 = new User(34545743, "asfd", "sdf", "1", "", "agfadg");
        User u5 = new User(1345645, "fd", "SDFASF", "2", "", "aggadg");
        User u6 = new User(13654, "ASDFASF", "dfbsv", "2", "", "agdfg");
        User u7 = new User(23564765, "adfadg", "agdf", "2", "", "agfgadg");
        Set<User> users = new HashSet<User>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        users.add(u5);
        users.add(u6);
        users.add(u6);
        users.add(u7);

        Instances dataSet = UsersConverter.usersToInstances(users);


        //tfidf
        StringToWordVector filter = getVectorizer();
        filter.setInputFormat(dataSet);
        Instances dataFiltered = Filter.useFilter(dataSet, filter);

        // some evaluation
        double error = 0.1;
        int numClusters = 2;
        while (true) {
            SimpleKMeans clusterer = new SimpleKMeans();
            clusterer.setNumClusters(numClusters);
            clusterer.buildClusterer(dataFiltered);


            //output
            System.out.println("# clusters: " + Integer.toString(numClusters));
            double[] arr = clusterer.getClusterSizes();
            System.out.println("Cluster sizes");
            for (int i = 0; i <= arr.length - 1; i++)
                System.out.println("\tCluster #" + Integer.toString(i) + ":" + Double.toString(arr[i]));
            System.out.println("Error " + Double.toString(clusterer.getSquaredError()));

            for (Instance instance : dataFiltered) {
                System.out.println("Cluster: " + clusterer.clusterInstance(instance));
                System.out.println(instance.toString());
            }
            System.out.println("#####################################");
            numClusters++;
            if (numClusters >= users.size())
                break;

        }

    }

    @Test
    // on real vk users
    public void ClusterVKUserTest() throws Exception {
        String searchingUserId = "7272824";

        //get vk data
        VkApi vkApi = new VkApi();

        String resultOneUser = vkApi.getUser(searchingUserId);
        JSONParser parser = new JSONParser();
        User oneUser = parser.parseUser(resultOneUser);

        String choiceSex = "2"; //TODO sexEnum
        VkApiParams param = VkApiParams.create();
        if (oneUser.getCity() instanceof String) {
            param.add("city", "1");
        }
        if (oneUser.getBdate() instanceof String) {
            String year = oneUser.getBdate();
            if (year.length() > 5) {
                Calendar calendar = Calendar.getInstance();

                int age = calendar.get(Calendar.YEAR) - Integer.valueOf(year.substring(4));
                param.add("age", String.valueOf(age));
            }
        }
        param.add("sex", choiceSex);
        String resultUserList = vkApi.getUsersList(param);
        List<User> listUsers = parser.parseUsers(resultUserList);
        listUsers.add(oneUser);

        // clusterer evaluation
        Instances dataSet = UsersConverter.usersToInstances(new HashSet<User>(listUsers));
        //StringToWordVector filter = getVectorizer();

        StringToWordVector filter = new StringToWordVector();
        filter.setAttributeIndicesArray(new int[]{1, 2, 3, 4, 5});
        filter.setInputFormat(dataSet);
        filter.setIDFTransform(true);
        filter.setTFTransform(true);

        Instances dataFiltered = Filter.useFilter(dataSet, filter);

        Instance tmp = dataFiltered.instance(0);
        System.out.println(tmp);
        int tmpID = (int)tmp.value(0);


        double error = 0.0;
        int numClusters = 2;
        //that's how we roll!

        Random rnd = new Random();
        int randomAccuracy = rnd.nextInt((5 - 3) + 1) + 3; // 3-5
        while (true) {
            SimpleKMeans clusterer = new SimpleKMeans();
            clusterer.setNumClusters(numClusters);

            clusterer.buildClusterer(dataFiltered);

            double thisError = clusterer.getSquaredError();
            if (numClusters == 2) {
                error = thisError;
                numClusters++;
                continue;
            }

            System.out.println("current n:" + numClusters);
            System.out.println("Current error: " + thisError);
            System.out.println("Cluster sizes");
            double[] arr = clusterer.getClusterSizes();
            for (int i = 0; i <= arr.length - 1; i++) {
                System.out.println("\tCluster #" + Integer.toString(i) + ":" + Double.toString(arr[i]));
            }
            double errorDiff = error - thisError;
            System.out.println("Error diff: " + errorDiff);

            if (numClusters== 13) {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                //find cluster with searced user
                int searchedClustererIndex = 0;
                for (Instance user : dataFiltered) {
                    if ( (int)user.value(0) == Integer.parseInt(searchingUserId)) {
                        searchedClustererIndex = clusterer.clusterInstance(user);
                    }
                }

                List<Integer> findedUSers = new ArrayList<Integer>();

                for (Instance instance : dataFiltered) {
                    if (clusterer.clusterInstance(instance) == searchedClustererIndex) {
                        findedUSers.add((int)instance.value(0));
                    }
                }

                Set<Instance> thisData = new HashSet<Instance>();

                for (Instance inst: dataSet)
                {
                    for(Integer id: findedUSers)
                    {
                        if (( (int)inst.value(0)) == id)
                        {
                            thisData.add(inst);
                        }
                    }
                }

                System.out.println("Searched Clusterer is clusterer #" + searchedClustererIndex);
                for(Instance inst: thisData )
                    {
                        System.out.println(inst);
                    }
                break;
            }
            numClusters++;
            error = thisError;
            if (numClusters >= listUsers.size())
                break;
        }

    }
    @Test
    // on deck of cards
    public void ClusteringTest() throws Exception {

        Instances data = DataGenerator.Generate();
        data.attribute(0).setWeight(0.0);
        data.attribute(1).setWeight(1.0);

        SimpleKMeans clusterer = new SimpleKMeans();
        clusterer.setNumClusters(4);
        clusterer.buildClusterer(data);

        //region output
        double[] arr = clusterer.getClusterSizes();
        for (int i = 0; i <= arr.length - 1; i++) {
            System.out.println("Cluster #" + Integer.toString(i) + ":" + Double.toString(arr[i]));
        }
        System.out.println("Error " + Double.toString(clusterer.getSquaredError()));

        Set<Instance> firstCluster = new HashSet<>();
        Set<Instance> secondCluster = new HashSet<>();
        Set<Instance> thirdCluster = new HashSet<>();
        Set<Instance> fourCluster = new HashSet<>();

        for (Instance instance : data) {
            if (clusterer.clusterInstance(instance) == 0) {
                firstCluster.add(instance);
            }
            if (clusterer.clusterInstance(instance) == 1) {
                secondCluster.add(instance);
            }
            if (clusterer.clusterInstance(instance) == 2) {
                thirdCluster.add(instance);
            }
            if (clusterer.clusterInstance(instance) == 3) {
                fourCluster.add(instance);
            }
        }


        System.out.println("first");
        for (Instance instance : firstCluster) {
            System.out.print(instance + " ");
        }
        System.out.println();
        System.out.println("second");
        for (Instance instance : secondCluster) {
            System.out.print(instance + " ");
        }
        System.out.println();
        System.out.println("third");
        for (Instance instance : thirdCluster) {
            System.out.print(instance + " ");
        }
        System.out.println();
        System.out.println("four");
        for (Instance instance : fourCluster) {
            System.out.print(instance + " ");
        }
        System.out.println();


        //endregion

        double error = 1.0;
        int n = 2;
        while (error != 0.0) {
            SimpleKMeans cluster = new SimpleKMeans();

            cluster.setNumClusters(n);
            cluster.buildClusterer(data);

            System.out.println(cluster.getNumClusters());

            error = cluster.getSquaredError();
            System.out.println(error);
            n++;
        }

    }

    @Test
    // on some mock-users
    public void ClusteringUserTest() throws Exception {

        //OMG
        User u1 = new User(30429836, "Женя", "Дубик", "2", "18.11", "Gabberland");
        User u2 = new User(32955433, "Алексей", "Митенев", "2", "23.11.1992", "Тюмень");
        User u3 = new User(32955433, "Кристина", "Клайд ", "1", "1.4", "Братск");
        User u4 = new User(34545743, "asfd", "sdf", "1", "", "agfadg");
        User u5 = new User(1345645, "fd", "SDFASF", "2", "", "aggadg");
        User u6 = new User(13654, "ASDFASF", "dfbsv", "2", "", "agdfg");
        User u7 = new User(23564765, "adfadg", "agdf", "2", "", "agfgadg");
        Set<User> users = new HashSet<User>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
        users.add(u5);
        users.add(u6);
        users.add(u6);
        users.add(u7);

        Instances dataSet = UsersConverter.usersToInstances(users);


        //tfidf
        StringToWordVector filter = getVectorizer();
        filter.setInputFormat(dataSet);
        Instances dataFiltered = Filter.useFilter(dataSet, filter);

        // some evaluation
        double error = 0.1;
        int numClusters = 2;
        while (true) {
            SimpleKMeans clusterer = new SimpleKMeans();
            clusterer.setNumClusters(numClusters);
            clusterer.buildClusterer(dataFiltered);


            //output
            System.out.println("# clusters: " + Integer.toString(numClusters));
            double[] arr = clusterer.getClusterSizes();
            System.out.println("Cluster sizes");
            for (int i = 0; i <= arr.length - 1; i++)
                System.out.println("\tCluster #" + Integer.toString(i) + ":" + Double.toString(arr[i]));
            System.out.println("Error " + Double.toString(clusterer.getSquaredError()));

            for (Instance instance : dataFiltered) {
                System.out.println("Cluster: " + clusterer.clusterInstance(instance));
                System.out.println(instance.toString());
            }
            System.out.println("#####################################");
            numClusters++;
            if (numClusters >= users.size())
                break;

        }

    }

    @Test
    // on real vk users
    public void ClusterVKUserTest() throws Exception {
        String searchingUserId = "7272824";

        //get vk data
        VkApi vkApi = new VkApi();

        String resultOneUser = vkApi.getUser(searchingUserId);
        JSONParser parser = new JSONParser();
        User oneUser = parser.parseUser(resultOneUser);

        String choiceSex = "2"; //TODO sexEnum
        VkApiParams param = VkApiParams.create();
        if (oneUser.getCity() instanceof String) {
            param.add("city", "1");
        }
        if (oneUser.getBdate() instanceof String) {
            String year = oneUser.getBdate();
            if (year.length() > 5) {
                Calendar calendar = Calendar.getInstance();

                int age = calendar.get(Calendar.YEAR) - Integer.valueOf(year.substring(4));
                param.add("age", String.valueOf(age));
            }
        }
        param.add("sex", choiceSex);
        String resultUserList = vkApi.getUsersList(param);
        List<User> listUsers = parser.parseUsers(resultUserList);
        listUsers.add(oneUser);

        // clusterer evaluation
        Instances dataSet = UsersConverter.usersToInstances(new HashSet<User>(listUsers));
        //StringToWordVector filter = getVectorizer();

        StringToWordVector filter = new StringToWordVector();
        filter.setAttributeIndicesArray(new int[]{1, 2, 3, 4, 5});
        filter.setInputFormat(dataSet);
        filter.setIDFTransform(true);
        filter.setTFTransform(true);

        Instances dataFiltered = Filter.useFilter(dataSet, filter);

        Instance tmp = dataFiltered.instance(0);
        System.out.println(tmp);
        int tmpID = (int)tmp.value(0);


        double error = 0.0;
        int numClusters = 2;
        //that's how we roll!

        Random rnd = new Random();
        int randomAccuracy = rnd.nextInt((5 - 3) + 1) + 3; // 3-5
        while (true) {
            SimpleKMeans clusterer = new SimpleKMeans();
            clusterer.setNumClusters(numClusters);

            clusterer.buildClusterer(dataFiltered);

            double thisError = clusterer.getSquaredError();
            if (numClusters == 2) {
                error = thisError;
                numClusters++;
                continue;
            }

            System.out.println("current n:" + numClusters);
            System.out.println("Current error: " + thisError);
            System.out.println("Cluster sizes");
            double[] arr = clusterer.getClusterSizes();
            for (int i = 0; i <= arr.length - 1; i++) {
                System.out.println("\tCluster #" + Integer.toString(i) + ":" + Double.toString(arr[i]));
            }
            double errorDiff = error - thisError;
            System.out.println("Error diff: " + errorDiff);

            if (numClusters== 13) {
                System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
                //find cluster with searced user
                //FIXME не будет работать до тех пор, пока мы не вернемся от tfidf к строкам
                // почему? ПАТАМУШТА наш ID из "1234" превратится в неведомый вектор
                int searchedClustererIndex = 0;
                for (Instance user : dataFiltered) {
                    if ( (int)user.value(0) == Integer.parseInt(searchingUserId)) {
                        searchedClustererIndex = clusterer.clusterInstance(user);
                    }
                }

                List<Integer> findedUSers = new ArrayList<Integer>();

                for (Instance instance : dataFiltered) {
                    if (clusterer.clusterInstance(instance) == searchedClustererIndex) {
                        findedUSers.add((int)instance.value(0));
                    }
                }

                Set<Instance> thisData = new HashSet<Instance>();

                for (Instance inst: dataSet)
                {
                    for(Integer id: findedUSers)
                    {
                        if (( (int)inst.value(0)) == id)
                        {
                            thisData.add(inst);
                        }
                    }
                }

                System.out.println("Searched Clusterer is clusterer #" + searchedClustererIndex);
                for(Instance inst: thisData )
                    {
                        System.out.println(inst);
                    }
                break;
            }
            numClusters++;
            error = thisError;
            if (numClusters >= listUsers.size())
                break;
        }

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
    public void vkApiTest() throws Exception {
        VkApi vkApi = new VkApi();
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
        if (oneUser.getCity() instanceof String) {
            param.add("city", oneUser.getCity());
        }
        if (oneUser.getBdate() instanceof String) {
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
        List<User> listUsers = parser.parseUsers(resultUserList);
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
        vectorizer.setAttributeIndicesArray(new int[]{1, 2, 3, 4, 5});
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
