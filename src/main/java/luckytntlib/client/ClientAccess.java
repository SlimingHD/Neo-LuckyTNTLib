package luckytntlib.client;

import luckytntlib.client.gui.ConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class ClientAccess {

	private static final IConfigScreenFactory factory = new IConfigScreenFactory() {
		@Override
		public Screen createScreen(Minecraft mc, Screen screen) {
			return new ConfigScreen();
		}
	};

	public static IConfigScreenFactory getScreenFactory() {
		return factory;
	}
}
