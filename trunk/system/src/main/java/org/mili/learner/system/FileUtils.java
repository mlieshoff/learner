package org.mili.learner.system;

import org.apache.commons.io.Charsets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: micha
 * Date: 16.09.13
 * Time: 11:05
 * To change this template use File | Settings | File Templates.
 */
public class FileUtils {

    public static String readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        return new String(baos.toByteArray(), Charsets.UTF_8);
    }


}
