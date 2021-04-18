package app.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    public static void archiveFile(String filePath) {
        try {
            File folder = new File(filePath);
            Zip zip = new Zip(filePath + ".zip");
            File[] listFile = folder.listFiles();
            assert listFile != null;
            for (File file : listFile)
                if (file.isFile()) zip.add(file);
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Zip {
        private final ZipOutputStream zipStream;

        public Zip(String path) throws FileNotFoundException {
            FileOutputStream fos = new FileOutputStream(path);
            this.zipStream = new ZipOutputStream(fos);
        }

        public void add(File file) throws IOException {
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
        }

        public void add(String path) {
            File aFile = new File(path);
            try {
                add(aFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
