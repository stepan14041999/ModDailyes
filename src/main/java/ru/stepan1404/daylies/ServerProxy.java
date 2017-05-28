package ru.stepan1404.daylies;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import ru.stepan1404.daylies.commands.ReloadCommand;
import ru.stepan1404.daylies.events.LoginEvent;
import ru.stepan1404.daylies.utils.RewardBase;

public class ServerProxy extends CommonProxy {
	
	public void doSomething(FMLPreInitializationEvent e){
		File dayDir = new File(e.getModConfigurationDirectory() + "/daylies/");
		File dayLog = new File(e.getModConfigurationDirectory() + "/daylies/Daylies_Log.log");
		File rewards = new File(e.getModConfigurationDirectory() + "/daylies/rewards.cfg");
		if(!dayDir.exists()){
			dayDir.mkdirs();
		}
		if(!dayLog.exists()){
			try {
				dayLog.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(!rewards.exists()){
			try {
				rewards.createNewFile();
				FileOutputStream fileOut = new FileOutputStream(rewards);
				fileOut.write(("1:minecraft:stone\n1:minecraft:stone\n1:minecraft:stone\n1:minecraft:stone\n"
						+ "1:minecraft:wool:12\n1:minecraft:wool:12\n1:minecraft:wool:12").getBytes());
				fileOut.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		Daylies.register(new LoginEvent(e));
		RewardBase.loadRewards(e);
	}
	
	public void registerCommands(FMLServerStartingEvent e){
		e.registerServerCommand(new ReloadCommand());
	}
	
	

}
