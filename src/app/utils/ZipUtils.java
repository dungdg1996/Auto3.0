package app.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static void toArchive(String filePath) {
        File folder = new File(filePath);
        Zip zip = new Zip(filePath + ".zip");
        File[] listFile = folder.listFiles();
        assert listFile != null;
        for (File file : listFile)
            if (file.isFile()) zip.add(file);
        zip.close();
    }

    private static class Zip {
        private ZipOutputStream zipStream;

        public Zip(String path) {
            try {
                FileOutputStream fos = new FileOutputStream(path);
                this.zipStream = new ZipOutputStream(fos);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void add(File file) {
            try {
                FileInputStream fis = new FileInputStream(file);
                ZipEntry zipEntry = new ZipEntry(file.getName());
                zipStream.putNextEntry(zipEntry);

                byte[] bytes = new byte[1024];
                int length;
                while ((length = fis.read(bytes)) >= 0) {
                    zipStream.write(bytes, 0, length);
                }
                zipStream.closeEntry();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public void add(String path) {
            File aFile = new File(path);
            add(aFile);
        }

        public void close() {
            try {
                zipStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
