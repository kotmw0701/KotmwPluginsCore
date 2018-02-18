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
import jp.kotmw.core.nms.particle.magicsquare.Position.PositionType;

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
	
	protected EnumParticle particle;
	protected String color;
	protected List<Double> params = new ArrayList<>();
	protected List<ShapeData> shapes = new ArrayList<>();
	
	protected Magic_square() {}
	
	public Magic_square(Location location, String json) {
		this.location = location;
		Magic_square square = new Gson().fromJson(json, Magic_square.class);
		particle = square.particle;
		color = square.color;
		shapes = square.shapes;
		params = square.params;
	}

	public void show() {
		for(ShapeData data : shapes)
			Branch(data);
	}
	
	private void Branch(ShapeData data) {
		Location location = data.getPosition() == null 
				? this.location.clone() : this.location.clone().add(data.getPosition().getType() == PositionType.POLAR_COORDINATE 
						? data.getPosition().getPolar_Coordinates(this.location.getWorld()).convertLocation() : data.getPosition().getLocation(this.location.getWorld()));
		EnumParticle particle = (data.getParticle() == null ? this.particle : data.getParticle());
		String colorcode = (data.color == null ? color : data.color);
		if(params.size() < 4) params.addAll(Arrays.asList((double)(particle.hasColorParticle() ? 1 : 0), (double)0));
		detailsColor = colorcode != null ? new DetailsColor(colorcode) : new DetailsColor(params.get(0).intValue(), params.get(1).intValue(), params.get(2).intValue());
		switch (data.getType()) {
		case CIRCLE: circle(data, location, particle);
			break;
		case POLYGON: polygon(data, location, particle);
			break;
		case STAR: star(data, location, particle);
			break;
		case LINE: line(data, location, particle);
			break;
		}
	}
	
	//num 星型多角形
	private void star(ShapeData data, Location location, EnumParticle particle) {
		for(double theta = 0.0; theta <= 2*Math.PI; theta += Math.PI/(data.getQuantity()/2)) {
			Polar_coordinates pc = new Polar_coordinates(location.getWorld(), data.getRadius(), theta, 0);
			double max = pc.convertLocation().distance(pc.clone().add(0, 2*Math.PI/(data.getQuantity()/2), 0).convertLocation());
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
		for(double theta = 0.0; theta <= Math.PI*2; theta += Math.PI/60) {
			sendParticle(particle, location.clone().add(new Polar_coordinates(location.getWorld(), data.getRadius(), theta, 0).convertLocation()));
		}
	}
	
	//線
	private void line(ShapeData data, Location location, EnumParticle particle) {
		for(double radius = 0; radius <= data.getRadius(); radius += 0.1) {
			sendParticle((data.getParticle() == null ? particle : data.getParticle()), location.clone().add(new Polar_coordinates(location.getWorld(), radius, Math.toRadians(data.getPosition().getAngle()), 0).convertLocation()));
		}
	}
	
	private void sendParticle(EnumParticle particle, Location center) {
		if(particle.hasColorParticle()) {
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
