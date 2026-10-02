package com.hotel.app.common.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码工具类
 *
 * @author ruoyi
 * @date 2025-09-02
 */
public class QRCodeUtils {

    /**
     * 生成二维码并返回Base64编码的图片数据
     *
     * @param content 二维码内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return Base64编码的二维码图片数据
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeImage(String content, int width, int height) throws WriterException, IOException {
        // 设置二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        // 生成二维码矩阵
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        // 将二维码转换为Base64编码的图片数据
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);

        return "data:image/png;base64," + base64Image;
    }

    /**
     * 生成默认大小的二维码
     *
     * @param content 二维码内容
     * @return Base64编码的二维码图片数据
     * @throws WriterException
     * @throws IOException
     */
    public static String generateQRCodeImage(String content) throws WriterException, IOException {
        return generateQRCodeImage(content, 200, 200);
    }
}