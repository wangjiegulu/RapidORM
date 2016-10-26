package com.wangjie.rapidorm.compiler.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.tools.Diagnostic;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 3/15/16.
 */
public class LogUtil {
    public static final boolean LOG_CONTROL = true;
    public static final boolean LOG_FILE = false;
    private static SimpleDateFormat LOGGER_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");


    public static void loggerE(Throwable throwable) {
        if (!LOG_CONTROL && !LOG_FILE) {
            return;
        }
        logger("[ERROR]" + transformStackTrace(throwable));
    }

    public static void logger(String str) {
        if (!LOG_CONTROL && !LOG_FILE) {
            return;
        }
        String loggerDate = "...[" + LOGGER_DATE_FORMAT.format(new Date()) + "]";
        String log = loggerDate + ": " + str;
        if (LOG_CONTROL) {
            GlobalEnvironment.getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.NOTE, log);
        }
        if (LOG_FILE) {
            writeToDisk(log);
        }
    }

    private static void writeToDisk(String log) {
        try {
            File logFile = new File("/Users/wangjie/Desktop/za/test/alibaba/processor_http.txt");
            if (!logFile.exists()) {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            }
            FileWriter fw = new FileWriter(logFile, true);
            fw.write(log + "\n\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringBuilder transformStackTrace(StackTraceElement[] elements){
        StringBuilder sb = new StringBuilder();
        for(StackTraceElement element : elements){
            sb.append(element.toString()).append("\r\n");
        }
        return sb;
    }
    public static String transformStackTrace(Throwable throwable){
        if(null == throwable){
            return "throwable is null";
        }
        StringBuilder sb = new StringBuilder(throwable.getMessage()).append("\n");
        for(StackTraceElement element : throwable.getStackTrace()){
            sb.append(element.toString()).append("\r\n");
        }
        return sb.toString();
    }

}
