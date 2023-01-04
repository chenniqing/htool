package cn.javaex.htool.picture;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import cn.javaex.htool.core.codec.Base64Utils;

/**
 * 图片工具类
 * 
 * @author 陈霓清
 * @Date 2023年1月4日
 */
public class ImageUtils {
    /**
     * 改变 图片 最低 宽度
     */
    private static final int MIN_PIC_WIDTH = 10;
    /**
     * 改变 图片 最低 高度
     */
    private static final int MIN_PIC_HEIGHT = 10;

    /**
     * 旋转角度
     */
    private static final int PIC_ANGEL = 315;
    /**
     * 每个水印水平间隔
     */
    private static final int X_PADDING = 150;
    /**
     * 每个水印垂直间隔
     */
    private static final int Y_PADDING = 200;

    /**
     *  数值 2
     */
    private static final int NUM_TWO = 2;

    /**
     * 添加水印（水印在右下角）
     * @param srcBuffer
     * @param text 水印内容，例如 https://www.javaex.cn
     * @param font 水印字体，例如 Font font = new Font("宋体", Font.BOLD, 26);
     * @param color 水印颜色，为null时，显示白色水印
     * @return
     */
    public static BufferedImage addWatermark(BufferedImage srcBuffer, String text, Font font, Color color) {
        try {
            int srcImgWidth = srcBuffer.getWidth();
            int srcImgHeight = srcBuffer.getHeight();
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcBuffer, 0, 0, srcImgWidth, srcImgHeight, null);
            if (color==null) {
            	g.setColor(Color.WHITE);
            } else {
            	g.setColor(color);
            }
            g.setFont(font);
            
            // 水印串宽度
            int stringWidth = g.getFontMetrics(g.getFont()).charsWidth(text.toCharArray(), 0, text.length());
            // 水印透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            // 设置水印的坐标
            int x = srcImgWidth - stringWidth - 20;
            int y = srcImgHeight - 20;
            // 从坐标x,y开始添加水印
            g.drawString(text, x, y);
            // 释放资源
            g.dispose();
            
            return bufImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return srcBuffer;
    }
    
    /**
     * 添加水印（铺满）
     * @param srcBuffer
     * @param text 水印内容，例如 https://www.javaex.cn
     * @param font 水印字体，例如 Font font = new Font("宋体", Font.BOLD, 26);
     * @param color 水印颜色，为null时，显示白色水印
     * @return
     */
    public static BufferedImage addWatermarks(BufferedImage srcBuffer, String text, Font font, Color color) {
        try {
            int srcImgWidth = srcBuffer.getWidth();
            int srcImgHeight = srcBuffer.getHeight();
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcBuffer, 0, 0, srcImgWidth, srcImgHeight, null);
            if (color==null) {
            	g.setColor(Color.WHITE);
            } else {
            	g.setColor(color);
            }
            g.setFont(font);
            FontRenderContext frc = g.getFontRenderContext();
            TextLayout tl = new TextLayout(text, font, frc);
            // 水印串宽度
            int stringWidth = g.getFontMetrics(g.getFont()).charsWidth(text.toCharArray(), 0, text.length());
            // 旋转水印
            g.rotate(Math.toRadians(PIC_ANGEL), (double) srcImgWidth / NUM_TWO, (double) srcImgHeight / NUM_TWO);
            // 水印透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            // 设置水印的坐标
            int x = -srcImgHeight / NUM_TWO;
            int y;
            // 循环绘制
            while (x < srcImgWidth + srcImgHeight / NUM_TWO) {
                y = -srcImgWidth / NUM_TWO;
                while (y < srcImgHeight + srcImgWidth / NUM_TWO) {
                    Shape sha = tl.getOutline(AffineTransform.getTranslateInstance(x, y));
                    g.fill(sha);

                    y += Y_PADDING;
                }
                x += stringWidth + X_PADDING;
            }
            // 释放资源
            g.dispose();
            
            return bufImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return srcBuffer;
    }
    
	/**
	 * base64转url
	 * @param base64Image
	 * @param imageFilePath : "D:\\test\\1.jpg"
	 * @return
	 * @throws IOException
	 */
	public static void base64ToFile(String base64Image, String imageFilePath) throws IOException {
		String image = base64Image.substring(22);
		if (image.startsWith(",")) {
			image = base64Image.substring(23);
		}
		
		byte[] bytes = Base64Utils.decodeByte(image);
		
		try (OutputStream outStream = new FileOutputStream(imageFilePath)) {
			outStream.write(bytes);
			outStream.flush();
		}
	}
	
	/**
	 * 根据图片地址转换为base64编码字符串
	 * @param imageFilePath : "D:\\test\\1.jpg"
	 * @return
	 */
	public static String toBase64Str(String imageFilePath) {
		byte[] bytes = null;
		
		try (InputStream inputStream = new FileInputStream(imageFilePath)) {
			bytes = new byte[inputStream.available()];
			inputStream.read(bytes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 返回Base64编码过的字节数组字符串
		return "data:image/jpeg;base64," + Base64Utils.encodeByte(bytes);
	}
	
	/**
	 * 图片路径转BufferedImage
	 * @param imageFilePath
	 * @return
	 * @throws IOException 
	 */
	public static BufferedImage read(String imageFilePath) throws IOException {
		return ImageIO.read(new File(imageFilePath));
	}
	
	/**
	 * BufferedImage转图片路径
	 * @param bufferedImage
	 * @param imageFilePath : "D:\\test\\2.jpg"
	 * @param imageType : "jpg"
	 * @throws IOException
	 */
	public static void write(BufferedImage bufferedImage, String imageFilePath, String imageType) throws IOException {
		ImageIO.write(bufferedImage, imageType, new File(imageFilePath));
	}
	
	/**
	 * 得到网页中图片的地址
	 * @param htmlStr
	 * @return
	 */
	public static List<String> listImages(String htmlStr) {
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("<(img|IMG)(.*?)(>|></img>|/>)");
		Matcher matcher = p.matcher(htmlStr);
		boolean hasPic = matcher.find();
		
		// 判断是否含有图片
		if (hasPic) {
			// 如果含有图片，那么持续进行查找，直到匹配不到
			while (hasPic) {
				// 获取第二个分组的内容，也就是 (.*?)匹配到的
				String group = matcher.group(2);
				// 匹配图片的地址
				Pattern srcText = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
				Matcher matcher2 = srcText.matcher(group);
				if (matcher2.find()) {
					try {
						list.add(matcher2.group(3));
					} catch (Exception e) {
						
					}
				}
				// 判断是否还有img标签
				hasPic = matcher.find();
			}
		}
		
		return list;
	}
	
	/**
	 * 图片压缩（自定义尺寸）
	 * @param srcBuffer 要缩放的图片
	 * @param width 目标宽度像素
	 * @param height 目标高度像素
	 */
	public final static BufferedImage compress(BufferedImage srcBuffer, int width, int height) {
		if (width < MIN_PIC_WIDTH) {
			width = MIN_PIC_WIDTH;
		}
		if (height < MIN_PIC_HEIGHT) {
			height = MIN_PIC_HEIGHT;
		}
		
		double ratio = 0.0; // 缩放比例
		// bi.SCALE_SMOOTH 选择图像平滑度比缩放速度具有更高优先级的图像缩放算法
		Image itemp = srcBuffer.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		// 计算比例
		if ((srcBuffer.getHeight()>height) || (srcBuffer.getWidth()>width)) {
			double ratioHeight = (new Integer(height)).doubleValue() / srcBuffer.getHeight();
			double ratioWhidth = (new Integer(width)).doubleValue() / srcBuffer.getWidth();
			if (ratioHeight > ratioWhidth) {
				ratio = ratioHeight;
			} else {
				ratio = ratioWhidth;
			}
			// 仿射转换
			// 返回表示剪切变换的变换
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
			// 转换源 BufferedImage 并将结果存储在目标 BufferedImage 中
			itemp = op.filter(srcBuffer, null);
		}
		
		// 补白
		// 构造一个类型为预定义图像类型之一的 BufferedImage
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		//创建一个 Graphics2D，可以将它绘制到此 BufferedImage 中
		Graphics2D g = image.createGraphics();
		g.setColor(Color.white);    //控制颜色
		g.fillRect(0, 0, width, height);    // 使用 Graphics2D 上下文的设置，填充 Shape 的内部区域
		if (width == itemp.getWidth(null)) {
			g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2,
					itemp.getWidth(null), itemp.getHeight(null),
					Color.white, null);
		} else {
			g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0,
					itemp.getWidth(null), itemp.getHeight(null),
					Color.white, null);
		}
		
		g.dispose();
		itemp = image;
		return (BufferedImage) itemp;
	}
	
	/**
	 * 图片压缩（等比例压缩）
	 * @param srcBuffer 要缩放的图片
	 * @param quality 压缩率（0~1之间）
	 */
	public final static BufferedImage compress(BufferedImage srcBuffer, float quality) {
		if (quality <= 0 || quality > 1) {
            System.err.println("参数不合法");
            return null;
        }
		
		int width = (int) (srcBuffer.getWidth() * quality);
		int height = (int) (srcBuffer.getHeight() * quality);
		
		return compress(srcBuffer, height, width);
	}
	
	/**
	 * 裁剪图片方法
	 * @param bufferedImage 图像源
	 * @param startX 裁剪开始x坐标
	 * @param startY 裁剪开始y坐标
	 * @param endX 裁剪结束x坐标
	 * @param endY 裁剪结束y坐标
	 * @return
	 */
	public static BufferedImage cut(BufferedImage bufferedImage, int startX, int startY, int endX, int endY) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		if (startX == -1) {
			startX = 0;
		}
		if (startY == -1) {
			startY = 0;
		}
		if (endX == -1) {
			endX = width - 1;
		}
		if (endY == -1) {
			endY = height - 1;
		}
		BufferedImage result = new BufferedImage(endX-startX, endY-startY, 4);
		for (int x=startX; x<endX; ++x) {
			for (int y=startY; y<endY; ++y) {
				int rgb = bufferedImage.getRGB(x, y);
				result.setRGB(x-startX, y-startY, rgb);
			}
		}
		return result;
	}
	
}
