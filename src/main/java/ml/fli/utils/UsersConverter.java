package ml.fli.utils;

import ml.fli.models.User;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.Set;

public class UsersConverter {
    //TODO сейчас валится исключение, если у пользователя есть пустые поля. Надо пофиксить
    //для этого в м-де writeBody проверять значения и заменять пустые на что-нибудь другое

    /**
     * Конвертор пользователей в объект Instances
     *
     * @param users
     * @return
     */
    public static Instances usersToInstances(Set<User> users) {
        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("Empty users list");
        }

        //generate instance for attribute for instance
        ArrayList<Attribute> atts = new ArrayList<Attribute>();
        atts.add(new Attribute("User_audio"));
        ArrayList<String> artistAndTrackName = new ArrayList<String>();
        artistAndTrackName.add("Artist");
        artistAndTrackName.add("Song");
        atts.add(new Attribute("user.audio", artistAndTrackName));
        Instances uAud_str = new Instances("user_audio", atts, 0);

        ArrayList<Attribute> atts1 = new ArrayList<Attribute>();
        atts1.add(new Attribute("User_group"));
        Instances uGrp_str = new Instances("user_group", atts1, 0);



        //generate attributes
        ArrayList<Attribute> attributesList = new ArrayList<Attribute>();
        Attribute id = new Attribute("Id");

        Attribute first_name = new Attribute("First_name", (ArrayList<String>) null);
        Attribute last_name = new Attribute("Last_name", (ArrayList<String>) null);
        Attribute sex = new Attribute("Sex", (ArrayList<String>) null);
        Attribute city = new Attribute("Home_town", (ArrayList<String>) null);
        Attribute bdate = new Attribute("Bdate", (ArrayList<String>) null);
        Attribute user_audio = new Attribute("User_audio", uAud_str);
        Attribute user_group = new Attribute("User_group", uGrp_str);

        attributesList.add(id);
        attributesList.add(first_name);
        attributesList.add(last_name);
        attributesList.add(sex);
        attributesList.add(city);
        attributesList.add(bdate);
        attributesList.add(user_audio);
        attributesList.add(user_group);

        // cast to instances
        Instances dataSet = new Instances("Users", attributesList, 0);
        //FIXME расставить веса
        //dataSet.attribute("Id").setWeight(0.0);
        //dataSet.attribute("Last_name").setWeight(0.0);
        //dataSet.attribute("First_name").setWeight(0.0);
        //dataSet.attribute("Bdate").setWeight(0.0);

        for (User user : users) {
            double[] values = new double[dataSet.numAttributes()];

            values[0] = (double)user.getId();
            values[1] = dataSet.attribute(1).addStringValue(user.getFirst_name());
            values[2] = dataSet.attribute(2).addStringValue(user.getLast_name());
            values[3] = dataSet.attribute(3).addStringValue(user.getSex());
            values[4] = dataSet.attribute(4).addStringValue((user.getCity() != null) ? user.getCity() : "");
            values[5] = dataSet.attribute(5).addStringValue((user.getBdate() != null) ? user.getBdate() : "");
            values[6] = dataSet.attribute(6).addStringValue(String.valueOf(user.getUser_audio()));
            values[6] = dataSet.attribute(6).addStringValue(String.valueOf(user.getUser_group()));
            Instance instance = new DenseInstance(1.0, values);
            dataSet.add(instance);
        }

        return dataSet;
    }
}
