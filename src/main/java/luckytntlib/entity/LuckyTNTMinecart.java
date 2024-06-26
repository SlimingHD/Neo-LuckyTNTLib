package luckytntlib.entity;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import luckytntlib.block.LTNTBlock;
import luckytntlib.item.LTNTMinecartItem;
import luckytntlib.util.tnteffects.PrimedTNTEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * 
 * The LuckyTNTMinecart is an extension of the {@link LTNTMinecart}
 * and turns into a random {@link LTNTMinecart} of a list when fused.
 */
public class LuckyTNTMinecart extends LTNTMinecart{

	private List<DeferredItem<LTNTMinecartItem>> minecarts;
	
	public LuckyTNTMinecart(EntityType<LTNTMinecart> type, Level level, DeferredBlock<LTNTBlock> defaultRender, Supplier<DeferredItem<LTNTMinecartItem>> pickItem, List<DeferredItem<LTNTMinecartItem>> minecarts) {
		super(type, level, null, pickItem, false);
		effect = new PrimedTNTEffect() {
			@Override
			public Block getBlock() {
				return defaultRender.get();
			}
		};
		this.minecarts = minecarts;
		setTNTFuse(-1);
	}
	
	@Override
	public void fuse() {
		LTNTMinecart minecart = minecarts.get(new Random().nextInt(minecarts.size())).get().createMinecart(level(), getX(), getY(), getZ(), placer);
		minecart.setYRot(getYRot());
		minecart.setDeltaMovement(getDeltaMovement());
		level().addFreshEntity(minecart);
		minecart.fuse();
		discard();
	}
}
