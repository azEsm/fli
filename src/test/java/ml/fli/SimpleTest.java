package ml.fli;

import org.junit.Assert;
import org.junit.Test;
import weka.clusterers.Clusterer;
import weka.clusterers.SimpleKMeans;

public class SimpleTest {

    @Test
    public void dummyTest() throws Exception {
        Clusterer clusterer = new SimpleKMeans();
        Assert.assertNotNull(clusterer);
    }
}
