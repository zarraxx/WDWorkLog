package com.wondersgroup.hs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zarra on 15/3/9.
 */
public class TestHelper {
    public static InputStream loadFile(String filename) {
        InputStream in = TestHelper.class.getClassLoader().getResourceAsStream(filename);

        return in;
    }

//    public static File testFile(String filename) throws URISyntaxException {
//        URL url = TestHelper.class.getClassLoader().getResource(filename);
//        File f = new File(url.toURI());
//        return f;
//    }

    public static Properties loadProperties(String name) throws IOException {
        try(InputStream inputStream = loadFile(name+".properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        }
    }
}
