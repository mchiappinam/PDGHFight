package me.mchiappinam.pdghfight;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.mchiappinam.pdghapiutility.Metodos;

public class Main extends JavaPlugin {
	
	private static Main instance;
	public static boolean apiutility=false;
	private static me.mchiappinam.pdghapiutility.Main api;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		Main.instance = this;
		getServer().getConsoleSender().sendMessage("§3[PDGHFight] §2ativando...");
		
		if (getServer().getPluginManager().getPlugin("PDGHAPIUtility") == null) {
			getLogger().warning("PDGHAPIUtility nao encontrado!");
			apiutility=false;
		}else{
			getLogger().info("PDGHAPIUtility ativado!");
			api = (me.mchiappinam.pdghapiutility.Main)getServer().getPluginManager().getPlugin("PDGHAPIUtility");
			apiutility=true;
		}

		Hooks.hookEconomy();
		Hooks.hookSimpleClans();
		Files.getInstance().setupFiles(this);
		getServer().getConsoleSender().sendMessage("§3[PDGHFight] §2ativado - Plugin inspirado no LsFight. Versão by mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHFight] §2Acesse: http://pdgh.com.br/");
		
		getCommand("fight").setExecutor(new Comandos());
		getCommand("fight").setTabCompleter(new FightTabCompleter());
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		if (Bukkit.getPluginManager().getPlugin("Legendchat") != null) {
			Bukkit.getPluginManager().registerEvents(new Chat(), this);
		}
		
		if (getConfig().getBoolean("AutoStart.Ativar")) {
			AutoStart.checkTempo();
		}
		
	}
	
	@Override
	public void onDisable() {
		getServer().getConsoleSender().sendMessage("§3[PDGHFight] §2desativado - Plugin inspirado no LsFight. Versão by mchiappinam");
		getServer().getConsoleSender().sendMessage("§3[PDGHFight] §2Acesse: http://pdgh.com.br/");
	}
	
    public static Metodos getMetodos() {
    	return api.getMetodos();
    }
	
	public static Main getInstance() {
		return Main.instance;
	}
	
}
