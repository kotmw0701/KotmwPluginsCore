package jp.kotmw.core.nms.particle.magicsquare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.Gson;

import jp.kotmw.core.Polar_coordinate;
import jp.kotmw.core.api.particle.EnumParticle;
import jp.kotmw.core.api.particle.ParticleRunnable;
import jp.kotmw.core.nms.DetailsColor;

public class Magic_square extends ParticleRunnable{
	
	/*
	 * メモ
	 * Graphics2かなんかで図形作ってから描画っての方が楽かもしれん
	 * ただメモリ使用量は増えそうだが
	 * 文字列の描画をしたい場合には楽・・・だと思うというかGraphics2Dしか出来なくない？
	 * ちょっと面倒になる
	 * 
	 */
	private Location location;
	private long ave;
	
	protected EnumParticle particle;
	protected String color;
	protected List<Double> params = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0));
	protected List<ShapeData> shapes = new ArrayList<>();
	
	protected Magic_square() {
		this(null, "");
	}
	
	public Magic_square(Location location, String json) {
		super(null, location, 0, 0, 0, 0, 0);
		if(location == null) return;
		Magic_square square = new Gson().fromJson(json, Magic_square.class);
		particle = square.particle;
		color = square.color;
		shapes = square.shapes;
		params = square.params;
		this.location = location;
		if(params.size() < 4) params.addAll(Arrays.asList((particle.hasColorParticle() ? 1.0 : 0.0), 0.0, 0.0, 0.0, 0.0));//もしサイズが0だったときのためのエラー回避で5つ入るようにする・・・そんなこと無いと思うがな
	}

	public void average() {
		System.out.println("平均描画時間: "+((ave/100) / 1000000f)+"ms");
		System.out.println("-----------------------------------------");
	}
	
	public void show() {
		for(ShapeData data : shapes) {
			long start = System.nanoTime();
			EnumParticle particle = data.getParticle() != null ? data.getParticle() : this.particle;
			if(particle.hasColorParticle()) particle.setColor(new DetailsColor(data.getColor() != null ? data.getColor() : color));
			setParticle(particle);
			double paramrepeat = data.getPosition() != null ? data.getPosition().getRepeat() != null ? data.getPosition().getRepeat().getAngle() : 2*Math.PI : 2*Math.PI;
			int limitcount = data.getPosition() != null ? data.getPosition().getRepeat() != null ? data.getPosition().getRepeat().getLimit()-1 : 0 : 0;
			double max = limitcount > 0 ? paramrepeat*limitcount < 2*Math.PI ? paramrepeat*limitcount : 2*Math.PI : 2*Math.PI;
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
			long end = System.nanoTime();
			System.out.println(data.getType()+"の処理時間: "+(end-start)/1000000f+"ms");
			ave += (end-start);
		}
	}
	
	//num 星型多角形
	private void star(ShapeData data, Location location) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/(data.getQuantity()/2)) {
			Polar_coordinate pc = new Polar_coordinate(location.getWorld(), data.getRadius(), theta+(data.getPosition() != null ? data.getPosition().getAngle() : 0), 0);
			double max = pc.convertLocation().distance(pc.clone().add(0, Math.PI/(data.getQuantity()/2)*2, 0).convertLocation());
			for(double line = 0.0; line <= max; line += 0.2)
				sendParticle(location.clone().add(pc.convertLocation().add(pc.clone().add(line-data.getRadius(), ((2+(data.getQuantity()/2))*Math.PI/((data.getQuantity()/2)*2)), 0).convertLocation())));
		}
	}
	
	//num 多角形
	private void polygon(ShapeData data, Location location) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/(data.getQuantity()/2)) {
			Polar_coordinate pc = new Polar_coordinate(location.getWorld(), data.getRadius(), theta+(data.getPosition() != null ? data.getPosition().getAngle() : 0), 0);
			double max = pc.convertLocation().distance(pc.clone().add(0, (Math.PI/(data.getQuantity()/2)), 0).convertLocation());
			for(double line = 0.0; line <= max; line += 0.2)
				sendParticle(location.clone().add(pc.convertLocation().add(pc.clone().add(line-data.getRadius(), Math.PI*((data.getQuantity()/2)+1)/(2*(data.getQuantity()/2)), 0).convertLocation())));
		}
	}
	
	//丸
	private void circle(ShapeData data, Location location) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/60)
			sendParticle(location.clone().add(new Polar_coordinate(location.getWorld(), data.getRadius(), theta, 0).convertLocation()));
	}
	
	//線
	private void line(ShapeData data, Location location) {
		double angle = data.getPosition() == null ? 0 : data.getPosition().getAngle();
		for(double radius = 0; radius <= data.getRadius(); radius += 0.2)
			sendParticle(location.clone().add(new Polar_coordinate(location.getWorld(), radius, angle, 0).convertLocation()));
	}
	
	private void sendParticle(Location center) {
		setLocation(center);
		setParams(params.get(0).floatValue(), params.get(1).floatValue(), params.get(2).floatValue(), params.get(3).floatValue(), (params.size() < 5 ? 0 : params.get(4).intValue()));
		sendParticle(Bukkit.getOnlinePlayers());
	}
}
