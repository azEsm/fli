package ml.fli.utils;

import au.com.bytecode.opencsv.CSVWriter;
import ml.fli.models.User;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.Loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Set;

public class UsersConverter {
    private static final String CHARSET = "UTF-8";
    private static final String LOCATION = System.getProperty("user.dir");

    //TODO сейчас валится исключение, если у пользователя есть пустые поля. Надо пофиксить
    //для этого в м-де writeBody проверять значения и заменять пустые на что-нибудь другое
    /**
     * Конвертор пользователей в объект Instances
     * @param users
     * @return
     */
    public static Instances usersToInstances(Set<User> users) {
        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("Empty users list");
        }

        File file = Paths.get(LOCATION, System.nanoTime() + ".arff").toFile();

        writeHeader(file);
        writeBody(file, users);

        return buildResult(file);
    }

    private static void writeHeader(File file) {
        try (PrintWriter writer = new PrintWriter(file, CHARSET);) {
            writer.println("@Relation Users");
            writer.println("");
            writer.println("@attribute id string");
            writer.println("@attribute first_name string");
            writer.println("@attribute last_name string");
            writer.println("@attribute sex string");
            writer.println("@attribute city string");
            writer.println("@attribute bdate string");
            writer.println("");
            writer.println("@data");
            writer.close();
        } catch (Exception e) {
            throw Errors.asUnchecked(e);
        }
    }

    private static void writeBody(File file, Set<User> users) {
        try (CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(new FileOutputStream(file, true), CHARSET))) {
            String[] entries = new String[6];

            for (User user : users) {
                entries[0] = user.getId();
                entries[1] = user.getFirst_name();
                entries[2] = user.getLast_name();
                entries[3] = user.getSex();
                entries[4] = user.getCity();
                entries[5] = user.getBdate();
                csvWriter.writeNext(entries);
            }
        } catch (Exception e) {
            throw Errors.asUnchecked(e);
        }
    }

    private static Instances buildResult(File file) {
        try (InputStream arffFile = new FileInputStream(file)) {
            Loader loader = new ArffLoader();
            loader.setSource(arffFile);
            Instances result = loader.getDataSet();

            file.delete();

            return result;
        } catch (Exception e) {
            throw Errors.asUnchecked(e);
        }
    }
}
