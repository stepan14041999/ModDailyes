package ru.stepan1404.daylies.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import ru.stepan1404.daylies.utils.RewardBase;

public class ReloadCommand extends CommandBase{

	@Override
	public String getCommandName() {
		return "dailies";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/dailies reload";
	}

	@Override
	public void processCommand(ICommandSender p_71515_1_, String[] lines) {
		if(lines.length > 0){
			if(lines.length == 1 && lines[0].equals("reload")){
				RewardBase.reloadRewards();
				p_71515_1_.addChatMessage(new ChatComponentText("Dailies reloaded!"));
			}
			if(lines.length == 2 && lines[0].equals("nextday")){
				RewardBase rb = new RewardBase();
				rb.nextDay(MinecraftServer.getServer().getConfigurationManager().func_152612_a(lines[1]));
			}
			if(lines.length > 2){
				throw new WrongUsageException("Must be write a task!", new Object[0]);
			}
		}
		else
        {
            throw new WrongUsageException("Must be write a task!", new Object[0]);
        }
	}

}
