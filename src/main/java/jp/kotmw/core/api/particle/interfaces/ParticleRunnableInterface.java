package jp.kotmw.core.api.particle.interfaces;

public interface ParticleRunnableInterface extends ParticleInterface, Runnable {
	
	/**
	 * 表示処理をこの中に\ﾄﾞｰﾝ/
	 */
	void show();
	
	/**
	 * 1回だけ表示
	 * 
	 * @param delay 表示までの遅延
	 * @return このパーティクルのインスタンス
	 */
	ParticleRunnableInterface runMilliLater(long delay);
	
	/**
	 * ミリ秒(MilliSecond)間隔で表示
	 * 
	 * @param delay 表示までの遅延
	 * @param period 表示間隔
	 * @return このパーティクルのインスタンス
	 */
	ParticleRunnableInterface runMilliTimer(long delay, long period);
	
	/**
	 * ミリ秒(MilliSecond)間隔で表示
	 * 
	 * @param delay 表示までの遅延
	 * @param period 表示間隔
	 * @param maximum 最大回数
	 * @return このパーティクルのインスタンス
	 */
	ParticleRunnableInterface runMilliTimer(long delay, long period, int maximum);
	
	/**
	 * 1回だけ表示
	 * 
	 * @param delay 表示までの遅延
	 * @return このパーティクルのインスタンス
	 */
	ParticleRunnableInterface runSecondLater(long delay);
	
	/**
	 * 秒(Second)間隔で表示
	 * 
	 * @param delay 表示までの遅延
	 * @param period 表示間隔
	 * @return このパーティクルのインスタンス
	 */
	ParticleRunnableInterface runSecondTimer(long delay, long period);
	
	/**
	 * 秒(Second)間隔で表示
	 * 
	 * @param delay 表示までの遅延
	 * @param period 表示間隔
	 * @param maximum 最大回数
	 * @return このパーティクルのインスタンス
	 */
	ParticleRunnableInterface runSecondTimer(long delay, long period, int maximum);
	
	/**
	 * 終了処理
	 */
	void cancel();
}
