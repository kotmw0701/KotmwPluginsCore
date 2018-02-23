package jp.kotmw.core.api.particle;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import jp.kotmw.core.Polar_coordinate;
import jp.kotmw.core.api.particle.interfaces.ParticleInterface;
import jp.kotmw.core.nms.DetailsColor;
import jp.kotmw.core.nms.NMSBase;

public class ParticleCore extends NMSBase implements ParticleInterface{

	private EnumParticle particle;
	private Location location;
	private float[] diffusion = new float[3];
	private float speed;
	private int amount;
	
	public ParticleCore(EnumParticle particle, DetailsColor detailsColor, Location location, float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		this.particle = particle;
		float[] color = particle.setColor(detailsColor).getColor();
		this.location = location;
		this.diffusion = color != null ? color : new float[]{offsetX, offsetY, offsetZ};
		this.speed = color != null ? 1f : speed;
		this.amount = color != null ? 0 : amount;
	}

	@Override
	public boolean sendParticle(Player player) {
		if(player.getLocation().getWorld().getName().equalsIgnoreCase(location.getWorld().getName())) player.spawnParticle(org.bukkit.Particle.valueOf(particle.name()), location, amount, diffusion[0], diffusion[1], diffusion[2], speed, particle.getData());
		else return false;
		return true;
	}

	@Override
	public void sendParticle(Collection<? extends Player> players) {
		players.stream().filter(player -> player.getLocation().getWorld().getName().equalsIgnoreCase(location.getWorld().getName())).forEach(player -> sendParticle(player));
	}
	
	@Override
	public void setParticle(EnumParticle particle) {
		this.particle = particle;
	}
	
	@Override
	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public void setPolar_Coordinates(Polar_coordinate polar_coordinates) {
		this.location = polar_coordinates.convertLocation();
	}
	
	@Override
	public Location addLocation(Location location) {
		return location.add(location);
	}
	
	@Override
	public Polar_coordinate addPolar_Coordinate(Polar_coordinate polar_coordinate) {
		return new Polar_coordinate(location).add(polar_coordinate);
	}
	
	@Override
	public void setParams(float offsetX, float offsetY, float offsetZ, float speed, int amount) {
		float[] color = this.particle.getColor();
		this.diffusion = color != null ? color : new float[]{offsetX, offsetY, offsetZ};
		this.speed = color != null ? 1f : speed;
		this.amount = color != null ? 0 : amount;
	}
}
