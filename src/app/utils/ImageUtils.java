package app.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtils {
    public static void crop(String in, String out) {
        try {
            BufferedImage originalImage = ImageIO.read(new File(in));
            BufferedImage subImage = originalImage.getSubimage(110, 1760, 450, 120);
            File outputFile = new File(out);
            ImageIO.write(subImage, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static InputStream resizeImage(InputStream uploadedInputStream, String fileName, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(uploadedInputStream);
            java.awt.Image originalImage= image.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);

            int type = ((image.getType() == 0) ? BufferedImage.TYPE_INT_ARGB : image.getType());
            BufferedImage resizedImage = new BufferedImage(width, height, type);

            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();
            g2d.setComposite(AlphaComposite.Src);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, fileName.split("\\.")[1], byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            return uploadedInputStream;
        }
    }

    public static void mergeAll(String fileName) {
        File folder = new File(fileName);
        mergeAll(folder);
    }
    public static void mergeAll(File folder){
        try {
            File[] files = folder.listFiles();
            int width = 1648, height = 2136;
            assert files != null;
            BufferedImage sumImage = new BufferedImage(width, height*files.length,  BufferedImage.SCALE_SMOOTH );
            Graphics2D g2d = sumImage.createGraphics();
            g2d.setComposite(AlphaComposite.Src);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int i = 0;
            for (File file : files){
                if (file.getName().endsWith(".jpg")){
                    FileInputStream jpg = new FileInputStream(file);
                    BufferedImage image = ImageIO.read(jpg);
                    Image originalImage= image.getScaledInstance(width, height,  Image.SCALE_DEFAULT);
                    g2d.drawImage(originalImage, 0, height*i, width, height, null);
                    i++;
                }
            }
            g2d.dispose();
            ImageIO.write(sumImage,  "jpg", new File(folder.getPath()+ ".jpg"));
        } catch (IOException e){
            e.printStackTrace();
        }

    }


}
