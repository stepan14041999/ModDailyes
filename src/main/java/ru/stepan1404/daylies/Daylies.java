package ru.stepan1404.daylies;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import ru.stepan1404.daylies.messages.MessageGui;

@Mod(modid = "daylies")
public class Daylies {
	
	@Instance("daylies")
	public static Daylies instance;
	@SidedProxy(clientSide="ru.stepan1404.daylies.ClientProxy", serverSide="ru.stepan1404.daylies.ServerProxy")
	public static CommonProxy proxy;
	public static SimpleNetworkWrapper network;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		network = NetworkRegistry.INSTANCE.newSimpleChannel("daylies");
		network.registerMessage(MessageGui.MessageGuiHandler.class, MessageGui.class, 0, Side.CLIENT);
		proxy.doSomething(e);
	}
	
	public static void register(Object... objects) {
		for (Object o : objects) {
			MinecraftForge.EVENT_BUS.register(o);
			FMLCommonHandler.instance().bus().register(o);
		}
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent e){
		proxy.registerCommands(e);
	}

}
