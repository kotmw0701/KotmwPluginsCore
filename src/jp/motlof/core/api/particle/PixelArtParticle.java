package jp.motlof.core.api.particle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import jp.motlof.core.api.DetailsColor;
import jp.motlof.core.api.Polar_coodinates;
import jp.motlof.core.api.particle.ParticleAPI.EnumParticle;
import jp.motlof.core.api.particle.ParticleAPI.Particle;

public class PixelArtParticle {
	
	private Location location;
	private BufferedImage image;
	private int w, h;
	private double separate = 0.2;
	private boolean applyColor = true;
	private EnumParticle enumParticle;
	
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
		stringParticle(string, location, separate);
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
		stringParticle(string, location, separate);
	}
	
	private void stringParticle(String string, Location location, double separate) {
		this.location = location.clone();
		this.separate = (separate <= 0) ? 0.2 : separate;//0以下だったら強制的に0.2にする
		w = string.length()*16;//1文字が16x16pxと仮定して、文字数x16px
		h = count(string, "%n")*16;//改行する分高さの16を掛ける
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);//算出した縦横数値をもとにベースとなる画像を生成
		Graphics2D graphics2d = image.createGraphics();//クソ、使いにくいこれ、もっと良いの無いの
		graphics2d.setBackground(new Color(255, 255, 255, 0));//背景色を"透明"に
		graphics2d.setColor(Color.black);//文字色を黒色に
		int i = 0;
		for(String string2 : string.split("%n"))
			graphics2d.drawString(string2.replaceAll("_", " "), 0, 14+(16*++i));
	}
	
	public void show() {
		DetailsColor color = new DetailsColor(0, 0, 0);
		for(int x = 0; x < w; x++) {
			for(int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);
				if(new Color(pixel, true).getAlpha() == 0)//Alpha値が0(=透明)だった場合は出力しない
					continue;
				if(applyColor) color = new DetailsColor((pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF);
				Polar_coodinates pCoodinates = new Polar_coodinates(new Location(location.getWorld(), x*separate, y*separate, 0)).add(0, 0, Math.toRadians(180));
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
	
	private void sendParticle(Location location, DetailsColor color) {
		for(Player player : Bukkit.getOnlinePlayers())
			if(player.getWorld().getName().equalsIgnoreCase(location.getWorld().getName()))
				new Particle(enumParticle, location, color.getRed(), color.getGreen(), color.getBlue(), 1, 0).sendParticle(player);
	}
	
}
