package jp.kotmw.core.nms.particle;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import jp.kotmw.core.nms.DetailsColor;
import jp.kotmw.core.nms.Polar_coordinates;
import jp.kotmw.core.nms.particle.ParticleAPI.EnumParticle;
import jp.kotmw.core.nms.particle.ParticleAPI.Particle;

public class PixelArtParticle {

	private Location location;
	private BufferedImage image;
	private int w, h;
	private double separate = 0.2;
	private boolean applyColor = true;
	private EnumParticle enumParticle = EnumParticle.REDSTONE;
	
	private Font font = new Font(Font.SERIF, Font.PLAIN, 12);
	
	/**
	 * 画像からパーティクルを生成するコンストラクタ
	 * 
	 * @param file 対象の画像ファイル
	 * @param location 開始点座標
	 * @throws IOException ファイルのエラー
	 */
	public PixelArtParticle(File file, Location location) throws IOException {
		imageParticle(file, location, separate, applyColor);
	}
	
	/**
	 * 画像からパーティクルを生成するコンストラクタ
	 * 
	 * @param file 対象の画像ファイル
	 * @param location 開始点座標
	 * @param separate パーティクル同士の間隔(推奨: 0.10～0.20)
	 * @throws IOException ファイルのエラー
	 */
	public PixelArtParticle(File file, Location location, double separate) throws IOException {
		imageParticle(file, location, separate, applyColor);
	}
	
	/**
	 * 画像からパーティクルを生成するコンストラクタ
	 * 
	 * @param file 対象の画像ファイル
	 * @param location 開始点座標
	 * @param separate パーティクル同士の間隔(推奨: 0.10～0.20)
	 * @param applyColor 画像の色を反映させるか
	 * @throws IOException ファイルのエラー
	 */
	public PixelArtParticle(File file, Location location, double separate, boolean applyColor) throws IOException {
		imageParticle(file, location, separate, applyColor);
	}
	
	private void imageParticle(File file, Location location, double separate, boolean applyColor) throws IOException {
		this.location = location.clone();
		this.separate = (separate <= 0) ? 0.2 : separate;//0以下だったら強制的に0.2にする
		this.applyColor = applyColor;
		image = ImageIO.read(file);//Fileから画像取得
		w = image.getWidth();
		h = image.getHeight();
	}
	
	/**
	 * 文字列からパーティクルを生成するコンストラクタ<br>
	 * "_"が空白、"%n"が改行(デフォルト)
	 * 
	 * @param string 出力したい文字列
	 * @param location 開始点座標
	 */
	public PixelArtParticle(String string, Location location) {
		try {
			stringParticle(string, location, separate, enumParticle, font);
		} catch (IllegalArgumentException e) {}//握りつぶしたくはないけど、デフォルトフォントじゃエラーは発生しないから
	}
	
	/**
	 * 文字列からパーティクルを生成するコンストラクタ<br>
	 * "_"が空白、"%n"が改行(デフォルト)
	 * 
	 * @param string 出力したい文字列
	 * @param location 開始点座標
	 * @param separate パーティクル同士の間隔(推奨: 0.10～0.20)
	 */
	public PixelArtParticle(String string, Location location, double separate) {
		try {
			stringParticle(string, location, separate, enumParticle, font);
		} catch (IllegalArgumentException e) {}//握りつぶしたくはないけど、デフォルトフォントじゃエラーは発生しないから
	}
	
	/**
	 * 文字列からパーティクルを生成するコンストラクタ<br>
	 * "_"が空白、"%n"が改行(デフォルト)
	 * 
	 * @param string 出力したい文字列
	 * @param location 開始点座標
	 * @param separate パーティクル同士の間隔(推奨: 0.10～0.20)
	 * @param enumParticle Particleの種類
	 */
	public PixelArtParticle(String string, Location location, double separate, EnumParticle enumParticle) {
		try {
			stringParticle(string, location, separate, enumParticle, font);
		} catch (IllegalArgumentException e) {}//握りつぶしたくはないけど、デフォルトフォントじゃエラーは発生しないから
	}
	
	/**
	 * 文字列からパーティクルを生成するコンストラクタ<br>
	 * "_"が空白、"%n"が改行(デフォルト)
	 * 
	 * @param string 出力したい文字列
	 * @param location 開始点座標
	 * @param separate パーティクル同士の間隔(推奨: 0.10～0.20)
	 * @param enumParticle Particleの種類
	 * @param font 出力したいフォント、デフォルトは(SERIF, PLAIN, 12)
	 * @throws Exception 使用することが出来ないフォントが設定されている
	 */
	public PixelArtParticle(String string, Location location, double separate, EnumParticle enumParticle, Font font) throws IllegalArgumentException {
		stringParticle(string, location, separate, enumParticle, font);
	}
	
	private void stringParticle(String string, Location location, double separate, EnumParticle enumParticle, Font font) throws IllegalArgumentException {
		this.location = location.clone();
		this.enumParticle = enumParticle;
		this.separate = (separate <= 0) ? 0.2 : separate;//0以下だったら強制的に0.2にする
		FontMetrics fontMetrics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics().getFontMetrics(font);//文字列の縦横比を取るため、Fontのメトリクスを取得
		int count = count(string, "%n");//改行回数
		w = fontMetrics.stringWidth(maxWidthString(string, "%n"));//最大の長さの文字列を出して、そこから横幅を算出
		h = fontMetrics.getHeight()*count;//高さx改行回数
		if(w == 0 || h == 0)//どちらかが0だと描画さえされないため、使用不可能
			throw new IllegalArgumentException("このフォントは使用することが出来ません");
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);//算出した縦横数値をもとにベースとなる画像を生成
		Graphics2D graphics2d = image.createGraphics();//クソ、使いにくいこれ、もっと良いの無いの
		graphics2d.setFont(font);//フォント設定
		graphics2d.setBackground(new Color(255, 255, 255, 0));//背景色を"透明"に
		graphics2d.setColor(Color.black);//文字色を黒色に
		int i = 0;
		for(String string2 : string.split("%n"))
			graphics2d.drawString(string2.replaceAll("_", " "), 0, fontMetrics.getAscent()+fontMetrics.getLeading()+(fontMetrics.getHeight()*++i));
	}
	
	public void show() {
		show(0, 0, 0);
	}
	
	public void show(double x_rotation, double y_rotation, double z_rotation) {
		DetailsColor color = new DetailsColor(0, 0, 0);
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);
				if(new Color(pixel, true).getAlpha() == 0)//Alpha値が0(=透明)だった場合は出力しない
					continue;
				if(applyColor) color = new DetailsColor((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF);
				Polar_coordinates pCoodinates = new Polar_coordinates(new Location(location.getWorld(), x*separate, y*separate, 0)).add(0, 0, Math.toRadians(180));
				sendParticle(location.clone().add(pCoodinates.convertLocation()), color);
			}
		}
	}
	
	/**
	 * targetの文字列が何回入っているかを取得
	 * 
	 * @param string 元の文字列
	 * @param target カウントしたい文字列
	 * @return 幾つ含まれていたか
	 */
	private int count(String string, String target) {
		int i = (string.length() - string.replaceAll(target, "").length() / target.length());
		return i <= 0 ? 1 : i; 
	}
	
	/**
	 * 全部改行した時に、一番長い文字列を返す
	 * 
	 * @param string 元の文字列
	 * @param target 改行コード
	 * @return 全部改行して、一番長い文字列
	 */
	private String maxWidthString(String string, String target) {
		String maxString = string;
		int maxbyte = 1;
		for(String str : string.split(target))
			if(maxbyte < str.getBytes().length) {
				maxbyte = str.getBytes().length;
				maxString = str;
			}
		return maxString;
	}
	
	private void sendParticle(Location location, DetailsColor color) {
		for(Player player : Bukkit.getOnlinePlayers())
			if(player.getWorld().getName().equalsIgnoreCase(location.getWorld().getName()))
				new Particle(enumParticle, location, color.getRed(), color.getGreen(), color.getBlue(), 1, 0).sendParticle(player);
	}
	
}
