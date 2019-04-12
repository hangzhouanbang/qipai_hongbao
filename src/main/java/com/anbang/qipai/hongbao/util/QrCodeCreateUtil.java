package com.anbang.qipai.hongbao.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QrCodeCreateUtil {

	/**
	 * 生成二维码
	 */
	public static BufferedImage createQrCode(String content, int qrCodeSize) throws WriterException {
		// 设置二维码纠错级别ＭＡＰ
		Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // 矫错级别
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		// 创建比特矩阵(位矩阵)的QR码编码的字符串
		BitMatrix byteMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);
		// 使BufferedImage勾画QRCode (matrixWidth 是行二维码像素点)
		int matrixWidth = byteMatrix.getWidth();
		BufferedImage image = new BufferedImage(matrixWidth - 100, matrixWidth - 100, BufferedImage.TYPE_INT_RGB);
		// 获取画笔
		Graphics2D graphics = image.createGraphics();
		// 填充空白区域
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixWidth);
		// 使用比特矩阵画
		graphics.setColor(Color.BLACK);
		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i - 50, j - 50, 1, 1);
				}
			}
		}
		// 释放画笔
		graphics.dispose();
		return image;
	}

	// 将img2合成到img1
	public static void mergeImag(BufferedImage img1, BufferedImage img2, int x, int y, int width, int height) {
		// 获取画笔
		Graphics2D graphics = img1.createGraphics();
		// 合成
		graphics.drawImage(img2, x, y, width, height, null);
		// 释放画笔
		graphics.dispose();
	}

	/**
	 * 指定长和宽对图片进行缩放
	 */
	public static BufferedImage zoomBySize(int width, int height, BufferedImage img) throws IOException {
		// 获取缩放后的实例
		Image _img = img.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		// 创建新画布
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取画笔
		Graphics2D graphics = newImg.createGraphics();
		// 将缩放后的图画到新画布上
		graphics.drawImage(_img, 0, 0, null);
		// 释放画笔
		graphics.dispose();
		return newImg;
	}

}
