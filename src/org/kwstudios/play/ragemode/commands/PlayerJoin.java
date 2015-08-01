package org.kwstudios.play.ragemode.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.kwstudios.play.ragemode.gameLogic.PlayerList;
import org.kwstudios.play.ragemode.toolbox.ConfigFactory;
import org.kwstudios.play.ragemode.toolbox.MapChecker;

public class PlayerJoin {
	
	private static final String GAME_PATH = "settings.games";
	
	private Player player;
	@SuppressWarnings("unused")
	private String label;
	private String[] args;
	private FileConfiguration fileConfiguration;
	
	public PlayerJoin(Player player, String label, String[] args, FileConfiguration fileConfiguration){
		this.player = player;
		this.label = label;
		this.args = args;
		this.fileConfiguration = fileConfiguration;
		doPlayerJoin();
	}
	
	private void doPlayerJoin(){
		MapChecker mapChecker = new MapChecker(args[1], fileConfiguration);
		if(mapChecker.isValid()){
			String world = ConfigFactory.getString(GAME_PATH + "." + args[1] + ".lobby", "world", fileConfiguration);			
			int lobbyX = ConfigFactory.getInt(GAME_PATH + "." + args[1] + ".lobby", "x", fileConfiguration);
			int lobbyY = ConfigFactory.getInt(GAME_PATH + "." + args[1] + ".lobby", "y", fileConfiguration);
			int lobbyZ = ConfigFactory.getInt(GAME_PATH + "." + args[1] + ".lobby", "y", fileConfiguration);
			Location lobbyLocation = new Location(Bukkit.getWorld(world), lobbyX, lobbyY, lobbyZ);
			
			Location playerLocation = player.getLocation();
			
			Logger logger = Logger.getLogger("Minecraft");
			
			if(PlayerList.addPlayer(player, args[1], fileConfiguration)){
				PlayerList.oldLocations.addToBoth(player, playerLocation);
				player.teleport(lobbyLocation);
				logger.info(ChatColor.DARK_AQUA + "[RageMode] " + ChatColor.DARK_GREEN + player.getName() + " joined the RageMode game " + args[1] + ".");
			}else{
				logger.info(ChatColor.DARK_AQUA + "[RageMode] " + ChatColor.DARK_RED + player.getName() + " could not join the RageMode game " + args[1] + ".");
			}
			
		}else{
			player.sendMessage(mapChecker.getMessage());
		}
	}

}