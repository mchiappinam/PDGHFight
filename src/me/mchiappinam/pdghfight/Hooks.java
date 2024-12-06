package me.mchiappinam.pdghfight;

import net.milkbowl.vault.economy.Economy;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class Hooks {

	public static SimpleClans sc;
	public static Economy eco;
	
	public static void hookSimpleClans(){
	    if(Bukkit.getServer().getPluginManager().getPlugin("SimpleClans") != null){
			Bukkit.getConsoleSender().sendMessage("�2Hooked to SimpleClans");
		       sc = ((SimpleClans)Bukkit.getServer().getPluginManager().getPlugin("SimpleClans"));
		    }
	}
	
	public static void hookEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null){
			eco = (Economy)economyProvider.getProvider();
			Bukkit.getConsoleSender().sendMessage("�2Hooked to Vault (Economia)!");
		}
	}
}
