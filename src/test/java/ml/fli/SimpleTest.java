package ml.fli;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeans;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebAppConfiguration
public class SimpleTest {
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void dummyTest() throws Exception {
        Clusterer clusterer = new SimpleKMeans();
        Assert.assertNotNull(clusterer);
        Assert.assertNotNull(restTemplate);
    }

    public void testTest() throws Exception{
        // to do something
    }
}
