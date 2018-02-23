package jp.kotmw.core.api.particle;

import org.bukkit.Location;

import jp.kotmw.core.nms.DetailsColor;

public class Particle extends ParticleCore{
	
	/**
	 * 新しいParticleのインスタンスを生成します。
	 * @param particle パーティクルの種類
	 * @param location 発生位置
	 * @param offsetX x方向への拡散度
	 * @param offsetY y方向への拡散度
	 * @param offsetZ z方向への拡散度
	 * @param speed 速度
	 */
	public Particle(EnumParticle particle,Location location, float offsetX, float offsetY, float offsetZ,
			float speed) {
		this(particle, null, location, offsetX, offsetY, offsetZ, speed, 1);
	}
	
	/**
	 * 新しいParticleのインスタンスを生成します。
	 * @param particle パーティクルの種類
	 * @param detailsCoolor パーティクルに色がある場合の色
	 * @param location 発生位置
	 * @param offsetX x方向への拡散度
	 * @param offsetY y方向への拡散度
	 * @param offsetZ z方向への拡散度
	 * @param speed 速度
	 */
	public Particle(EnumParticle particle, DetailsColor detailsColor, Location location, float offsetX, float offsetY, float offsetZ,
			float speed) {
		this(particle, detailsColor, location, offsetX, offsetY, offsetZ, speed, 1);
	}
	
	/**
	 * 新しいParticleのインスタンスを生成します。
	 * @param particle パーティクルの種類
	 * @param location 発生位置
	 * @param offsetX x方向への拡散度
	 * @param offsetY y方向への拡散度
	 * @param offsetZ z方向への拡散度
	 * @param speed 速度
	 * @param amount 量
	 */
	public Particle(EnumParticle particle, Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int amount) {
		super(particle, null, location, offsetX, offsetY, offsetZ, speed, amount);
	}
	
	/**
	 * 新しいParticleのインスタンスを生成します。
	 * @param particle パーティクルの種類
	 * @param detailsCoolor パーティクルに色がある場合の色
	 * @param location 発生位置
	 * @param offsetX x方向への拡散度
	 * @param offsetY y方向への拡散度
	 * @param offsetZ z方向への拡散度
	 * @param speed 速度
	 * @param amount 量
	 */
	public Particle(EnumParticle particle, DetailsColor detailsColor, Location location, float offsetX, float offsetY, float offsetZ,
			float speed, int amount) {
		super(particle, detailsColor, location, offsetX, offsetY, offsetZ, speed, amount);
	}
}
