package ml.fli.utils;

import ml.fli.models.User;
import ml.fli.models.weka.VKAudio;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Set;

public class UsersConverter {

    public static Instances usersToInstances(Set<User> users) {
        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("Empty users list");
        }

        //generate attributes
        ArrayList<Attribute> attributesList = new ArrayList<>();
        Attribute id = new Attribute("Id");
        Attribute first_name = new Attribute("First_name", (ArrayList<String>) null);
        Attribute last_name = new Attribute("Last_name", (ArrayList<String>) null);
        Attribute sex = new Attribute("Sex", (ArrayList<String>) null);
        Attribute city = new Attribute("Home_town", (ArrayList<String>) null);
        Attribute bdate = new Attribute("Bdate", (ArrayList<String>) null);

        ArrayList<Attribute> audioAttributes = new ArrayList<>();
        Attribute artist = new Attribute("Artist", (ArrayList<String>) null);
        Attribute track = new Attribute("Track", (ArrayList<String>) null);
        audioAttributes.add(artist);
        audioAttributes.add(track);
        Instances audios = new Instances("Audios", audioAttributes, 0);
        Attribute audios_relation = new Attribute("Audios", audios);

        ArrayList<Attribute> groupAttributes = new ArrayList<>();
        Attribute groupAtt = new Attribute("Group", (ArrayList<String>) null);
        groupAttributes.add(groupAtt);
        Instances groups = new Instances("Groups", groupAttributes, 0);
        Attribute groups_relation = new Attribute("Groups", audios);

        attributesList.add(id);
        attributesList.add(first_name);
        attributesList.add(last_name);
        attributesList.add(sex);
        attributesList.add(city);
        attributesList.add(bdate);
        attributesList.add(audios_relation);
       // attributesList.add(groups_relation);

        Instances dataSet = new Instances("Users", attributesList, 0);
        //FIXME расставить веса
        dataSet.attribute("Id").setWeight(0.0);
        dataSet.attribute("Last_name").setWeight(0.0);
        dataSet.attribute("First_name").setWeight(0.0);
        // cast to instances

        for (User user : users) {
            double[] values = new double[dataSet.numAttributes()];

            values[0] = (double)user.getId();
            values[1] = dataSet.attribute(1).addStringValue(user.getFirst_name());
            values[2] = dataSet.attribute(2).addStringValue(user.getLast_name());
            values[3] = dataSet.attribute(3).addStringValue(user.getSex());
            values[4] = dataSet.attribute(4).addStringValue((user.getCity() != null) ? user.getCity() : "");
            values[5] = dataSet.attribute(5).addStringValue((user.getBdate() != null) ? user.getBdate() : "");

            for (VKAudio audio: user.getUser_audio())
            {
                double[] audioValues = new double[audios.numAttributes()];
                audioValues[0] = audios.attribute(0).addStringValue(audio.getArtist());
                audioValues[1] = audios.attribute(1).addStringValue(audio.getTrack());
                Instance instance = new DenseInstance(1.0, audioValues);
                audios.add(instance);
            }
            values[6] = dataSet.attribute(6).addRelation(audios);

            for (String group: user.getUser_group())
            {
                double[] groupValues = new double[groups.numAttributes()];
                groupValues[0] = groups.attribute(0).addStringValue(group);
                Instance instance = new DenseInstance(1.0, groupValues);
                groups.add(instance);
            }
           // values[7] = dataSet.attribute(7).addRelation(groups);

            Instance instance = new DenseInstance(1.0, values);
            dataSet.add(instance);
        }
        return dataSet;
    }
}
