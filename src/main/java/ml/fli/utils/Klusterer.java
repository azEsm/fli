package ml.fli.utils;

import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

import java.util.HashSet;
import java.util.Set;


public class Klusterer {

    private Instances dataBackup;
    private SimpleKMeans clusterer;

    public Klusterer()
    {}
    // tf-idf transform
    private Instances DataPrepare(Instances data) throws Exception
    {
        StringToWordVector filter = new StringToWordVector();
        filter.setInputFormat(data);
        filter.setIDFTransform(true);
        filter.setTFTransform(true);

        Instances dataFiltered = Filter.useFilter(data, filter);
        return dataFiltered;
    }

    //detecting numbers of clusters by "Rule of Thumb"
    private int DetectNumberOFClusters(int dataSize)
    {
        return (int)Math.pow((double)(dataSize/2),0.5);
    }

    private void Clusterize(Instances data, int numOfClusters) throws Exception
    {
        clusterer = new SimpleKMeans();
        clusterer.setNumClusters(numOfClusters);
        clusterer.buildClusterer(data);
    }

    private  Set<Instance> FindTargetCluster(Instances data, int searchedId) throws Exception
    {
        int searchedClustererIndex = 0;

        //ищем кластер, где находится искомый объект
        for (Instance user : data) {
            if ( (int)user.value(0) == searchedId) {
                searchedClustererIndex = clusterer.clusterInstance(user);
            }
        }

        //собираем все объекты из искомого кластера ( над кластером tf-idf)
        Set<Integer> findedUsers = new HashSet<Integer>();
        for (Instance instance : data) {
            if (clusterer.clusterInstance(instance) == searchedClustererIndex) {
                findedUsers.add((int)instance.value(0));
            }
        }

        //ищем соответствующие записи в ИСХОДНЫХ ДАННЫХ
        Set<Instance> thisData = new HashSet<>();
        for (Instance inst: dataBackup)
        {
            for(Integer id: findedUsers)
            {
                if (( (int)inst.value(0)) == id)
                {
                    thisData.add(inst);
                }
            }
        }

        return  thisData;
    }

    public  Set<Instance> FindCluster(Instances data, int findedId) throws  Exception
    {
        this.dataBackup = data;

        int numberOfClusters = DetectNumberOFClusters(data.size());
        Instances preparedData = DataPrepare(data);

        Clusterize(preparedData, numberOfClusters);

        Set<Instance> searchedData = FindTargetCluster(preparedData, findedId);

        return searchedData;



    }
}
