package jp.kotmw.core.nms.particle.magicsquare;

public class Repetation {

	private double angle; //同じ図形の差の角度(180度以下)
	private int limit; //上限数
	private boolean rotation = false; //一緒に回すかどうか
	
	public double getAngle() {
		return Math.toRadians(angle);
	}
	
	public int getLimit() {
		return limit;
	}
	
	public boolean isRotation() {
		return rotation;
	}
}
