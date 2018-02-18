package jp.kotmw.core.nms.particle.magicsquare;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.annotations.SerializedName;

import jp.kotmw.core.nms.Polar_coordinates;

public class Position {
	
	private PositionType type; //指定するタイプ
	private List<Double> pos; //始点
	
	//line限定
	private double angle;
	
	public PositionType getType() {
		return type;
	}

	public List<Double> getPosition() {
		return pos;
	}
	
	public double getAngle() {
		return angle;
	}

	public void setType(PositionType type) {
		this.type = type;
	}

	public void setParams(List<Double> params) {
		this.pos = params;
	}
	
	public Polar_coordinates getPolar_Coordinates(World world) {
		return new Polar_coordinates(world, pos.get(0), Math.toRadians(pos.get(1)), Math.toRadians(pos.get(2)));
	}
	
	public Location getLocation(World world) {
		return new Location(world, pos.get(0), pos.get(1), pos.get(2));
	}

	enum PositionType {
		@SerializedName("Coordinate")
		COORDINATE, 
		@SerializedName("Polar_Coordinate")
		POLAR_COORDINATE
	}
}
