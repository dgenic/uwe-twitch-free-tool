package de.uwe;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Config {

    public static final Map<String, String> map = new HashMap<>();
    public static final String O_AUTH = "O_AUTH";
    public static final String CLIENT_ID = "CLIENT_ID";

    static {

        try {
            InputStream inputStream = Config.class.getClassLoader().getResourceAsStream("twitch.properties");

            assert inputStream != null;
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                String[] pair = scanner.nextLine().split("=");
                map.put(pair[0], pair[1]);
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getValue(String name){
        return map.get(name);
    }

}
