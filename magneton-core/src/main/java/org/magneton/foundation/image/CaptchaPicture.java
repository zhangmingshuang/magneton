package org.magneton.foundation.image;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.*;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证码图片.
 *
 * @author zhangmsh 22/03/2022
 * @since 2.0.7
 */
@Builder
public class CaptchaPicture {

	/**
	 * Chinese Pattern
	 */
	private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]+");

	private Path outputPath;

	@Builder.Default
	private int width = 100;

	@Builder.Default
	private int height = 100;

	@Builder.Default
	private int cornerRadius = 100;

	/**
	 * 反向截取
	 */
	@Builder.Default
	private boolean reverseSub = false;

	@Builder.Default
	private int subUserNameLength = 1;

	@Builder.Default
	private int nonChineseSubUserNameLength = 1;

	@Builder.Default
	private boolean chineseIdentify = false;

	@Builder.Default
	private Font defaultFont = new Font("微软雅黑", Font.PLAIN, 50);

	private DrawProcessor drawProcessor;

	public String generateImg(String userName, String imageName) throws IOException {
		return this.generateImg(userName, null, imageName);
	}

	public String generateImg(String userName, @Nullable String prePath, String imageName) throws IOException {
		Preconditions.checkNotNull(userName, "userName must be not null");
		Preconditions.checkNotNull(imageName, "imageName must be not null");

		int subLen = Math.max(1, isChinese(userName) ? this.subUserNameLength : this.nonChineseSubUserNameLength);
		int nameLen = Math.min(subLen, userName.length());
		String written = this.reverseSub ? userName.substring(userName.length() - nameLen)
				: userName.substring(0, nameLen);
		BufferedImage bufferedImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setBackground(this.getRandomColor());
		g2.clearRect(0, 0, this.width, this.height);
		g2.setPaint(Color.WHITE);
		Font font = this.drawProcessor == null ? this.defaultFont
				: this.drawProcessor.font(written, this.width, this.height);
		g2.setFont(font);
		Point point = this.drawProcessor == null ? this.defaultPoint(written, this.width, this.height, font)
				: this.drawProcessor.point(written, this.width, this.height, font);
		g2.drawString(written.toUpperCase(), point.getX(), point.getY());

		Path realPath = Strings.isNullOrEmpty(prePath) ? Paths.get(imageName + ".png")
				: Paths.get(prePath).resolve(imageName + ".png");
		Path realOutPath = this.outputPath.resolve(realPath);
		if (Files.notExists(realOutPath)) {
			Files.createDirectories(realOutPath);
		}
		File file = realOutPath.toFile();
		BufferedImage rounded = this.makeRoundedCorner(bufferedImage, this.cornerRadius);
		ImageIO.write(rounded, "png", file);
		return realPath.toString();
	}

	private Point defaultPoint(String written, int width, int height, Font font) {
		int len = written.length();
		if (len < 1) {
			return new Point(0, 0);
		}
		int size = font.getSize();
		int x = (width - size * len) / 2;
		if (!isChinese(written)) {
			x += width * 0.13;
		}
		int y = (height + (int) (size * 0.75)) / 2;
		return new Point(x, y);
	}

	/**
	 * 获得随机颜色
	 * @return
	 */
	private Color getRandomColor() {
		String[] beautifulColors = new String[] { "232,221,203", "205,179,128", "3,101,100", "3,54,73", "3,22,52",
				"237,222,139", "251,178,23", "96,143,159", "1,77,103", "254,67,101", "252,157,154", "249,205,173",
				"200,200,169", "131,175,155", "229,187,129", "161,23,21", "34,8,7", "118,77,57", "17,63,61", "60,79,57",
				"95,92,51", "179,214,110", "248,147,29", "227,160,93", "178,190,126", "114,111,238", "56,13,49",
				"89,61,67", "250,218,141", "3,38,58", "179,168,150", "222,125,44", "20,68,106", "130,57,53",
				"137,190,178", "201,186,131", "222,211,140", "222,156,83", "23,44,60", "39,72,98", "153,80,84",
				"217,104,49", "230,179,61", "174,221,129", "107,194,53", "6,128,67", "38,157,128", "178,200,187",
				"69,137,148", "117,121,71", "114,83,52", "87,105,60", "82,75,46", "171,92,37", "100,107,48", "98,65,24",
				"54,37,17", "137,157,192", "250,227,113", "29,131,8", "220,87,18", "29,191,151", "35,235,185",
				"213,26,33", "160,191,124", "101,147,74", "64,116,52", "255,150,128", "255,94,72", "38,188,213",
				"167,220,224", "1,165,175", "179,214,110", "248,147,29", "230,155,3", "209,73,78", "62,188,202",
				"224,160,158", "161,47,47", "0,90,171", "107,194,53", "174,221,129", "6,128,67", "38,157,128",
				"201,138,131", "220,162,151", "137,157,192", "175,215,237", "92,167,186", "255,66,93", "147,224,255",
				"247,68,97", "185,227,217" };
		int len = beautifulColors.length;
		Random random = new Random();
		String[] color = beautifulColors[random.nextInt(len)].split(",");
		return new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
	}

	/**
	 * 图片做圆角处理
	 * @param image the image.
	 * @param cornerRadius 拐角半径
	 * @return 处理后的图
	 */
	public BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = output.createGraphics();
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return output;
	}

	public interface DrawProcessor {

		/**
		 * 字体
		 * @param written 要写入的内容
		 * @param imageWide 图片宽
		 * @param imageHeight 图片高
		 * @return 字体
		 */
		Font font(String written, int imageWide, int imageHeight);

		/**
		 * 生成位置
		 * @param written 要写入的内容
		 * @param imageWide 图片宽
		 * @param imageHeight 图片高
		 * @param font 字体
		 * @return 位置
		 */
		Point point(String written, int imageWide, int imageHeight, Font font);

	}

	/**
	 * 判断字符串是否为中文
	 * @param str 待校验字符串
	 * @return 是否为中文
	 */
	public static boolean isChinese(String str) {
		Matcher m = CHINESE_PATTERN.matcher(str);
		return m.find();
	}

	@Setter
	@Getter
	@ToString
	@AllArgsConstructor
	public static class Point {

		private int x;

		private int y;

	}

}
