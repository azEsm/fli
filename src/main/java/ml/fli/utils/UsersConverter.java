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

        //generate attributes
        ArrayList<Attribute> attributesList = new ArrayList<Attribute>();

        Attribute id = new Attribute("Id");
        Attribute first_name = new Attribute("First_name", (ArrayList<String>) null);
        Attribute last_name = new Attribute("Last_name", (ArrayList<String>) null);
        Attribute sex = new Attribute("Sex", (ArrayList<String>) null);
        Attribute city = new Attribute("Home_town", (ArrayList<String>) null);
        Attribute bdate = new Attribute("Bdate", (ArrayList<String>) null);

        attributesList.add(id);
        attributesList.add(first_name);
        attributesList.add(last_name);
        attributesList.add(sex);
        attributesList.add(city);
        attributesList.add(bdate);

        // cast to instances
        Instances dataSet = new Instances("Users", attributesList, 0);
        //dataSet.attribute("Id").setWeight(0.0);

        for (User user : users) {
            double[] values = new double[dataSet.numAttributes()];

            values[0] = (double)user.getId();
            values[1] = dataSet.attribute(1).addStringValue(user.getFirst_name());
            values[2] = dataSet.attribute(2).addStringValue(user.getLast_name());
            values[3] = dataSet.attribute(3).addStringValue(user.getSex());
            values[4] = dataSet.attribute(4).addStringValue((user.getCity() != null) ? user.getCity() : "");
            values[5] = dataSet.attribute(5).addStringValue((user.getBdate() != null) ? user.getBdate() : "");
            Instance instance = new DenseInstance(1.0, values);
            dataSet.add(instance);
        }

        return dataSet;
    }
}
