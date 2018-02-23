package jp.kotmw.core.nms.particle.magicsquare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;

import com.google.gson.annotations.SerializedName;

import jp.kotmw.core.Polar_coordinate;

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
	
	public Polar_coordinate getPolar_Coordinates(World world) {
		if(type != null) switch (type) {
			case COORDINATE:
				return new Polar_coordinate(new Location(world, pos.get(0), pos.get(1), pos.get(2)));
			case POLAR_COORDINATE:
				break;
			}
		return new Polar_coordinate(world, pos.get(0), Math.toRadians(pos.get(1)), Math.toRadians(pos.get(2)));
	}
	
	public Location getLocation(World world) {
		if(type != null) switch (type) {
		case COORDINATE:
			break;
		case POLAR_COORDINATE:
			return new Polar_coordinate(world, pos.get(0), Math.toRadians(pos.get(1)), Math.toRadians(pos.get(2))).convertLocation();
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
