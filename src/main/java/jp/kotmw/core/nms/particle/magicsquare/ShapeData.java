package jp.kotmw.core.nms.particle.magicsquare;

import com.google.gson.annotations.SerializedName;

import jp.kotmw.core.api.particle.EnumParticle;

public class ShapeData extends Magic_square {
	
	//全図形共通変数
	private SquareType type = SquareType.STAR; //図形のパターン
	private double radius; //はんけー
	private Position position; //始点をどこにするか
	
	//多角形と星型多角形のみ
	private double quantity; //頂点の数
	
	public SquareType getType() {return type;}

	public double getQuantity() {return quantity;}

	public double getRadius() {return radius;}

	public EnumParticle getParticle() {return particle;}
	
	public String getColor() {return color;}

	public Position getPosition() {return position;}
	
	public enum SquareType {
		@SerializedName("star")
		STAR, 
		@SerializedName("polygon")
		POLYGON,
		@SerializedName("circle")
		CIRCLE,
		@SerializedName("line")
		LINE;
	}
}
