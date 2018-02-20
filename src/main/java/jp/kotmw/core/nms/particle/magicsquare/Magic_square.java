package jp.kotmw.core.nms.particle.magicsquare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.google.gson.Gson;

import jp.kotmw.core.nms.DetailsColor;
import jp.kotmw.core.nms.Polar_coordinates;
import jp.kotmw.core.nms.particle.ParticleAPI;
import jp.kotmw.core.nms.particle.ParticleAPI.EnumParticle;

public class Magic_square {
	
	/*
	 * メモ
	 * Graphics2かなんかで図形作ってから描画っての方が楽かもしれん
	 * ただメモリ使用量は増えそうだが
	 * 文字列の描画をしたい場合には楽・・・だと思うというかGraphics2Dしか出来なくない？
	 * ちょっと面倒になる
	 * 
	 */
	
	private Location location;
	private DetailsColor detailsColor;
	//private long ave;
	
	protected EnumParticle particle;
	protected String color;
	protected List<Double> params = new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0));
	protected List<ShapeData> shapes = new ArrayList<>();
	
	protected Magic_square() {}
	
	public Magic_square(Location location, String json) {
		this.location = location;
		Magic_square square = new Gson().fromJson(json, Magic_square.class);
		particle = square.particle;
		color = square.color;
		shapes = square.shapes;
		params = square.params;
		if(params.size() < 4) params.addAll(Arrays.asList((particle.hasColorParticle() ? 1.0 : 0.0), 0.0, 0.0, 0.0, 0.0));//もしサイズが0だったときのためのエラー回避で5つ入るようにする・・・そんなこと無いと思うがな
	}

	public void show() {
		for(ShapeData data : shapes) {
			//long start = System.nanoTime();
			Branch(data);
			//long end = System.nanoTime();
			//System.out.println(data.getType().toString()+"を描画するのに掛かる時間: "+((end-start) / 1000000f)+"ms");
			//ave += (end-start);
		}
	}
	
	/*public void average() {
		System.out.println("平均描画時間: "+((ave/100) / 1000000f)+"ms");
		System.out.println("-----------------------------------------");
	}*/
	
	private void Branch(ShapeData data) {
		EnumParticle particle = data.getParticle() != null ? data.getParticle() : this.particle;
		detailsColor = new DetailsColor(data.color != null ? data.color : color);
		double paramrepeat = data.getPosition() != null ? data.getPosition().getRepeat() != null ? data.getPosition().getRepeat().getAngle() : 2*Math.PI : 2*Math.PI;
		int limitcount = data.getPosition() != null ? data.getPosition().getRepeat() != null ? data.getPosition().getRepeat().getLimit()-1 : 0 : 0;
		double max = limitcount > 0 ? paramrepeat*limitcount < 2*Math.PI ? paramrepeat*limitcount : 2*Math.PI : 2*Math.PI;
		for(double theta = 0.0; theta <= max ; theta += paramrepeat) {
			Location center = data.getPosition() != null ? location.clone().add(data.getPosition().getPolar_Coordinates(location.getWorld()).add(0, theta, 0).convertLocation()) : this.location.clone();
			switch (data.getType()) {
			case CIRCLE: circle(data, center, particle);
				break;
			case POLYGON: polygon(data, center, particle);
				break;
			case STAR: star(data, center, particle);
				break;
			case LINE: line(data, center, particle);
				break;
			}
		}
	}
	
	//num 星型多角形
	private void star(ShapeData data, Location location, EnumParticle particle) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/(data.getQuantity()/2)) {
			Polar_coordinates pc = new Polar_coordinates(location.getWorld(), data.getRadius(), theta, 0);
			double max = pc.convertLocation().distance(pc.clone().add(0, Math.PI/(data.getQuantity()/2)*2, 0).convertLocation());
			for(double line = 0.0; line <= max; line += 0.2)
				sendParticle(particle, location.clone().add(pc.convertLocation()).add(pc.clone().add(line-data.getRadius(), ((2+(data.getQuantity()/2))*Math.PI/((data.getQuantity()/2)*2)), 0).convertLocation()));
		}
	}
	
	//num 多角形
	private void polygon(ShapeData data, Location location, EnumParticle particle) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/(data.getQuantity()/2)) {
			Polar_coordinates pc = new Polar_coordinates(location.getWorld(), data.getRadius(), theta, 0);
			double max = pc.convertLocation().distance(pc.clone().add(0, (Math.PI/(data.getQuantity()/2)), 0).convertLocation());
			for(double line = 0.0; line <= max; line += 0.2)
				sendParticle(particle, location.clone().add(pc.convertLocation()).add(pc.clone().add(line-data.getRadius(), Math.PI*((data.getQuantity()/2)+1)/(2*(data.getQuantity()/2)), 0).convertLocation()));
		}
	}
	
	//丸
	private void circle(ShapeData data, Location location, EnumParticle particle) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/60) {
			sendParticle(particle, location.clone().add(new Polar_coordinates(location.getWorld(), data.getRadius(), theta, 0).convertLocation()));
		}
	}
	
	//線
	private void line(ShapeData data, Location location, EnumParticle particle) {
		double angle = data.getPosition() == null ? 0 : data.getPosition().getAngle();
		for(double radius = 0; radius <= data.getRadius(); radius += 0.1) {
			sendParticle((data.getParticle() == null ? particle : data.getParticle()), location.clone().add(new Polar_coordinates(location.getWorld(), radius, angle, 0).convertLocation()));
		}
	}
	
	private void sendParticle(EnumParticle particle, Location center) {
		if(particle.hasColorParticle() && detailsColor != null) {
			sendParticle(particle, center, detailsColor.getRed(), detailsColor.getGreen(), detailsColor.getBlue(), 1, 0);
			return;
		}
		sendParticle(particle, center, params.get(0).floatValue(), params.get(1).floatValue(), params.get(2).floatValue(), params.get(3).floatValue(), (params.size() < 5 ? 0 : params.get(4).intValue()));
	}
	
	private void sendParticle(EnumParticle particle, Location center, float x, float y, float z, float speed, int amount) {
		Bukkit.getOnlinePlayers().stream().filter(player -> center.getWorld().getName().equals(player.getLocation().getWorld().getName()))
		.forEach(player -> {
			new ParticleAPI.Particle(particle, 
					center, 
					x, 
					y, 
					z, 
					speed, 
					amount).sendParticle(player);
		});
	}
}
