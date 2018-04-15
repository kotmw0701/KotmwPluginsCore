package jp.kotmw.core.nms.particle.magicsquare;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.Gson;

import jp.kotmw.core.Polar_Coordinate2D;
import jp.kotmw.core.Polar_Coordinate3D;
import jp.kotmw.core.api.particle.EnumParticle;
import jp.kotmw.core.api.particle.ParticleRunnable;
import jp.kotmw.core.nms.DetailsColor;

public class Magic_square extends ParticleRunnable {
	
	/*
	 * メモ
	 * Graphics2かなんかで図形作ってから描画っての方が楽かもしれん
	 * ただメモリ使用量は増えそうだが
	 * 文字列の描画をしたい場合には楽・・・だと思うというかGraphics2Dしか出来なくない？
	 * ちょっと面倒になる
	 * 
	 */
	private long ave;
	
	protected String color;
	protected List<Double> params = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0));
	protected List<ShapeData> shapes = new ArrayList<>();
	
	protected Magic_square() {
		this(null, "");
	}
	
	public Magic_square(Location location, String json) {
		super(EnumParticle.REDSTONE, location, 0, 0, 0, 0, 0);
		if(location == null) return;
		Magic_square square = new Gson().fromJson(json, Magic_square.class);
		particle = square.particle;
		color = square.color;
		shapes = square.shapes;
		params = square.params;
		this.location = location;
		if(params.size() < 4) {
			params.addAll(Arrays.asList(0.0, 0.0, 0.0, 0.0, 0.0));//もしサイズが0だったときのためのエラー回避で5つ入るようにする・・・そんなこと無いと思うがな
		}
	}

	public void average() {
		System.out.println("平均描画時間: "+((ave/100) / 1000000f)+"ms");
		System.out.println("-----------------------------------------");
	}
	
	public void show() {
		for(ShapeData data : shapes) {
			boolean isposition = data.getPosition() != null, isrepeat = isposition ? data.getPosition().getRepeat() != null : false;
			particle = data.getParticle() != null ? data.getParticle() : this.particle;
			if(particle.hasColorParticle()) particle.setColor(new DetailsColor(data.getColor() != null ? data.getColor() : color));
			double paramrepeat = isposition ? isrepeat ? data.getPosition().getRepeat().getAngle() : 2*Math.PI : 2*Math.PI;
			int limitcount = isposition ? isrepeat ? data.getPosition().getRepeat().getLimit()-1 : 0 : 0;
			double max = limitcount > 0 ? paramrepeat*limitcount < 2*Math.PI ? paramrepeat*limitcount : 2*Math.PI : 2*Math.PI;
			setParams(data.params.get(0).floatValue(), data.params.get(1).floatValue(), data.params.get(2).floatValue(), (data.params.size() < 4 ? particle.hasColorParticle() ? 1 : 0 : 0) , (data.params.size() < 5 ? 0 : data.params.get(4).intValue()));
			for(double theta = 0.0; theta <= max ; theta += paramrepeat) {
				Location center = data.getPosition() != null ? location.clone().add(data.getPosition().getPolar_Coordinates(location.getWorld()).add(0, theta, 0).convertLocation()) : this.location.clone();
				switch (data.getType()) {
				case CIRCLE: circle(data, center);
					break;
				case POLYGON: polygon(data, center);
					break;
				case STAR: star(data, center);
					break;
				case LINE: line(data, center);
					break;
				}
			}
		}
	}
	
	//num 星型多角形
	private void star(ShapeData data, Location location) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/(data.getQuantity()/2)) {
			Polar_Coordinate3D pc = new Polar_Coordinate3D(location.getWorld(), data.getRadius(), theta+(data.getPosition() != null ? data.getPosition().getAngle() : 0), 0);
			double max = pc.convertLocation().distance(pc.clone().add(0, Math.PI/(data.getQuantity()/2)*2, 0).convertLocation());
			for(double line = 0.0; line <= max; line += 0.2)
				sendParticle(location.clone().add(pc.convertLocation().add(pc.clone().add(line-data.getRadius(), ((2+(data.getQuantity()/2))*Math.PI/((data.getQuantity()/2)*2)), 0).convertLocation())));
			//中心座標 + パーティクル開始座標 + 移動後の表示する1点の座標
		}
	}
	
	//num 多角形
	private void polygon(ShapeData data, Location location) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/(data.getQuantity()/2)) {
			Polar_Coordinate3D pc = new Polar_Coordinate3D(location.getWorld(), data.getRadius(), theta+(data.getPosition() != null ? data.getPosition().getAngle() : 0), 0);
			double max = pc.convertLocation().distance(pc.clone().add(0, (Math.PI/(data.getQuantity()/2)), 0).convertLocation());
			for(double line = 0.0; line <= max; line += 0.2)
				sendParticle(location.clone().add(pc.convertLocation().add(pc.clone().add(line-data.getRadius(), Math.PI*((data.getQuantity()/2)+1)/(2*(data.getQuantity()/2)), 0).convertLocation())));
		}
	}
	
	//丸
	private void circle(ShapeData data, Location location) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/50)
			sendParticle(location.clone().add(new Polar_Coordinate3D(location.getWorld(), data.getRadius(), theta, 0).convertLocation()));
	}
	
	//線
	private void line(ShapeData data, Location location) {
		double angle = data.getPosition() == null ? 0 : data.getPosition().getAngle();
		for(double radius = 0; radius <= data.getRadius(); radius += 0.2)
			sendParticle(location.clone().add(new Polar_Coordinate3D(location.getWorld(), radius, angle, 0).convertLocation()));
	}
	
	private void sendParticle(Location center) {
		sendParticle(Bukkit.getOnlinePlayers(), center);
	}
	
	/*Graphics2D使うパターン*/
	
	private BufferedImage bufferedImage;
	private Graphics2D graphics2d;
	private final Point2D.Double center = new Point2D.Double(500, 500);
	
	public void draw() {
		bufferedImage = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
		graphics2d = bufferedImage.createGraphics();
		showgrid();
		graphics2d.setColor(Color.BLACK);
		for(ShapeData data : shapes) {
			boolean isposition = data.getPosition() != null, isrepeat = isposition ? data.getPosition().getRepeat() != null : false;
			double paramrepeat = isposition ? isrepeat ? data.getPosition().getRepeat().getAngle() : 2*Math.PI : 2*Math.PI;
			int limitcount = isposition ? isrepeat ? data.getPosition().getRepeat().getLimit()-1 : 0 : 0;
			double max = limitcount > 0 ? paramrepeat*limitcount < 2*Math.PI ? paramrepeat*limitcount : 2*Math.PI : 2*Math.PI;
			setParams(data.params.get(0).floatValue(), data.params.get(1).floatValue(), data.params.get(2).floatValue(), (data.params.size() < 4 ? particle.hasColorParticle() ? 1 : 0 : 0) , (data.params.size() < 5 ? 0 : data.params.get(4).intValue()));
			for(double theta = 0.0; theta <= max-paramrepeat ; theta += paramrepeat) {
				Point2D newcenter = data.getPosition() != null ? getMovedCenter(data.getPosition().getPolar_Coordinates(location.getWorld()).get2D().add(0, theta)) : center;
				switch (data.getType()) {
				case CIRCLE:
					circle(data, newcenter);
					break;
				case LINE:
					line(data, newcenter);
					break;
				case POLYGON:
					polygon(data, newcenter);
					break;
				case STAR:
					star(data, newcenter);
					break;
				}
			}
		}
		try {
			ImageIO.write(bufferedImage, "png", new File("test.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * メモ: 
	 * 次の点の算出方法: Math.PI/(data.getQuantity()/2)*2
	 * 次の点に向かう角度の算出方法 ((2+(data.getQuantity()/2))*Math.PI/((data.getQuantity()/2)*2))
	 * 
	 * 偶数角形  >No> そのまま処理
	 * \/Yes
	 * 今の座標は偶数回めか >No> 
	 * \/Yes
	 * 普通に代入
	 */
	private void star(ShapeData data, Point2D center) {
		graphics2d.setColor(Color.BLUE);
		double quantity = data.getQuantity(), add = Math.PI/(quantity/2);
		for(double theta = 0.0; theta <= 2*Math.PI-add; theta += add) {
			System.out.println("draw");
			Polar_Coordinate2D pc2d = new Polar_Coordinate2D(data.getRadius()*10, theta+(data.getPosition() != null ? data.getPosition().getAngle() : 0));
			double max = pc2d.convertLocation().distance(pc2d.clone().add(0, Math.PI/(data.getQuantity()/2)*2).convertLocation());
			Point2D pos1 = pc2d.convertLocation(500, 500), pos2 = pc2d.add(max, ((2+(data.getQuantity()/2))*Math.PI/((data.getQuantity()/2)*2))).convertLocation(500, 500);
			graphics2d.draw(new Line2D.Double(pos1, pos2));
		}
	}
	
	private void polygon(ShapeData data, Point2D center) {
		int i = 0;
		double quantity = data.getQuantity(), add = Math.PI/(quantity/2);
		int[] x = new int[(int) quantity], y = new int[x.length];
		graphics2d.setColor(Color.GREEN);
		for(double theta = 0.0; theta <= 2*Math.PI-add; theta += add) {
			Point2D point = new Polar_Coordinate2D(data.getRadius()*10, theta+(data.getPosition() != null ? data.getPosition().getAngle() : 0)).convertLocation();
			x[i] = (int) Math.round(center.getX()+point.getX());
			y[i] = (int) Math.round(center.getY()+point.getY());
			i++;
		}
		graphics2d.drawPolygon(x, y, i);
	}
	
	private void circle(ShapeData data, Point2D center) {
		int size = (int)(data.getRadius()*2)*10;
		graphics2d.drawOval((int)Math.round(center.getX()-(size/2)), (int)Math.round(center.getY()-(size/2)), size, size);
	}
	
	private void line(ShapeData data, Point2D center) {
		
	}
	
	private Point2D getMovedCenter(Polar_Coordinate2D moved) {
		Point2D.Double movedpos = moved.convertLocation();
		double x = 500+movedpos.getX()*10, y = 500+movedpos.getY()*10;
		return new Point2D.Double(x, y);
	}
	
	private void showgrid() {
		graphics2d.setColor(Color.LIGHT_GRAY);
		for(int i = 0; i < 100; i++) {
			graphics2d.drawLine(i*10, 0, i*10, 1000);
			graphics2d.drawLine(0, i*10, 1000, i*10);
		}
		graphics2d.setColor(Color.DARK_GRAY);
		for(int i = 0; i < 10; i++) {
			graphics2d.drawLine(i*100, 0, i*100, 1000);
			graphics2d.drawLine(0, i*100, 1000, i*100);
		}
		graphics2d.setColor(Color.RED);
		graphics2d.drawLine(500, 500, 500, 500);
	}
}
