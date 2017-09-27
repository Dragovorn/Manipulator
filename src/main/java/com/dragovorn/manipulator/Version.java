package com.dragovorn.manipulator;

import com.dragovorn.manipulator.util.FileUtil;

import java.io.IOException;
import java.util.Properties;

public class Version {

    private String version;

    Version() {
        Properties properties = new Properties();

        try {
            properties.load(FileUtil.getResource("version.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.version = properties.getProperty("version");
    }

    String getVersion() {
        return this.version;
    }
}