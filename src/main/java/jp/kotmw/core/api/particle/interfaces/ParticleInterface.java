package jp.kotmw.core.api.particle.interfaces;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import jp.kotmw.core.Polar_coordinate;

public interface ParticleInterface {
	
	/**
	 * 特定のプレイヤーにのみパーティクルを送信します<br>
	 * 別のワールドに居た場合は実行されません
	 * 
	 * @param player 対象のプレイヤー
	 * @return 別のワールドに居た場合はfalseが戻ってきます
	 */
	boolean sendParticle(Player player);
	
	boolean sendParticle(Player player, Location location);
	
	/**
	 * 集団のプレイヤーに送信します<br>
	 * Bukkit.getOnlinePlayers()そのままぶち込む前提の引数にしてあるけど普通のListでも入れられるはず
	 * 
	 * @param players 送信したいプレイヤー達のCollection
	 */
	void sendParticle(Collection<? extends Player> players);
	
	void sendParticle(Collection<? extends Player> players, Location location);
	
	/**
	 * 既存の座標データに新しいデータを足します
	 * 
	 * @param location 移動量
	 * @return 移動後の座標
	 */
	Location addLocation(Location location);
	
	/**
	 * 既存の座標データに新しいデータを足します
	 * 
	 * @param polar_coordinate 移動量
	 * @return 移動後の極座標
	 */
	Polar_coordinate addPolar_Coordinate(Polar_coordinate polar_coordinate);
	
	/**
	 * パーティクルに関するパラメータを設定します
	 * 
	 * @param offsetX x方向への拡散度
	 * @param offsetY y方向への拡散度
	 * @param offsetZ z方向への拡散度
	 * @param speed 速度
	 * @param amount 量
	 */
	void setParams(float offsetX, float offsetY, float offsetZ, float speed, int amount);
}
