package me.mchiappinam.pdghfight;

import static me.mchiappinam.pdghfight.FightUtils.finalizar;
import static me.mchiappinam.pdghfight.FightUtils.iniciando;
import static me.mchiappinam.pdghfight.FightUtils.isAberto;
import static me.mchiappinam.pdghfight.FightUtils.isAcontecendo;
import static me.mchiappinam.pdghfight.FightUtils.participantes;
import static me.mchiappinam.pdghfight.FightUtils.totalParticipantes;
import static me.mchiappinam.pdghfight.LocationUtils.setEntrada;
import static me.mchiappinam.pdghfight.LocationUtils.setPos1;
import static me.mchiappinam.pdghfight.LocationUtils.setPos2;
import static me.mchiappinam.pdghfight.LocationUtils.setSaida;
import static me.mchiappinam.pdghfight.MensagensUtils.formatMessage;
import static me.mchiappinam.pdghfight.MensagensUtils.getFinalizadoPorAdmMensagens;
import static me.mchiappinam.pdghfight.MensagensUtils.getMensagem;
import static me.mchiappinam.pdghfight.MensagensUtils.getPrefix;
import static me.mchiappinam.pdghfight.MensagensUtils.getStatusTemplate;
import static me.mchiappinam.pdghfight.PlayerUtils.isAuthorized;
import static me.mchiappinam.pdghfight.PlayerUtils.isPlayer;
import static me.mchiappinam.pdghfight.TeleportUtils.teleportEntrada;
import static me.mchiappinam.pdghfight.TeleportUtils.teleportSaida;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Comandos implements CommandExecutor {
	
	static Files files = Files.getInstance();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String lsCommand, String[] args) {
		
		if (args.length == 0) {
			if (args.length == 0) {
				for (String s : files.getMensagensFile().getStringList("USAGE")) {
					if (!sender.hasPermission("lsfight.admin") && (s.contains("set") || s.contains("Iniciar") || s.contains("Finalizar"))) {
						return true;
					}
					sender.sendMessage(MensagensUtils.formatMessage(s));
				}
				return true;
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("setentrada")) {
			if (isAuthorized(sender)) {
				if (isPlayer(sender)) {
					setEntrada((Player)sender);
					sender.sendMessage(getPrefix() + " §cEntrada definida com sucesso.");
				}
			}
		}
		
		if (args[0].equalsIgnoreCase("setsaida")) {
			if (isAuthorized(sender)) {
				if (isPlayer(sender)) {
					setSaida((Player)sender);
					sender.sendMessage(getPrefix() + " §cSaida definida com sucesso.");
				}
			}
		}
		
		if (args[0].equalsIgnoreCase("setpos1")) {
			if (isAuthorized(sender)) {
				if (isPlayer(sender)) {
					setPos1((Player)sender);
					sender.sendMessage(getPrefix() + " §cPosição 1 definida com sucesso.");
				}
			}
		}
		
		if (args[0].equalsIgnoreCase("setpos2")) {
			if (isAuthorized(sender)) {
				if (isPlayer(sender)) {
					setPos2((Player)sender);
					sender.sendMessage(getPrefix() + " §cPosição 2 definida com sucesso.");
				}
			}
		}
		
		if (args[0].equalsIgnoreCase("finiciar")) {
			if (isAuthorized(sender)) {
				if (files.getDataFile().getString("Entrada.") == null || files.getDataFile().getString("Saida.") == null || files.getDataFile().getString("Pos1.") == null || files.getDataFile().getString("Pos2.") == null) {
					sender.sendMessage(getPrefix() + " §cVocê precisa definir a entrada, saida, posição 1 e posição 2 antes de iniciar o evento.");
					return true;
				}
				if (isAcontecendo()) {
					sender.sendMessage(getPrefix() + " §c O evento já está acontecendo.");
					return true;
				}
		    	if(Main.apiutility) {
		    		Main.getMetodos().sendTweet("EVENTO FIGHT INICIANDO...");
		    	}
				iniciando(Main.getInstance().getConfig().getInt("Iniciando.NumeroDeAvisos"));
			}
		}
		
		if (args[0].equalsIgnoreCase("ffinalizar")) {
			if (isAuthorized(sender)) {
				if (isAcontecendo()) {
					for (Player p : participantes) {
						teleportSaida(p);
					}
					
					finalizar();
					
					for (String msg : getFinalizadoPorAdmMensagens()) {
						Bukkit.broadcastMessage(formatMessage(msg));
					}
				} else {
					sender.sendMessage(getMensagem("NAO_ACONTECENDO"));
				}
			}
		}
		
		/*            COMANDOS MEMBROS                */
		
		if (args[0].equalsIgnoreCase("participar")) {
			if (isPlayer(sender)) {
				Player p = (Player)sender;
				if (!isAcontecendo()) {
					p.sendMessage(getMensagem("NAO_ACONTECENDO"));
					return true;
				} else if (!isAberto()) {
					p.sendMessage(getMensagem("NAO_ABERTO"));
					return true;
				} else if (participantes.contains(sender)) {
					p.sendMessage(getMensagem("JA_PARTICIPANDO"));
					return true;
				}
				if (!PlayerUtils.isInventoryEmpty(p)) {
					p.sendMessage(getMensagem("ESVAZIE_INV"));
					return true;
				}
				if((p.getHealth()<=0)||p.getGameMode().equals(GameMode.SPECTATOR)) {
					p.sendMessage(getMensagem("MORTO"));
					return true;
				}
				teleportEntrada((Player)sender);
				participantes.add((Player)sender);
				totalParticipantes.put(((Player)sender).getName(), 1);
			}
		}
		
		if (args[0].equalsIgnoreCase("status")) {
			for (String msg : getStatusTemplate()) {
				sender.sendMessage(formatMessage(msg));
			}
		}
	
		return false;
	}

}
