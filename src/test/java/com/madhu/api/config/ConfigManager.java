package com.madhu.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPS = new Properties();

    static {
        String env = System.getProperty("env", "qa");   // mvn test -Denv=qa
        String file = "config/" + env + ".properties";
        try (InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream(file)) {
            if (in == null) throw new IllegalStateException("Config file not found: " + file);
            PROPS.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private ConfigManager() {}

    // An environment variable wins over the file: auth.password -> AUTH_PASSWORD
    public static String get(String key) {
        String fromEnv = System.getenv(key.toUpperCase().replace('.', '_'));
        return fromEnv != null ? fromEnv : PROPS.getProperty(key);
    }
}