package com.icecream.controller;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.icecream.entity.Goods;
import com.icecream.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/scan")
public class ScanController {

    // ⚠️ 引入你的商品业务服务（这里需要改成你自己实际的项目类名和包名）
    @Autowired
    private GoodsService goodsService;

    @PostMapping("/barcode")
    public Map<String, Object> scanBarcode(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        if (file.isEmpty()) {
            response.put("status", -1);
            response.put("msg", "文件不能为空");
            return response;
        }

        try {
            // 1. 读取并修复图片方向
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            bufferedImage = fixExifOrientation(file, bufferedImage);

            // 2. 缩放图片
            bufferedImage = resizeImageForBarcode(bufferedImage, 800);

            // 3. 识别条形码
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);
            String barcode = result.getText();

            // 4. 【核心改动】根据识别出的条码，查询数据库中的商品信息
            Object goodsDetail = null;
            boolean found = false;

            if (barcode != null) {
                // ⚠️ 这里调用你自己 Service 里的查询方法，比如 goodsService.findByBarcode(barcode)
                // 假设查询出来的商品对象叫 goods
                Goods goods = goodsService.getByBarcode(barcode);
                if (goods != null) {
                    goodsDetail = goods;
                    found = true;
                }

                // 为了演示结构，这里用一个占位符 (实际请换上你的数据库查询结果)
                // 如果没查到，goodsDetail 保持 null，found 保持 false
            }

            // 5. 组装返回给前端的 JSON
            response.put("status", 0);
            response.put("msg", found ? "识别成功" : "识别成功，但未找到对应商品");
            response.put("found", found);
            response.put("barcode", barcode);
            response.put("data", goodsDetail); // 如果查到商品，这里就是完整的商品对象

        } catch (IOException e) {
            response.put("status", -1);
            response.put("msg", "图片读取异常: " + e.getMessage());
        } catch (com.google.zxing.NotFoundException e) {
            response.put("status", 0);
            response.put("msg", "识别成功，但未找到对应商品");
            response.put("found", false);
            response.put("barcode", null);
            response.put("data", null);
        } catch (Exception e) {
            response.put("status", -1);
            response.put("msg", "系统识别异常");
        }
        return response;
    }

    // ==========================================================
    // 辅助工具方法集合
    // ==========================================================
    private BufferedImage fixExifOrientation(MultipartFile file, BufferedImage image) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
            ExifIFD0Directory exif = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            if (exif != null && exif.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                int orientation = exif.getInt(ExifIFD0Directory.TAG_ORIENTATION);
                switch (orientation) {
                    case 6:
                        return rotateImage(image, 90);
                    case 3:
                        return rotateImage(image, 180);
                    case 8:
                        return rotateImage(image, 270);
                }
            }
        } catch (Exception e) {
        }
        return image;
    }

    private BufferedImage resizeImageForBarcode(BufferedImage image, int maxPixels) {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width <= maxPixels && height <= maxPixels) return image;

        double ratio = Math.min((double) maxPixels / width, (double) maxPixels / height);
        int newWidth = (int) (width * ratio);
        int newHeight = (int) (height * ratio);

        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2d.dispose();
        return resizedImage;
    }

    private BufferedImage rotateImage(BufferedImage image, int degrees) {
        int width = image.getWidth();
        int height = image.getHeight();
        boolean swap = (degrees == 90 || degrees == 270);
        int newWidth = swap ? height : width;
        int newHeight = swap ? width : height;

        BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();
        g2d.translate(newWidth / 2.0, newHeight / 2.0);
        g2d.rotate(Math.toRadians(degrees));
        g2d.translate(-width / 2.0, -height / 2.0);
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return rotatedImage;
    }
}