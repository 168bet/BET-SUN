package com.wecode.game.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtils {

	private static Random random = new Random();
	private static String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrst";// 随机产生的字符串

	private static int width = 80;// 图片宽
	private static int height = 26;// 图片高
	private static int lines = 40;// 干扰线数量

	public static String genCode(int charCount) {
		String randomString = "";
		for (int i = 0; i < charCount; ++i) {
			randomString += randString.charAt(random.nextInt(randString.length()));
		}
		return randomString;
	}

	public static BufferedImage genCaptcha(String code) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 18));
		g.setColor(getRandColor(110, 133));
		// 绘制随机字符
		drowCode(g, code);
		// 绘制干扰线
		for (int i = 0; i <= lines; i++) {
			drowLine(g);
		}
		g.dispose();
		return image;
	}

	/*
	 * 绘制字符串
	 */
	private static String drowCode(Graphics g, String code) {
		int margin = width / code.length();
		for (int i = 0; i < code.length(); ++i) {
			g.setFont(getFont());
			g.setColor(getRandColor(0, 100));
			g.translate(random.nextInt(3), random.nextInt(3));
			g.drawString(String.valueOf(code.charAt(i)), margin * i, 16);
		}
		return code;
	}

	/*
	 * 绘制干扰线
	 */
	private static void drowLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(13);
		int yl = random.nextInt(15);
		g.drawLine(x, y, x + xl, y + yl);
	}

	/*
	 * 获得字体
	 */
	private static Font getFont() {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 18);
	}

	/*
	 * 获得颜色
	 */
	private static Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc - 16);
		int g = fc + random.nextInt(bc - fc - 14);
		int b = fc + random.nextInt(bc - fc - 18);
		return new Color(r, g, b);
	}
}
