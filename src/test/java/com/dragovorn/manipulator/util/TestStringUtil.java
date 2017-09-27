package com.dragovorn.manipulator.util;

import com.dragovorn.manipulator.Main;
import com.dragovorn.manipulator.Manipulator;
import com.dragovorn.manipulator.Version;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(DataProviderRunner.class)
public class TestStringUtil {

    @Test
    @UseDataProvider("formatClassPathData")
    public void testFormatClassPath(String expected, Class<?> clazz) {
        assertEquals(expected, StringUtil.formatClassPath(clazz));
    }

    @DataProvider
    public static Object[][] formatClassPathData() {
        return new Object[][] {
                { "com/dragovorn/manipulator/Main", Main.class },
                { "com/dragovorn/manipulator/Manipulator", Manipulator.class },
                { "com/dragovorn/manipulator/Version", Version.class }
        };
    }
}