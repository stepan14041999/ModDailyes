package ru.stepan1404.daylies.events;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import ru.stepan1404.daylies.Daylies;
import ru.stepan1404.daylies.messages.MessageGui;
import ru.stepan1404.daylies.utils.RewardBase;

public class LoginEvent {

	private File dir;
	private boolean isItem = false;
	private boolean isBlock = false;
	private List<String> data;
	private String materialName = "minecraft:diamond";
	private int amount = 1;
	private int damage = 0;

	public LoginEvent(FMLPreInitializationEvent e) {
		this.dir = e.getModConfigurationDirectory();
	}

	@SubscribeEvent
	public synchronized void onPlayerLogin(PlayerLoggedInEvent e) {
		if (!e.player.worldObj.isRemote) {
			try {
				File file = new File(dir + "/daylies/Daylies_Log.log");
				if (file.exists()) {
					// Read file
					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);
					String line;
					int day;
					boolean nameFound = false;
					// Write to buffer
					StringBuffer ib = new StringBuffer();
					while ((line = br.readLine()) != null) {
						ib.append(line);
						ib.append('\n');
					}
					fr.close();
					br.close();
					// Get current date
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(new Date());

					String dM = "" + calendar.get(Calendar.DAY_OF_YEAR);

					List<String> lines = new ArrayList<String>();
					lines = Arrays.asList(ib.toString().split("\n"));
					lines = new ArrayList<String>(lines);
					// Search string with name or create this
					for (int i = 0; i < lines.size(); i++) {
						String pString = lines.get(i);
						if (pString.contains(e.player.getDisplayName())) {
							String lastDate = pString.substring(e.player.getDisplayName().length() + 3,
									pString.length());
							if (!lastDate.equals(dM)) {
								day = Character.getNumericValue(pString.charAt(e.player.getDisplayName().length() + 1));
								if (day > 0 && day < 7) {
									day++;
								} else {
									day = 1;
								}
								lines.set(i, e.player.getDisplayName() + ":" + day + ":" + dM);
								Daylies.network.sendTo(new MessageGui(day, RewardBase.rewards.toString().substring(1, RewardBase.rewards.toString().length()-1)),
										(EntityPlayerMP) e.player);
								data = Arrays.asList(RewardBase.rewards.get(day-1).split(":"));
								amount = Integer.valueOf(data.get(0));
								materialName = data.get(1) + ":" + data.get(2);
								if (data.size() == 4)
									damage = Integer.valueOf(data.get(3));
								checkName(materialName);
								if (isItem)
									if (!e.player.inventory.addItemStackToInventory(
											new ItemStack(getBlock(materialName), amount, damage))) {
										e.player.worldObj.spawnEntityInWorld(new EntityItem(e.player.worldObj,
												e.player.posX, e.player.posY, e.player.posZ,
												new ItemStack(getItem(materialName), amount, damage)));
									}
								if (isBlock)
									if (!e.player.inventory.addItemStackToInventory(
											new ItemStack(getBlock(materialName), amount, damage))) {
										e.player.worldObj.spawnEntityInWorld(new EntityItem(e.player.worldObj,
												e.player.posX, e.player.posY, e.player.posZ,
												new ItemStack(getBlock(materialName), amount, damage)));
									}
							}
							nameFound = true;
						}
					}

					if (!nameFound) {
						lines.add(e.player.getDisplayName() + ":1:" + dM);
						Daylies.network.sendTo(new MessageGui(1, RewardBase.rewards.toString().substring(1, RewardBase.rewards.toString().length()-1)), (EntityPlayerMP) e.player);
						data = Arrays.asList(RewardBase.rewards.get(0).split(":"));
						amount = Integer.valueOf(data.get(0));
						materialName = data.get(1) + ":" + data.get(2);
						if (data.size() == 4)
							damage = Integer.valueOf(data.get(3));
						checkName(materialName);
						if (isItem)
							e.player.worldObj.spawnEntityInWorld(new EntityItem(e.player.worldObj,
									e.player.posX, e.player.posY, e.player.posZ,
									new ItemStack(getItem(materialName), amount, damage)));
						if (isBlock)
							e.player.worldObj.spawnEntityInWorld(new EntityItem(e.player.worldObj,
									e.player.posX, e.player.posY, e.player.posZ,
									new ItemStack(getBlock(materialName), amount, damage)));
					}
					// Write to file
					FileOutputStream fileOut = new FileOutputStream(file);
					fileOut.write(StringUtils.join(lines, '\n').getBytes());
					fileOut.close();
				}
			} catch (IOException exc) {
				exc.printStackTrace();
			}
		}
	}

	public void checkName(String name) {
		Block block = (Block) Block.blockRegistry.getObject(name);
		if (block == null || block == Blocks.air) {
			Item item = (Item) Item.itemRegistry.getObject(name);
			if (item == null)
				throw new NullPointerException("Could not find any blocks named " + name);
			else
				isItem = true;
		} else
			isBlock = true;
	}

	public Block getBlock(String name) {
		return (Block) Block.blockRegistry.getObject(name);
	}

	public Item getItem(String name) {
		return (Item) Item.itemRegistry.getObject(name);
	}
}
