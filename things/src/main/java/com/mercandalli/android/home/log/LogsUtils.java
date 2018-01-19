package com.mercandalli.android.home.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Jonathan on 19/01/2018.
 */

public class LogsUtils {


    public static String logs() {
        try {
            Process process = Runtime.getRuntime().exec("logcat -t 35");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line).append("\n");
            }
            return log.toString();
        } catch (IOException ignored) {
        }
        return "";
    }
}
