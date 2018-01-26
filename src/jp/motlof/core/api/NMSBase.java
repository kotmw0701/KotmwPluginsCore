/**
 * @author kotmw0701
 *
 */
package jp.motlof.core.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NMSBase {

	private static String ver;
	
	static {
		ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}
	
	protected static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object pConnection = handle.getClass().getField("playerConnection").get(handle);
			pConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(pConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected static Class<?> getNMSClass(String name) {
		try {
			return Class.forName("net.minecraft.server."+ver+"."+name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected enum Ver{
		v1_9, //byte
		v1_10, //byte
		v1_11, //byte
		v1_12 //ChatMessageType
	}
	
	protected static boolean isCMT() {
		if(ver.contains(Ver.v1_9.toString()))
			return false;
		else if (ver.contains(Ver.v1_10.toString()))
			return false;
		else if (ver.contains(Ver.v1_11.toString()))
			return false;
		else if (ver.contains(Ver.v1_12.toString()))
			return true;
		return true;
	}
}
