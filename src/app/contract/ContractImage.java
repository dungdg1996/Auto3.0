package app.contract;

import app.entity.ConfigResult;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ContractImage {

    Graphics2D graphics;
    BufferedImage image;

    public ContractImage(String path) {
        try {
            this.image = ImageIO.read(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lỗi không tìm thấy file: " + path);
        }
        this.graphics = (Graphics2D) this.image.getGraphics();
        this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }

    public void drawText(String text, int x, int y, String align) {
        int dx;
        switch (align) {
            default:
            case "TRAI":
                drawText(x, y, text);
                break;
            case "GIUA":
                dx = (int) graphics.getFontMetrics().getStringBounds(text, graphics).getWidth();
                drawText(x - dx / 2, y, text);
                break;
            case "PHAI":
                dx = (int) graphics.getFontMetrics().getStringBounds(text, graphics).getWidth();
                drawText(x - dx, y, text);
                break;
        }
    }

    public void drawImg(Image img, int x, int y, String align) {
        int dx;
        switch (align) {
            default:
            case "TRAI":
                this.graphics.drawImage(img, x, y, null);
                break;
            case "GIUA":
                dx = img.getWidth(null);
                this.graphics.drawImage(img, x - dx / 2, y, null);
                break;
            case "PHAI":
                dx = img.getWidth(null);
                this.graphics.drawImage(img, x - dx, y, null);
                break;
        }
    }

    public void drawImg(String imgName, int x, int y, String align) {

        try {
            Image image = ImageIO.read(new FileInputStream(imgName));
            this.drawImg(image, x, y, align);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawText(int x, int y, String text) {
        graphics.drawString(text, x, y);
    }


    public void save(String path) {
        graphics.dispose();
        try {
            File file = new File(path);
            file.mkdirs();
            ImageIO.write(image, "jpg", file);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi khi lưu file : " + path);
        }
    }

    public void draw(ConfigResult c) {
        if (c.getType().startsWith("SDT")) {
            this.graphics.setFont(c.getFont());
            this.graphics.setColor(c.getColor());
            drawText(c.getValue(), c.getX(), c.getY(), c.getAlign());
            return;
        }
        switch (c.getType()) {
            case "CHU_KY":
            case "HINH_ANH":
                drawImg(c.getValue(), c.getX(), c.getY(), c.getAlign());
                break;
            default:
            case "TEXT":
                this.graphics.setFont(c.getFont());
                this.graphics.setColor(c.getColor());
                drawText(c.getValue(), c.getX(), c.getY(), c.getAlign());
                break;
        }
    }
}
