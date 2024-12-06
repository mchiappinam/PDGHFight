package me.mchiappinam.pdghfight;

import static me.mchiappinam.pdghfight.FightUtils.iniciando;
import static me.mchiappinam.pdghfight.FightUtils.isAcontecendo;
import static me.mchiappinam.pdghfight.MensagensUtils.getPrefix;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class AutoStart {
	
	//private static boolean in = false;
	private static FileConfiguration data = Files.getInstance().getDataFile();
	
	private static int getHora(String hora) {
		/*
		 Edit: 2016, Isso nao eh necessario.
		 
		 switch (hora) {
			case "00": hora = "0"; break;
			case "01": hora = "1"; break;
			case "02": hora = "2"; break;
			case "03": hora = "3"; break;
			case "04": hora = "4"; break;
			case "05": hora = "5"; break;
			case "06": hora = "6"; break;
			case "07": hora = "7"; break;
			case "08": hora = "8"; break;
			case "09": hora = "9"; break;
			default: break;
		}*/
		return Integer.parseInt(hora);
	}
	
	private static int getMinuto(String minuto) {
		/*switch (minuto) {
			case "00": minuto = "0"; break;
			case "01": minuto = "1"; break;
			case "02": minuto = "2"; break;
			case "03": minuto = "3"; break;
			case "04": minuto = "4"; break;
			case "05": minuto = "5"; break;
			case "06": minuto = "6"; break;
			case "07": minuto = "7"; break;
			case "08": minuto = "8"; break;
			case "09": minuto = "9"; break;
			default: break;
		}*/
		return Integer.parseInt(minuto);
	}
	
	private static int getDia(String dia) {
		
		switch (dia.toLowerCase()) {
			case "segunda":
				return 2;
				
			case "terca":
			case "ter�a":
				return 3;
				
			case "quarta":
				return 4;
				
			case "quinta":
				return 5;
				
			case "sexta":
				return 6;
				
			case "sabado":
				return 7;
				
			case "domingo":
				return 1;	
				
			default:
				return 0;
		}
	}
	
	public static void checkTempo() {
		
		try {
			for (String s : Main.getInstance().getConfig().getStringList("AutoStart.Horarios")) {
				
				String[] a = s.split("-");
				String dia1;
				String[] horario1 = null;
				
				if (a.length == 2) {
					dia1 = s.split("-")[0];	
					horario1 = s.split("-")[1].split(":");
				} else {
					dia1 = "";
					horario1 = s.split("-")[0].split(":");
				}
				
				final String dia = dia1;
				final String[] horario = horario1;
				
				Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
					
					@Override
					public void run() {
						
						if (Calendar.getInstance().get(7) == getDia(dia) && Calendar.getInstance().get(11) == getHora(horario[0]) && Calendar.getInstance().get(12) == getMinuto(horario[1])) {
							if (data.getString("Entrada.") == null || data.getString("Saida.") == null || data.getString("Pos1.") == null || data.getString("Pos2.") == null) {
								Bukkit.getLogger().severe(getPrefix() + " �cVoc� precisa definir a entrada, saida, posi��o 1 e posi��o 2 antes de iniciar o evento.");
								return;
							}
							if (isAcontecendo()) {
								Bukkit.getLogger().severe(getPrefix() + " �c O evento j� est� acontecendo.");
								return;
							}
							iniciando(Main.getInstance().getConfig().getInt("Iniciando.NumeroDeAvisos"));
						} else if (Calendar.getInstance().get(11) == getHora(horario[0]) && Calendar.getInstance().get(12) == getMinuto(horario[1])) {
							if (data.getString("Entrada.") == null || data.getString("Saida.") == null || data.getString("Pos1.") == null || data.getString("Pos2.") == null) {
								Bukkit.getLogger().severe(getPrefix() + " �cVoc� precisa definir a entrada, saida, posi��o 1 e posi��o 2 antes de iniciar o evento.");
								return;
							}
							if (isAcontecendo()) {
								Bukkit.getLogger().severe(getPrefix() + " �c O evento j� est� acontecendo.");
								return;
							}
					    	if(Main.apiutility) {
					    		Main.getMetodos().sendTweet("EVENTO FIGHT INICIANDO...");
					    	}
							iniciando(Main.getInstance().getConfig().getInt("Iniciando.NumeroDeAvisos"));
						}
					}
				}, 0, 200);
			}
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("�4[MVP] �cErro na configuracao dos horarios.");	
		}
	}
}
