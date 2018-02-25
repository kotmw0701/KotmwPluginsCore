package jp.kotmw.core.api.particle;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.google.gson.annotations.SerializedName;

import jp.kotmw.core.nms.DetailsColor;

public enum EnumParticle {
	@SerializedName("explode")
	EXPLOSION_NORMAL("explode", 0),
	@SerializedName("largeexplode")
	EXPLOSION_LARGE("largeexplode", 1),
	@SerializedName("hugeexplosion")
	EXPLOSION_HUGE("hugeexplosion", 2),
	@SerializedName("fireworksSpark")
	FIREWORKS_SPARK("fireworksSpark", 3),
	@SerializedName("bubble")
	WATER_BUBBLE("bubble", 4),
	@SerializedName("splash")
	WATER_SPLASH("splash", 5),
	@SerializedName("wake")
	WATER_WAKE("wake", 6),
	@SerializedName("suspended")
	SUSPENDED("suspended", 7),
	@SerializedName("depthsuspend")
	SUSPENDED_DEPTH("depthsuspend", 8),
	@SerializedName("crit")
	CRIT("crit", 9),
	@SerializedName("magicCrit")
	CRIT_MAGIC("magicCrit", 10),
	@SerializedName("smoke")
	SMOKE_NORMAL("smoke", 11),
	@SerializedName("largesmoke")
	SMOKE_LARGE("largesmoke", 12),
	@SerializedName("spell")
	SPELL("spell", 13),
	@SerializedName("instantSpell")
	SPELL_INSTANT("instantSpell", 14),
	@SerializedName("mobSpell")
	SPELL_MOB("mobSpell", 15, DataType.Color),
	@SerializedName("mobSpellAmbient")
	SPELL_MOB_AMBIENT("mobSpellAmbient", 16, DataType.Color),
	@SerializedName("witchMagic")
	SPELL_WITCH("witchMagic", 17),
	@SerializedName("dripWater")
	DRIP_WATER("dripWater", 18),
	@SerializedName("dripLava")
	DRIP_LAVA("dripLava", 19),
	@SerializedName("angryVillager")
	VILLAGER_ANGRY("angryVillager", 20),
	@SerializedName("happyVillager")
	VILLAGER_HAPPY("happyVillager", 21),
	@SerializedName("townaura")
	TOWN_AURA("townaura", 22),
	@SerializedName("note")
	NOTE("note", 23, DataType.Note),
	@SerializedName("portal")
	PORTAL("portal", 24),
	@SerializedName("enchantmenttable")
	ENCHANTMENT_TABLE("enchantmenttable", 25),
	@SerializedName("flame")
	FLAME("flame", 26),
	@SerializedName("lava")
	LAVA("lava", 27),
	@SerializedName("footstep")
	FOOTSTEP("footstep", 28),
	@SerializedName("cloud")
	CLOUD("cloud", 29),
	@SerializedName("reddust")
	REDSTONE("reddust", 30, DataType.Color),
	@SerializedName("snowballpoof")
	SNOWBALL("snowballpoof", 31),
	@SerializedName("snowshovel")
	SNOW_SHOVEL("snowshovel", 32),
	@SerializedName("slime")
	SLIME("slime", 33),
	@SerializedName("heart")
	HEART("heart", 34),
	@SerializedName("barrier")
	BARRIER("barrier", 35),
	@SerializedName("iconcrack")
	ITEM_CRACK("iconcrack", 36, DataType.ItemStack),
	@SerializedName("blockcrack")
	BLOCK_CRACK("blockcrack", 37, DataType.Block),
	@SerializedName("blockdust")
	BLOCK_DUST("blockdust", 38, DataType.Block),
	@SerializedName("droplet")
	WATER_DROP("droplet", 39),
	@SerializedName("take")
	ITEM_TAKE("take", 40),
	@SerializedName("mobappearance")
	MOB_APPEARANCE("mobappearance",41),
	@SerializedName("dragonbreath")
	DRAGON_BREATH("dragonbreath", 42),
	@SerializedName("endRod")
	END_ROD("endRod", 43),
	@SerializedName("damageIndicator")
	DAMAGE_INDICATOR("damageIndicator", 44),
	@SerializedName("sweepAttack")
	SWEEP_ATTACK("sweepAttack", 45),
	@SerializedName("fallingdust")
	FALLING_DUST("fallingdust", 46, DataType.Block),
	;

	private String name;
	private int id;
	private DataType type;
	private int[] numData = null;
	private ItemStack itemData = new ItemStack(Material.IRON_SPADE);
	private MaterialData materialData = new MaterialData(Material.STONE);
	private DetailsColor color = null;
	private int pitch = -1;

	private static Map<String, EnumParticle> X;
	private static Map<Integer, EnumParticle> Y;

	static {
		X = new HashMap<String, EnumParticle>();
		Y = new HashMap<Integer, EnumParticle>();
		for(EnumParticle a : values()){
			X.put(a.name, a);
			Y.put(a.id, a);
		}
	}
	private EnumParticle(String name, int id) {
		this(name, id, DataType.None);
	}
	private EnumParticle(String name, int id, DataType type) {
		this.name = name;
		this.id = id;
		this.type = type;
		switch(this.type) {
		case ItemStack:
			numData = new int[]{256, 0};
			break;
		case Block:
			numData = new int[]{1, 0};
			break;
		default: break;
		}
	}

	/**
	 * パーティクルの名前を返します。<br>
	 * ここでの名前はparticleコマンドで指定する名前です。
	 * @return パーティクルの名前を返します
	 */
	public String getName() {
		return name;
	}

	/**
	 * パーティクルの数値IDを返します。
	 * @return パーティクルの数値IDを返します
	 */
	public int getID() {
		return id;
	}

	/**
	 * このパーティクルが、1.9で追加された新しいパーティクルであるかを返します
	 * @return このパーティクルが1.9で追加されたものならtrue、そうでなければfalseを返します
	 */
	public boolean is1_9NewParticle() {
		switch (this) {
		case DRAGON_BREATH:
		case END_ROD:
		case DAMAGE_INDICATOR:
		case SWEEP_ATTACK: return true;
		default: return false;
		}
	}

	/**
	 * このパーティクルが、1.10で追加された新しいパーティクルであるかを返します
	 * @return このパーティクルが1.10で追加されたものならtrue、そうでなければfalseを返します
	 */
	public boolean is1_10NewParticle() {
		return this == EnumParticle.FALLING_DUST;
	}

	/**
	 * このパーティクルが、データ値を持つパーティクルであるかを返します。
	 * @return このパーティクルがデータ値を持てばtrue、そうでなければfalseを返します
	 */
	public boolean hasDataParticle() {
		switch (this.type) {
		case ItemStack:
		case Block: return true;
		default: return false;
		}
	}
	
	public boolean hasColorParticle() {
		switch(this.type) {
		case Color: return true;
		default: return false;
		}
	}

	/**
	 * このパーティクルのデータ値に、引数のItemStackの情報を割り当てます。
	 * @param item 適用させたい情報を持つItemStack
	 * @return データ値変更後のEnumParticle
	 */
	@SuppressWarnings("deprecation")
	public EnumParticle setItemData(ItemStack item) {
		if(item == null) throw new NullPointerException("`item` is null.");
		if(type == DataType.ItemStack) {
			itemData = item;
			numData[0] = item.getTypeId();
			numData[1] = (int)item.getDurability();
		}
		return this;
	}

	/**
	 * このパーティクルのデータ値に、引数のBlockの情報を割り当てます。
	 * @param block 適用させたい情報を持つBlock
	 * @return データ値変更後のEnumParticle
	 */
	@SuppressWarnings("deprecation")
	public EnumParticle setBlockData(Block block) {
		if(block == null) throw new NullPointerException("`block` is null.");
		switch (this.type) {
		case ItemStack:
			return setItemData(new ItemStack(block.getType(), 1, (short)block.getData()));
		case Block:
			materialData = block.getState().getData();
			numData[0] = block.getTypeId();
			numData[1] = (int)block.getData();
		default: return this;
		}
	}

	/**
	 * このパーティクルのデータ値に、引数のMaterialの情報を割り当てます。
	 * @param material 適用させたい情報を持つMaterial
	 * @return データ値変更後のEnumParticle
	 */
	@SuppressWarnings("deprecation")
	public EnumParticle setMaterialData(Material material) {
		if(material == null) throw new NullPointerException("`material` is null.");
		switch (this.type) {
		case ItemStack:
			return setItemData(new ItemStack(material));
		case Block:
			materialData = new MaterialData(material);
			numData[0] = material.getId();
			numData[1] = 0;
		default: return this;
		}
	}

	/**
	 * このパーティクルのデータ値を設定します。
	 * @param id 設定したいアイテムID
	 * @return データ値変更後のEnumParticle
	 */
	public EnumParticle setNumberData(int id) {
		return setNumberData(id, 0);
	}

	/**
	 * このパーティクルのデータ値を設定します。
	 * @param id 設定したいアイテムID
	 * @param data 設定したいダメージ値
	 * @return データ値変更後のEnumParticle
	 */
	@SuppressWarnings({ "deprecation", "unused" })
	public EnumParticle setNumberData(int id, int data) {
		switch (this.type) {
		case ItemStack:
			return setItemData(new ItemStack(id, 1, (short)data));
		case Block:
			MaterialData md = new MaterialData(id, (byte)data);
			if(md == null) throw new NullPointerException("`MaterialData` is null.");
			materialData = md;
			numData[0] = id;
			numData[1] = data;
		default: return this;
		}
	}

	/**
	 * 1.9以降用のデータ
	 * @return ItemStackやMaterialData
	 */
	public Object getData() {
		switch (this.type) {
		case ItemStack: return itemData;
		case Block: return materialData;
		default: return null;
		}
	}

	/**
	 * 一部のパーティクルの色を指定します。
	 * <p>色の指定が出来るのは{@link EnumParticle.SPELL_MOB}("mobSpell")や{@link EnumParticle.REDSTONE}("reddust")といった一部のパーティクルだけです。<br>
	 * 色を指定すると、拡散度と速度、発生するパーティクルが1回に固定されます。</p>
	 * @param color 指定する色
	 * @return EnumParticle
	 */
	public EnumParticle setColor(DetailsColor color) {
		if(this.type == DataType.Color) this.color = color;
		return this;
	}
	/**
	 * {@link EnumParticle.NOTE}のパーティクルの色を指定します。
	 * <p>{@link EnumParticle.NOTE}("note")専用です。<br>
	 * 色を指定すると、拡散度と速度、発生するパーティクルが1回に固定されます。<br>
	 * 値は0以上24以下で指定されます。</p>
	 * @param pitch 指定する色
	 * @return EnumParticle
	 */
	public EnumParticle setPitch(int pitch) {
		if(this.type == DataType.Note && pitch >= 0 && pitch <= 24) this.pitch = pitch;
		return this;
	}
	/**
	 * 色の設定を適用させるためのもの
	 * @return 設定がないならnull
	 */
	public float[] getColor() {
		switch (this.type) {
		case Color:
			if(color == null) return null;
			return new float[]{color.getRed(), color.getGreen(), color.getBlue()};
		case Note:
			if(pitch < 0) return null;
			return new float[]{pitch/24f, 0f, 0f};
		default: return null;
		}
	}

	/**
	 * 文字列から該当するパーティクルの種類を返します
	 * @param name パーティクルの名前
	 * @return 見つからない場合はnullが返されます
	 */
	public static EnumParticle getParticle(String name) {
		String[] divi = name.split("_");
		int len = divi.length;
		if(len == 2 || len == 3) {
			EnumParticle par = getParticle(divi[0]);
			if(par != null && par.hasDataParticle()) {
				int id = 1;
				int data = 0;
				try {
					if(len == 2) {
						int num = Integer.parseInt(divi[1]);
						if(num >= 4096) {
							id = num / 4096;
							data = num % 4096;
						} else {
							id = num;
						}
					} else if(len == 3) {
						id = Integer.parseInt(divi[1]);
						data = Integer.parseInt(divi[2]);
					}
				} catch(ArrayIndexOutOfBoundsException | NumberFormatException e) {}
				par.setNumberData(id, data);
			}
			return par;
		}
		return X.containsKey(name) ? X.get(name) : null;
	}

	/**
	 * 数値IDから該当するパーティクルの種類を返します
	 * @param id パーティクルの数値ID
	 * @return 見つからない場合はnullが返されます
	 */
	public static EnumParticle getParticle(int id) {
		return Y.containsKey(id) ? Y.get(id) : null;
	}
	
	private  enum DataType {
		ItemStack,
		Block,
		Color,
		Note,
		None
	}
}