package jp.kotmw.core.nms.particle.magicsquare;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.annotations.SerializedName;

import jp.kotmw.core.Polar_Coordinate2D;
import jp.kotmw.core.Polar_Coordinate3D;

public class Position {
	
	private PositionType type = PositionType.POLAR_COORDINATE; //指定するタイプ
	private List<Double> pos = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0)); //始点
	private double angle;
	private Repetation repeat; //繰り返し
	
	public PositionType getType() {
		return type;
	}

	public List<Double> getPosition() {
		return pos;
	}
	
	public Repetation getRepeat() {
		return repeat;
	}
	
	public double getAngle() {
		return Math.toRadians(angle);
	}

	public void setType(PositionType type) {
		this.type = type;
	}

	public void setParams(List<Double> params) {
		this.pos = params;
	}
	
	public Polar_Coordinate3D getPolar_Coordinates(World world) {
		if(type != null) switch (type) {
			case COORDINATE:
				return new Polar_Coordinate3D(new Location(world, pos.get(0), pos.get(1), pos.get(2)));
			case POLAR_COORDINATE:
				break;
			}
		return new Polar_Coordinate3D(world, pos.get(0), Math.toRadians(pos.get(1)), Math.toRadians(pos.get(2)));
	}
	
	public Polar_Coordinate2D getPolar_Coordinates() {
		if(type != null) switch (type) {
			case COORDINATE:
				return new Polar_Coordinate2D(new Point2D.Double(pos.get(0), pos.get(2)));
			case POLAR_COORDINATE:
				break;
			}
		return new Polar_Coordinate2D(pos.get(0), pos.get(1));
	}
	
	public Location getLocation(World world) {
		if(type != null) switch (type) {
		case COORDINATE:
			break;
		case POLAR_COORDINATE:
			return new Polar_Coordinate3D(world, pos.get(0), Math.toRadians(pos.get(1)), Math.toRadians(pos.get(2))).convertLocation();
		}
		return new Location(world, pos.get(0), pos.get(1), pos.get(2));
	}

	public enum PositionType {
		@SerializedName("Coordinate")
		COORDINATE, 
		@SerializedName("Polar_Coordinate")
		POLAR_COORDINATE
	}
}
