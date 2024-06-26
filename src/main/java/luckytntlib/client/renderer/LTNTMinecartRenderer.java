package luckytntlib.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;

import luckytntlib.entity.LTNTMinecart;
import luckytntlib.util.IExplosiveEntity;
import luckytntlib.util.tnteffects.PrimedTNTEffect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 
 * The LTNTMinecartRenderer renders a Minecart with a TNT inside of it.
 * The TNT is scaled using the size parameter of its {@link PrimedTNTEffect}.
 */
@OnlyIn(Dist.CLIENT)
public class LTNTMinecartRenderer extends MinecartRenderer<LTNTMinecart>{
	
	public LTNTMinecartRenderer(EntityRendererProvider.Context context) {
		super(context, ModelLayers.TNT_MINECART);
	}
	
	@Override
	public void renderMinecartContents(LTNTMinecart entity, float partialTicks, BlockState state, PoseStack stack, MultiBufferSource buffer, int i1) {
		int fuse = entity.getTNTFuse();
		if(fuse > -1 && (float) fuse - partialTicks + 1f < 10f) {
			float scaleMult = 1f - ((float)fuse - partialTicks + 1f) / 10f;
			scaleMult = Mth.clamp(scaleMult, 0f, 1f);
			scaleMult *= scaleMult;
			scaleMult *= scaleMult;
			float scale = 1f + scaleMult * 0.3f;
			stack.scale(scale, scale, scale);
		}
		stack.translate((-entity.getEffect().getSize(entity) + 1) / 2f, 0, (-entity.getEffect().getSize(entity) + 1) / 2f);
		stack.scale(entity.getEffect().getSize((IExplosiveEntity)entity), entity.getEffect().getSize((IExplosiveEntity)entity), entity.getEffect().getSize((IExplosiveEntity)entity));
		TntMinecartRenderer.renderWhiteSolidBlock(Minecraft.getInstance().getBlockRenderer(), state, stack, buffer, i1, fuse > -1 && fuse / 5 % 2 == 0);
	}
}
