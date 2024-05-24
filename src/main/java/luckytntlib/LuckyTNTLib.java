package luckytntlib;

import com.mojang.datafixers.util.Pair;

import luckytntlib.block.LTNTBlock;
import luckytntlib.client.gui.ConfigScreen;
import luckytntlib.config.LuckyTNTLibConfigs;
import luckytntlib.entity.LTNTMinecart;
import luckytntlib.item.LDynamiteItem;
import luckytntlib.item.LTNTMinecartItem;
import luckytntlib.registry.RegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(LuckyTNTLib.MODID)
public class LuckyTNTLib
{
    public static final String MODID = "luckytntlib";
    public static final DeferredRegister<EntityType<?>> test = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
    public LuckyTNTLib(IEventBus bus, ModContainer container) {
        bus.addListener(this::commonSetup);
        LuckyTNTLibConfigs.registerCommonConfig(container);
        container.registerExtensionPoint(IConfigScreenFactory.class, new IConfigScreenFactory(){		
			@Override
			public Screen createScreen(Minecraft mc, Screen screen) {
				return new ConfigScreen();
			}
		});
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
    	for(Pair<DeferredBlock<LTNTBlock>, DeferredItem<Item>> pair : RegistryHelper.TNT_DISPENSER_REGISTRY_LIST) {
    		LTNTBlock block = pair.getFirst().get();
    		Item item = pair.getSecond().get();
			DispenseItemBehavior behaviour = new DispenseItemBehavior() {
				@Override
				public ItemStack dispense(BlockSource source, ItemStack stack) {
					Level level = source.level();
					Position p = DispenserBlock.getDispensePosition(source);
					BlockPos pos = new BlockPos(Mth.floor(p.x()), Mth.floor(p.y()), Mth.floor(p.z()));
					block.explode(level, false, pos.getX(), pos.getY(), pos.getZ(), null);
					stack.shrink(1);
					return stack;
				}
			};
			DispenserBlock.registerBehavior(item, behaviour);
    	}
    	for(DeferredItem<LDynamiteItem> dynamite : RegistryHelper.DYNAMITE_DISPENSER_REGISTRY_LIST) {
    		LDynamiteItem item = dynamite.get();
    		DispenseItemBehavior behaviour = new DispenseItemBehavior() {
				
				@Override
				public ItemStack dispense(BlockSource source, ItemStack stack) {
					Level level = source.level();
					Vec3 dispenserPos = new Vec3(source.pos().getX() + 0.5f, source.pos().getY() + 0.5f, source.pos().getZ() + 0.5f);
					Position pos = DispenserBlock.getDispensePosition(source);
					item.shoot(level, pos.x(), pos.y(), pos.z(), new Vec3(pos.x(), pos.y(), pos.z()).add(-dispenserPos.x(), -dispenserPos.y(), -dispenserPos.z()), 2, null);
					stack.shrink(1);
					return stack;
				}
			};
			DispenserBlock.registerBehavior(item, behaviour);
    	}
    	for(DeferredItem<LTNTMinecartItem> minecart : RegistryHelper.MINECART_DISPENSER_REGISTRY_LIST) {
    		LTNTMinecartItem item = minecart.get();
			DispenseItemBehavior behaviour = new DispenseItemBehavior() {
				@Override
				public ItemStack dispense(BlockSource source, ItemStack stack) {
					Direction direction = source.state().getValue(DispenserBlock.FACING);
					Level level = source.level();
					double x = source.center().x() + (double) direction.getStepX() * 1.125D;
					double y = Math.floor(source.center().y()) + (double) direction.getStepY();
					double z = source.center().z() + (double) direction.getStepZ() * 1.125D;
					BlockPos pos = source.pos().relative(direction);
					BlockState state = level.getBlockState(pos);
					RailShape rail = state.getBlock() instanceof BaseRailBlock ? ((BaseRailBlock) state.getBlock()).getRailDirection(state, level, pos, null) : RailShape.NORTH_SOUTH;
					double railHeight;
					if (state.is(BlockTags.RAILS)) {
						if (rail.isAscending()) {
							railHeight = 0.6D;
						} else {
							railHeight = 0.1D;
						}
					} else {
						if (!state.isAir() || !level.getBlockState(pos.below()).is(BlockTags.RAILS)) {
							return new DefaultDispenseItemBehavior().dispense(source, stack);
						}

						BlockState stateDown = level.getBlockState(pos.below());
						@SuppressWarnings("deprecation")
						RailShape railDown = stateDown.getBlock() instanceof BaseRailBlock ? stateDown.getValue(((BaseRailBlock) stateDown.getBlock()).getShapeProperty()) : RailShape.NORTH_SOUTH;
						if (direction != Direction.DOWN && railDown.isAscending()) {
							railHeight = -0.4D;
						} else {
							railHeight = -0.9D;
						}
					}

					LTNTMinecart cart = item.createMinecart(level, x, y + railHeight, z, null);
					if (stack.has(DataComponents.CUSTOM_NAME) && !stack.get(DataComponents.CUSTOM_NAME).getString().equals("")) {
						cart.setCustomName(stack.getHoverName());
					}
					stack.shrink(1);
					return stack;
				}
			};
			DispenserBlock.registerBehavior(item, behaviour);
    	}
    }
}
