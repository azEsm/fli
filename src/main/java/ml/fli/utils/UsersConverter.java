package ml.fli.utils;

import ml.fli.db.models.User;
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
        Attribute photo = new Attribute("Photo", (ArrayList<String>) null);
        Attribute about = new Attribute("About", (ArrayList<String>) null);
        Attribute activities = new Attribute("Activities", (ArrayList<String>) null);
        Attribute books = new Attribute("Books", (ArrayList<String>) null);
        Attribute games = new Attribute("Games", (ArrayList<String>) null);
        Attribute interests = new Attribute("Interests", (ArrayList<String>) null);
        Attribute movies = new Attribute("Movies", (ArrayList<String>) null);
        Attribute audios = new Attribute("Audios", (ArrayList<String>) null);
        Attribute groups = new Attribute("Groups", (ArrayList<String>) null);



        attributesList.add(id);
        attributesList.add(first_name);
        attributesList.add(last_name);
        attributesList.add(sex);
        attributesList.add(city);
        attributesList.add(bdate);
        attributesList.add(photo);
        attributesList.add(about);
        attributesList.add(activities);
        attributesList.add(books);
        attributesList.add(games);
        attributesList.add(interests);
        attributesList.add(movies);
        attributesList.add(audios);
        attributesList.add(groups);


        Instances dataSet = new Instances("Users", attributesList, 0);
        //FIXME расставить веса
        dataSet.attribute("Id").setWeight(0.0);
        dataSet.attribute("Last_name").setWeight(0.0);
        dataSet.attribute("First_name").setWeight(0.0);
        dataSet.attribute("Sex").setWeight(1.0);
        dataSet.attribute("Audios").setWeight(0.7);
        dataSet.attribute("Groups").setWeight(0.9);
        dataSet.attribute("Home_town").setWeight(0.4);
        dataSet.attribute("Bdate").setWeight(0.2);
        dataSet.attribute("Photo").setWeight(0.0);
        // cast to instances

        for (User user : users) {
            double[] values = new double[dataSet.numAttributes()];

            values[0] = (double)user.getId();
            values[1] = dataSet.attribute(1).addStringValue(user.getFirst_name());
            values[2] = dataSet.attribute(2).addStringValue(user.getLast_name());
            values[3] = dataSet.attribute(3).addStringValue(user.getSex());
            values[4] = dataSet.attribute(4).addStringValue((user.getCity() != null) ? user.getCity() : "");
            values[5] = dataSet.attribute(5).addStringValue((user.getBdate() != null) ? user.getBdate() : "");

            String photoString = user.getPhoto_400_orig();
            if (photoString != null && photoString.length() <= 200) {
                values[6] = dataSet.attribute("Photo").addStringValue(photoString);
            }
            else
            {
                values[6] = dataSet.attribute("Photo").addStringValue("");
            }

            String aboutString = user.getAbout();
            if (aboutString != null && aboutString.length() <= 200) {
                values[7] = dataSet.attribute("About").addStringValue(aboutString);
            } else {
                values[7] = dataSet.attribute("About").addStringValue("");
            }

            String activitiesString = user.getActivities();
            if (activitiesString != null && activitiesString.length() <= 200) {
                values[8] = dataSet.attribute("Activities").addStringValue(aboutString);
            } else {
                values[8] = dataSet.attribute("Activities").addStringValue("");
            }

            String bookString = user.getBooks();
            if (bookString != null && bookString.length() <= 200) {
                values[9] = dataSet.attribute("Books").addStringValue(bookString);
            } else {
                values[9] = dataSet.attribute("Books").addStringValue("");
            }

            String gamesString = user.getGames();
            if (gamesString != null && gamesString.length() <= 200) {
                values[10] = dataSet.attribute("Games").addStringValue(gamesString);
            } else {
                values[10] = dataSet.attribute("Games").addStringValue("");
            }

            String interestsString = user.getInterests();
            if (interestsString != null && interestsString.length() <= 200) {
                values[11] = dataSet.attribute("Interests").addStringValue(interestsString);
            } else {
                values[11] = dataSet.attribute("Interests").addStringValue("");
            }

            String moviesString = user.getMovies();
            if (moviesString != null && moviesString.length() <= 200) {
                values[12] = dataSet.attribute("Movies").addStringValue(moviesString);
            } else {
                values[12] = dataSet.attribute("Movies").addStringValue("");
            }

            String audio = "";
            if (user.getAudio() != null) {
                for (String vkaudio : user.getAudio()) {
                    audio += vkaudio.toString() + ",";
                }
            }
            values[13] = dataSet.attribute(13).addStringValue(audio);

            String group = "";
            if (user.getGroups() != null) {
                for (String vkgroup : user.getGroups()) {
                    group += vkgroup.toString() + ",";
                }
            }
            values[14] = dataSet.attribute(14).addStringValue(group);

            Instance instance = new DenseInstance(1.0, values);
            dataSet.add(instance);
        }
        return dataSet;
    }
}
