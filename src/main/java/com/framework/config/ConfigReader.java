package com.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Central configuration access.
 * Precedence (highest first): -D system property  >  config/{env}.properties  >  config/config.properties
 */
public final class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        load("config/config.properties", true);
        String env = System.getProperty("env", PROPS.getProperty("env", "qa"));
        load("config/" + env + ".properties", false);
    }

    private ConfigReader() {
    }

    private static void load(String path, boolean mandatory) {
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                if (mandatory) {
                    throw new IllegalStateException("Missing configuration file: " + path);
                }
                return;
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read " + path, e);
        }
    }

    public static String get(String key) {
        String override = System.getProperty(key);
        return override != null ? override.trim() : PROPS.getProperty(key, "").trim();
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }
}
