package com.vbermudez.floatingwidgets.utils;

import android.content.Context;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.xml.transform.Templates;

public class ParseHelper {

    public static byte[] inputStreamToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        return buffer.toByteArray();
    }

    public static String decompressGZ(GZIPInputStream is, Context context) throws IOException {
        String filePath = context.getFilesDir().getPath().toString() + "/dict.mp";
        File f = new File(filePath);
        f.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(f)) {

            // Create a buffer and temporary variable used during the
            // file decompress process.
            byte[] buffer = new byte[40960];
            int length;

            // Read from the compressed source file and write the
            // decompress file.
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // from
        String everything = "";
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(f)), "utf-8"))) {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        everything = stringBuilder.toString();
        return everything;
    }
}
