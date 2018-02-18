package jp.kotmw.core.nms;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.chat.TranslatableComponent;

public class Translate extends NMSBase {
	
	@SuppressWarnings("deprecation")
	public static TranslatableComponent getComponent(Block block) {
		return getComponent(new ItemStack(block.getType(), 1, block.getData()));
	}
	
	public static TranslatableComponent getComponent(ItemStack itemStack) {
		try {
			Object nmsItemStack = getClass("org.bukkit.craftbukkit.%s.inventory.CraftItemStack").getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);
			return new TranslatableComponent(getNMSClass("ItemStack").getMethod("a").invoke(nmsItemStack).toString()+".name");
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
