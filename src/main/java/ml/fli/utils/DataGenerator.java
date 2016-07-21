package ml.fli.utils;

import weka.core.*;

import java.util.Random;
import java.util.ArrayList;


// oh, .net... I miss soooooo much about your Static-classes...
public final class DataGenerator {

    private DataGenerator() {
        ; // yep, we do NOTHING
    }

    public static Instances Generate() {
        //build a beck of cards
        ArrayList<String> suites = new ArrayList<String>();
        suites.add("♠");
        suites.add("♥");
        suites.add("♦");
        suites.add("♣");

        ArrayList<String> cards = new ArrayList<String>();

        for (String s : suites) {
            for (int i = 1; i <= 13; i++)
                cards.add(Integer.toString(i) + "," + s);

        }

        // add BRAND NEW CARD
        cards.add("6,♠");


        cards = Shuffle(cards);

        return GenerateInstances(cards);

    }

    private static ArrayList<String> Shuffle(ArrayList<String> data) {
        Random rnd = new Random();
        int n = data.size();
        while (n > 1) {
            n--;
            int k = rnd.nextInt(n + 1);
            String value = data.get(k);
            data.set(k, data.get(n));
            data.set(n, value);
        }
        return data;
    }


    // look 18.3 in WekaManual for more
    private static Instances GenerateInstances(ArrayList<String> data) {
        ArrayList<Attribute> attributesList = new ArrayList<Attribute>();
        Attribute cardValue = new Attribute("Card Value");
        //define suite as noninal attribute
        ArrayList<String> suites = new ArrayList<>();
        suites.add("♠");
        suites.add("♥");
        suites.add("♦");
        suites.add("♣");
        Attribute cardCuite = new Attribute("suite", suites);
        attributesList.add(cardValue);
        attributesList.add(cardCuite);

        Instances dataSet = new Instances("deck of cards", attributesList, 0);
        for (String s : data) {
            double[] values = new double[dataSet.numAttributes()];
            String[] dataParts = s.split(",");
            values[0] = Integer.parseInt(dataParts[0]);
            values[1] = dataSet.attribute(1).indexOfValue(dataParts[1]);
            Instance inst = new DenseInstance(1.0, values);
            dataSet.add(inst);
        }
        return dataSet;
    }

}
