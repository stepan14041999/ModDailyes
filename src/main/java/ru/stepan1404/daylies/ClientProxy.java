package ru.stepan1404.daylies;

import net.minecraft.client.Minecraft;
import ru.stepan1404.daylies.gui.GuiDailyRewards;

public class ClientProxy extends CommonProxy {

	public void openGui(int day, String text){
		Minecraft.getMinecraft().displayGuiScreen(new GuiDailyRewards(day, text));
	}
	
}