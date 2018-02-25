package jp.kotmw.core.api.particle;

import org.bukkit.Location;

import jp.kotmw.core.nms.DetailsColor;

public abstract class ParticleRunnable extends ParticleCore implements Runnable {

	protected long delay;
	protected long period;
	protected long maximum;
	protected long count;
	
	private boolean running = true;
	private Thread thread;
	
	
	public ParticleRunnable(EnumParticle particle, Location location, float offsetX, 
			float offsetY, float offsetZ, float speed, int amount) {
		this(particle, null, location, offsetX, offsetY, offsetZ, speed, amount);
	}
	
	public ParticleRunnable(EnumParticle particle, DetailsColor detailsColor, Location location, float offsetX, 
			float offsetY, float offsetZ, float speed, int amount) {
		super(particle, detailsColor, location, offsetX, offsetY, offsetZ, speed, amount);
	}
	
	/**
	 * 表示処理をこの中に\ﾄﾞｰﾝ/
	 */
	public abstract void show();

	@Override
	public synchronized void run() {
		try {
			Thread.sleep(delay);
			while (running) {
				show();
				if(maximum > 0) {
					if(count >= maximum) {
						running = false;
						break;
					}
					count++;
				}
				Thread.sleep(period);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void cancel() {
		running = false;
	}

	/**
	 * 1回だけ表示
	 * 
	 * @param delay 表示までの遅延
	 * @return このパーティクルのインスタンス
	 * @throws 既にこのスレッドのインスタンスが生成され、動作してる時に投げられます
	 */
	public synchronized ParticleRunnable runMilliLater(long delay) throws IllegalStateException {
		return setup(delay, 0, 0);
	}

	/**
	 * ミリ秒(MilliSecond)間隔で表示
	 * 
	 * @param delay 表示までの遅延
	 * @param period 表示間隔
	 * @return このパーティクルのインスタンス
	 * @throws 既にこのスレッドのインスタンスが生成され、動作してる時に投げられます
	 */
	public synchronized ParticleRunnable runMilliTimer(long delay, long period) throws IllegalStateException {
		return setup(delay, period, 0);
	}

	/**
	 * ミリ秒(MilliSecond)間隔で表示
	 * 
	 * @param delay 表示までの遅延
	 * @param period 表示間隔
	 * @param maximum 最大回数
	 * @return このパーティクルのインスタンス
	 * @throws 既にこのスレッドのインスタンスが生成され、動作してる時に投げられます
	 */
	public synchronized ParticleRunnable runMilliTimer(long delay, long period, int maximum) throws IllegalStateException {
		return setup(delay, period, maximum);
	}

	/**
	 * 1回だけ表示
	 * 
	 * @param delay 表示までの遅延
	 * @return このパーティクルのインスタンス
	 * @throws 既にこのスレッドのインスタンスが生成され、動作してる時に投げられます
	 */
	public synchronized ParticleRunnable runSecondLater(long delay) throws IllegalStateException {
		return setup(delay*1000, 0, 0);
	}

	/**
	 * 秒(Second)間隔で表示
	 * 
	 * @param delay 表示までの遅延
	 * @param period 表示間隔
	 * @return このパーティクルのインスタンス
	 * @throws 既にこのスレッドのインスタンスが生成され、動作してる時に投げられます
	 */
	public synchronized ParticleRunnable runSecondTimer(long delay, long period) throws IllegalStateException {
		return setup(delay*1000, period*1000, 0);
	}

	/**
	 * 秒(Second)間隔で表示
	 * 
	 * @param delay 表示までの遅延
	 * @param period 表示間隔
	 * @param maximum 最大回数
	 * @return このパーティクルのインスタンス
	 * @throws 既にこのスレッドのインスタンスが生成され、動作してる時に投げられます
	 */
	public synchronized ParticleRunnable runSecondTimer(long delay, long period, int maximum) throws IllegalStateException {
		return setup(delay*1000, period*1000, maximum);
	}
	
	private ParticleRunnable setup(long delay, long period, long maximum) throws IllegalStateException {
		if(thread != null)
			throw new IllegalStateException("このRunnaleは既に動いて居ます");
		this.delay = delay;
		this.period = period;
		this.maximum = maximum;
		thread = new Thread(this);
		thread.start();
		return this;
	}
}
