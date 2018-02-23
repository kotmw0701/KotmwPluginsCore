package jp.kotmw.core.api.particle;

import org.bukkit.Location;

import jp.kotmw.core.api.particle.interfaces.ParticleRunnableInterface;
import jp.kotmw.core.nms.DetailsColor;

public abstract class ParticleRunnable extends ParticleCore implements ParticleRunnableInterface {

	private long delay;
	private long period;
	private long maximum;
	private long count;
	
	private boolean running = true;
	
	public ParticleRunnable(EnumParticle particle, Location location, float offsetX, 
			float offsetY, float offsetZ, float speed, int amount) {
		this(particle, null, location, offsetX, offsetY, offsetZ, speed, amount);
	}
	
	public ParticleRunnable(EnumParticle particle, DetailsColor detailsColor, Location location, float offsetX, 
			float offsetY, float offsetZ, float speed, int amount) {
		super(particle, detailsColor, location, offsetX, offsetY, offsetZ, speed, amount);
	}

	@Override
	public void run() {
		try {
			Thread.sleep(delay);
			while (running) {
				show();
				if(maximum > 0) {
					if(count >= maximum)
						running = false;
					count++;
				}
				Thread.sleep(period);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void cancel() {
		running = false;
	}

	@Override
	public ParticleRunnableInterface runMilliLater(long delay) {
		return setup(delay, 0, 0);
	}

	@Override
	public ParticleRunnableInterface runMilliTimer(long delay, long period) {
		return setup(delay, period, 0);
	}

	@Override
	public ParticleRunnableInterface runMilliTimer(long delay, long period, int maximum) {
		return setup(delay, period, maximum);
	}

	@Override
	public ParticleRunnableInterface runSecondLater(long delay) {
		return setup(delay*1000, 0, 0);
	}

	@Override
	public ParticleRunnableInterface runSecondTimer(long delay, long period) {
		return setup(delay*1000, period*1000, 0);
	}

	@Override
	public ParticleRunnableInterface runSecondTimer(long delay, long period, int maximum) {
		return setup(delay*1000, period*1000, maximum);
	}
	
	private ParticleRunnableInterface setup(long delay, long period, long maximum) {
		this.delay = delay;
		this.period = period;
		this.maximum = maximum;
		Thread thread = new Thread(this);
		thread.start();
		return this;
	}
}
