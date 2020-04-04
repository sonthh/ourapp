package com.son.util.common;

import org.apache.commons.io.FilenameUtils;

public class FileUtil {

    public static final String PREFIX = "file-";

    public static String rename(String fileName) {
        return PREFIX + System.nanoTime()
                + "." + FilenameUtils.getExtension(fileName);
    }
}
