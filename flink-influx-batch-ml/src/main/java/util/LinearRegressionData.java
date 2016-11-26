package util;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides the default data sets used for the Linear Regression example
 * program. The default data sets are used, if no parameters are given to the
 * program.
 */
public class LinearRegressionData {
    private static final String DELIMITER = " ";

    public static DataSet<Params> readParamsDataSetFromFile (String path, ExecutionEnvironment env) {
        String content;

        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            content = "0.0 0.0"; // Default params
        }

        // Parse to list
        List<Params> paramsList = new LinkedList<>();
        String[] params = content.split(DELIMITER);
        paramsList.add(new Params(Double.parseDouble(params[0]), Double.parseDouble(params[1])));

        return env.fromCollection(paramsList);
    }
}