package app.utils;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileUtils {
    final static String TONG_F_PATH = "\\\\HUNG_NAS\\Server_2020\\MAY TINH BAN_1\\TONG_F\\";

    public static void copy(String sourcePath, String destinationPath) throws IOException {
        System.out.println("FileUtils: copy " + sourcePath + " to " + destinationPath);
        Path in = Paths.get(sourcePath);
        Path des = Paths.get(destinationPath);
        Files.copy(in, des, StandardCopyOption.REPLACE_EXISTING);
        System.out.println(".Success!");
    }

    public static void copy(File source, File dest) throws IOException {
        if (source.getPath().equals(dest.getPath())) return;
        System.out.print("FileUtils: copy " + source.getPath() + " to " + dest.getPath());
        InputStream is;
        OutputStream os;
        is = new FileInputStream(source);
        os = new FileOutputStream(dest);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.close();
    }

    public static void copyFromTong_F(String fileName, String desForder) throws IOException {
        copy(TONG_F_PATH + fileName, desForder + fileName);
    }

    public static boolean isExistInTongF(String fileName) {
        File file = new File(TONG_F_PATH + fileName);
        return file.exists();
    }

    public static void save(String maHinh, List<File> files) throws IOException {
        if (files == null || files.size() < 3)
            throw new RuntimeException("File ảnh không đủ, vui lòng chọn thêm ảnh!");

        for (int i = 0; i < files.size(); i++) {
            File src = files.get(i);
            String saveFileName = TONG_F_PATH + "\\" + maHinh + (i + 1) + ".jpg";
            File des = new File(saveFileName);
            if (src.equals(des))
                continue;
            if (des.exists())
                throw new RuntimeException("File ảnh " + maHinh + (i + 1) + ".jpg đã tồn tại, kiểm tra lại trong TONG_F");
            FileUtils.copy(src, des);
            String reName = src.getParent() + "\\" + maHinh + (i + 1) + ".jpg";
            src.renameTo(new File(reName));
        }
    }
}
