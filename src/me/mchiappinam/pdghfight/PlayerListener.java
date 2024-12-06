package me.mchiappinam.pdghfight;

import static me.mchiappinam.pdghfight.FightUtils.lutadores;
import static me.mchiappinam.pdghfight.FightUtils.participantes;
import static me.mchiappinam.pdghfight.FightUtils.venceuDuelo;
import static me.mchiappinam.pdghfight.PlayerUtils.isLutando;
import static me.mchiappinam.pdghfight.PlayerUtils.isParticipando;
import static me.mchiappinam.pdghfight.TeleportUtils.teleportSaida;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	@EventHandler
	public void onHungerChange(FoodLevelChangeEvent e) {
		for (Player p : participantes)
			if(isParticipando(p))
				e.setCancelled(true);
	}
	
	@EventHandler
	void onDeath(PlayerDeathEvent e) {
		if (isLutando(e.getEntity().getPlayer()) && e.getEntity().getPlayer() == lutadores.get(0)) {
			lutadores.get(1).setHealth(20);
			participantes.remove(e.getEntity().getPlayer());
			teleportSaida(e.getEntity().getPlayer());
			venceuDuelo(lutadores.get(1));
			e.getDrops().clear();
		} else if (isLutando(e.getEntity().getPlayer()) && e.getEntity().getPlayer() == lutadores.get(1)) {
			lutadores.get(0).setHealth(20);
			participantes.remove(e.getEntity().getPlayer());
			teleportSaida(e.getEntity().getPlayer());
			venceuDuelo(lutadores.get(0));
			e.getDrops().clear();
		}
		
		if (isParticipando(e.getEntity().getPlayer())) {
			participantes.remove(e.getEntity().getPlayer());
			teleportSaida(e.getEntity().getPlayer());
			e.getDrops().clear();
		}
		
	}
	
	@EventHandler
	void onQuit(PlayerQuitEvent e) {
		if (isLutando(e.getPlayer()) && e.getPlayer() == lutadores.get(0)) {
			lutadores.get(0).setHealth(0);
			participantes.remove(e.getPlayer());
			teleportSaida(e.getPlayer());
			venceuDuelo(lutadores.get(1));
		} else if (isLutando(e.getPlayer()) && e.getPlayer() == lutadores.get(1)) {
			lutadores.get(0).setHealth(1);
			participantes.remove(e.getPlayer());
			teleportSaida(e.getPlayer());
			venceuDuelo(lutadores.get(0));
		}
		
		if (isParticipando(e.getPlayer())) {
			e.getPlayer().setHealth(0);
			participantes.remove(e.getPlayer());
			teleportSaida(e.getPlayer());
		}
	}
	
	@EventHandler
	void onKick(PlayerKickEvent e) {
		if (isLutando(e.getPlayer()) && e.getPlayer() == lutadores.get(0)) {
			lutadores.get(0).setHealth(0);
			participantes.remove(e.getPlayer());
			teleportSaida(e.getPlayer());
			venceuDuelo(lutadores.get(1));
		} else if (isLutando(e.getPlayer()) && e.getPlayer() == lutadores.get(1)) {
			lutadores.get(0).setHealth(1);
			participantes.remove(e.getPlayer());
			teleportSaida(e.getPlayer());
			venceuDuelo(lutadores.get(0));
		}
		
		if (isParticipando(e.getPlayer())) {
			e.getPlayer().setHealth(0);
			participantes.remove(e.getPlayer());
			teleportSaida(e.getPlayer());
		}
	}
	
	@EventHandler
	void antiPvP(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			if ((isLutando((Player)e.getDamager()) || isLutando((Player)e.getEntity())) || (isLutando((Player)e.getDamager()) && isLutando((Player)e.getEntity()))) {
				return;
			}
			if (isParticipando((Player)e.getEntity()) || isParticipando((Player)e.getDamager())){
				e.setCancelled(true);
			}
		} else if (e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player && e.getEntity() instanceof Player){
			Projectile pj = (Projectile)e.getDamager();
			if (isParticipando((Player)pj.getShooter()) || isParticipando((Player)e.getEntity())){
				//e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled=true)
	private void onPCmd(PlayerCommandPreprocessEvent e) {
		if(isParticipando(e.getPlayer()))
		    if((e.getMessage().toLowerCase().startsWith("/g"))||(e.getMessage().toLowerCase().startsWith("/."))) {
		    	return;
		    }else{
		    	e.setCancelled(true);
		    	e.getPlayer().sendMessage("§5[Fight] §cApenas o comando do chat global (/g) e do chat de clans (/.) são liberados.");
		    }
	}
	
	
}
