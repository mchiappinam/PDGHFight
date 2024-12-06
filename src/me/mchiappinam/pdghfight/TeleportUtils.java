package me.mchiappinam.pdghfight;

import static me.mchiappinam.pdghfight.FightUtils.totalParticipantes;
import static me.mchiappinam.pdghfight.MensagensUtils.getMensagem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class TeleportUtils {
	protected static List<String> vips = new ArrayList<String>();
	
	static FileConfiguration data = Files.getInstance().getDataFile();
	
	@SuppressWarnings("deprecation")
	public static void teleportEntrada(Player p) {
		
		if (data.getString("Entrada.") == null) {
			p.sendMessage(MensagensUtils.getPrefix() + " §cErro: Entrada do evento ainda não foi definida.");
			return;
		}


		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.getInventory().clear();
		p.setFoodLevel(20);
		World w = Bukkit.getWorld(data.getString("Entrada.World"));
		double x = data.getDouble("Entrada.X");
		double y = data.getDouble("Entrada.Y");
		double z = data.getDouble("Entrada.Z");
		float yaw = (float) data.getDouble("Entrada.Yaw");
		float pitch = (float) data.getDouble("Entrada.Pitch");
		
		Location entrada = new Location(w, x, y, z);
		entrada.setPitch(pitch);
		entrada.setYaw(yaw);
		
		p.teleport(entrada);
		PlayerUtils.ativarClanFF(p);
		
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.getInventory().clear();
		p.updateInventory();
		for(PotionEffect effect : p.getActivePotionEffects()) {
		    p.removePotionEffect(effect.getType());
		    p.sendMessage("§ePoção §6"+effect.getType().getName()+" §eremovida.");
		   }
		if(p.hasPermission("pdgh.vip"))
			if(!vips.contains(p.getName().toLowerCase())) {
					Bukkit.broadcastMessage(getMensagem("ENTROU_NO_EVENTO_VIP").replace("<player>", p.getName()));
					vips.add(p.getName().toLowerCase());
				}
		p.sendMessage(getMensagem("ENTROU_NO_EVENTO"));
		FightUtils.giveItem(p);
	}
	
	public static void teleportSaida(Player p) {
		
		if (data.getString("Saida.") == null) {
			p.sendMessage(MensagensUtils.getPrefix() + " §cErro: Saida do evento ainda não foi definida.");
			return;
		}

		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.closeInventory();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.getInventory().clear();
		p.updateInventory();
		World w = Bukkit.getWorld(data.getString("Saida.World"));
		double x = data.getDouble("Saida.X");
		double y = data.getDouble("Saida.Y");
		double z = data.getDouble("Saida.Z");
		float yaw = (float) data.getDouble("Saida.Yaw");
		float pitch = (float) data.getDouble("Saida.Pitch");
		
		Location saida = new Location(w, x, y, z);
		saida.setPitch(pitch);
		saida.setYaw(yaw);
		
		p.teleport(saida);
		totalParticipantes.remove(p.getName());
		PlayerUtils.desativarClanFF(p);
	}
	
	public static void teleportLutadores(Player p1, Player p2) {
		
		if (data.getString("Pos1.") == null || data.getString("Pos2.") == null) {
			Bukkit.broadcastMessage(MensagensUtils.getPrefix() + " §cErro: Posição 1 ou 2 do evento ainda não foi definida.");
			return;
		}
		p1.closeInventory();
		p1.closeInventory();
		p1.closeInventory();
		p1.closeInventory();
		p1.closeInventory();
		p1.getInventory().setHelmet(null);
		p1.getInventory().setChestplate(null);
		p1.getInventory().setLeggings(null);
		p1.getInventory().setBoots(null);
		p1.getInventory().clear();
		p1.updateInventory();
		for(PotionEffect effect : p1.getActivePotionEffects()) {
		    p1.removePotionEffect(effect.getType());
		    p1.sendMessage("§ePoção §6"+effect.getType().getName()+" §eremovida.");
		}

		p2.closeInventory();
		p2.closeInventory();
		p2.closeInventory();
		p2.closeInventory();
		p2.closeInventory();
		p2.getInventory().setHelmet(null);
		p2.getInventory().setChestplate(null);
		p2.getInventory().setLeggings(null);
		p2.getInventory().setBoots(null);
		p2.getInventory().clear();
		p2.updateInventory();
		for(PotionEffect effect : p2.getActivePotionEffects()) {
		    p2.removePotionEffect(effect.getType());
		    p2.sendMessage("§ePoção §6"+effect.getType().getName()+" §eremovida.");
		}
		
		World w1 = Bukkit.getWorld(data.getString("Pos1.World"));
		double x1 = data.getDouble("Pos1.X");
		double y1 = data.getDouble("Pos1.Y");
		double z1 = data.getDouble("Pos1.Z");
		float yaw1 = (float) data.getDouble("Pos1.Yaw");
		float pitch1 = (float) data.getDouble("Pos1.Pitch");
		
		Location pos1 = new Location(w1, x1, y1, z1);
		pos1.setPitch(pitch1);
		pos1.setYaw(yaw1);
		
		p1.setHealth(20);
		p1.teleport(pos1);
		
		World w2 = Bukkit.getWorld(data.getString("Pos2.World"));
		double x2 = data.getDouble("Pos2.X");
		double y2 = data.getDouble("Pos2.Y");
		double z2 = data.getDouble("Pos2.Z");
		float yaw2 = (float) data.getDouble("Pos2.Yaw");
		float pitch2 = (float) data.getDouble("Pos2.Pitch");
		
		Location pos2 = new Location(w2, x2, y2, z2);
		pos2.setPitch(pitch2);
		pos2.setYaw(yaw2);
		
		p2.setHealth(20);
		p2.teleport(pos2);
	}

}
