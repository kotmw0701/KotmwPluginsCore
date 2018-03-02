package jp.kotmw.core;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

import org.bukkit.Location;

public class Polar_Coordinate2D implements Cloneable{
	
	private double radius; //r
	private double theta; //θ ※ラジアン
	
	public Polar_Coordinate2D(double radius, double theta) {
		this.radius = radius;
		this.theta = theta;
	}
	
	public Polar_Coordinate2D(Point2D point) {
		this.radius = point.distance(0, 0);
		this.theta = Math.atan2(point.getY(), point.getX());
	}
	
	public Polar_Coordinate2D(Location bukkitlocation) {
		this.radius = bukkitlocation.distance(new Location(bukkitlocation.getWorld(), 0, 0, 0));
		double x = bukkitlocation.getX(), z = bukkitlocation.getZ();
		this.theta = Math.acos(z/Math.sqrt(Math.pow(x, 2)+Math.pow(z, 2)));
	}
	
	public Point2D.Double convertLocation() {
		double x = radius*Math.cos(theta);
		double z = radius*Math.sin(theta);
		return new Point2D.Double(x, z);
	}
	
	public Point2D.Double rotation(double newtheta) {
		Double pos = convertLocation();
		return new Point2D.Double(pos.getX()*Math.cos(newtheta)-pos.getY()*Math.sin(newtheta), pos.getX()*Math.sin(newtheta)+pos.getY()*Math.cos(newtheta));
	}
	
	public Polar_Coordinate2D add(Polar_Coordinate2D pc) {
		this.radius += pc.radius;
		this.theta += pc.theta;
		return this;
	}
	
	public Polar_Coordinate2D add(double radius, double theta) {
		this.radius += radius;
		this.theta += theta;
		return this;
	}
	
	public double getRadius() {return radius;}
	
	public double getTheta() {return theta;}

	public void setRadius(double radius) {this.radius = radius;}

	public void setTheta(double theta) {this.theta = theta;}
	
	@Override
	public Polar_Coordinate2D clone() {
		try {
			return (Polar_Coordinate2D) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
}
