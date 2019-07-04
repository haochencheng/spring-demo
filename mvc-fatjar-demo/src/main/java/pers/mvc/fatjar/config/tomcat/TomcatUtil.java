package pers.mvc.fatjar.config.tomcat;

import java.io.File;
import java.io.IOException;

/**
 * @description:
 * @author: haochencheng
 * @create: 2019-07-04 14:00
 **/
public class TomcatUtil {

    public static File createTempDir(String prefix, int port) throws IOException {
        File tempDir = File.createTempFile(prefix + ".", "." + port);
        tempDir.delete();
        tempDir.mkdir();
        tempDir.deleteOnExit();
        return tempDir;
    }

}
