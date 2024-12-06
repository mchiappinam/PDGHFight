package me.mchiappinam.pdghfight;

import static me.mchiappinam.pdghfight.MensagensUtils.*;
import static me.mchiappinam.pdghfight.TeleportUtils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FightUtils {
	
	private static boolean acontecendo = false;
	private static boolean aberto = false;
	private static int rodada = 1;
	public static ArrayList<Player> participantes = new ArrayList<>();
	public static ArrayList<Player> lutadores = new ArrayList<>();
	public static HashMap<String,Integer> totalParticipantes = new HashMap<String,Integer>();
	//private Main plugin;
	public FightUtils(Main main) {
		//plugin=main;
	}
	
	static Files files = Files.getInstance();
	
	public static boolean isAcontecendo() {
		return acontecendo;
	}
	
	public static boolean isAberto() {
		return aberto;
	}
	
	public static void setAcontecendo(boolean b) {
		acontecendo = b;
	}
	
	public static void setAberto(boolean b) {
		aberto = b;
	}

	public static void finalizar() {
		setAberto(false);
		setAcontecendo(false);
		participantes.clear();
		totalParticipantes.clear();
		vips.clear();
		rodada=1;
		lutadores.clear();
		Bukkit.getScheduler().cancelTasks(Main.getInstance());
	}
	
	public static void iniciando(final int numeroAvisos) {
		if (numeroAvisos != 0) {
			setAcontecendo(true);
			setAberto(true);
			for(String msg : getIniciandoMensagens()) {
				Bukkit.broadcastMessage(formatMessage(msg).replace("<restante>", Integer.toString(Main.getInstance().getConfig().getInt("Iniciando.TempoEntreAvisos") * numeroAvisos)));
			}
			Bukkit.getServer().getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
				
				public void run() {
					iniciando(numeroAvisos - 1);
				}
				
			}, Main.getInstance().getConfig().getInt("Iniciando.TempoEntreAvisos") * 20);
		} else {
			iniciar();
		}
	}
	
	public static void iniciar() {
		if (participantes.size() >= Main.getInstance().getConfig().getInt("MinimoParticipantes")) {
			for (String msg : getIniciadoMensagens()) {
				Bukkit.broadcastMessage(formatMessage(msg));
			}
			iniciarLuta();
			setAberto(false);
			
		} else {
			for (String msg : getMinPlayerMensagens()) {
				Bukkit.broadcastMessage(formatMessage(msg.replace("<min>", Integer.toString(Main.getInstance().getConfig().getInt("MinimoParticipantes")))));
			}
			for (Player p : participantes) {
				teleportSaida(p);
			}
			finalizar();
		}
	}
	
	public static void iniciarLuta() {
		for (String msg : getProximaLutaMensagens()) {
			Bukkit.broadcastMessage(formatMessage(msg.replace("<tempo>", Integer.toString(Main.getInstance().getConfig().getInt("TempoEntreLutas")))));
		}
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				int quantia=0;
				for(String n : totalParticipantes.keySet()) {
					//Bukkit.broadcastMessage(n+" rodada "+totalParticipantes.get(n));
					int rodadaParticipante = totalParticipantes.get(n);
					if(rodadaParticipante==rodada)
						quantia++;
				}
				if(quantia<2) {
					//Bukkit.broadcastMessage("1");
					rodada++;
					//Bukkit.broadcastMessage("2");
					if (participantes.size() == 3) {
						Bukkit.broadcastMessage("§5[Fight] §6»» §c§lPenúltima rodada!");
					}else if (participantes.size() == 2) {
						Bukkit.broadcastMessage("§5[Fight] §6»» §c§lÚltima rodada!");
					}else{
						Bukkit.broadcastMessage("§5[Fight] §6»» §c"+rodada+"ª rodada!");
					}
					//Bukkit.broadcastMessage("3");

					try {
					if(participantes.size()>2)
						for(String n : totalParticipantes.keySet()) {
							int rodadaParticipante = totalParticipantes.get(n);
							if(rodadaParticipante!=rodada) {
								//Bukkit.broadcastMessage("4");
								totalParticipantes.remove(n);
								//Bukkit.broadcastMessage("5");
								totalParticipantes.put(n, rodada);
								//Bukkit.broadcastMessage("6");
							}
						}
					} 
					catch (Exception e)
					{
					  //System.out.println("Got an Exception: " + e.getMessage());
					}
				}
				//for(String n : totalParticipantes.keySet())
				//	Bukkit.broadcastMessage(n+" rodadaafterkeySet "+totalParticipantes.get(n));
				//Bukkit.broadcastMessage("Rodada: "+rodada);
				//Bukkit.broadcastMessage("Quantia: "+quantia);
				//Bukkit.broadcastMessage("Participantes: "+participantes.size());
				
				if(participantes.size()>2) {
					Random r = new Random();
					Player lutador1 = participantes.get(r.nextInt(participantes.size()));
					Player lutador2 = participantes.get(r.nextInt(participantes.size()));
	
					//Bukkit.broadcastMessage("1: "+lutador1.getName());
					//Bukkit.broadcastMessage("2: "+lutador2.getName());
					while (totalParticipantes.get(lutador1.getName()) != rodada) {
						lutador1 = participantes.get(r.nextInt(participantes.size()));
						//Bukkit.broadcastMessage(".1: "+lutador1.getName());
					}
						
					while ((totalParticipantes.get(lutador2.getName()) != rodada)||(lutador2==lutador1)) {
						lutador2 = participantes.get(r.nextInt(participantes.size()));
						//Bukkit.broadcastMessage(".2: "+lutador2.getName());
					}
					lutadores.add(lutador1);
					lutadores.add(lutador2);
					
					for (String msg : getLutaMensagens()) {
						Bukkit.broadcastMessage(formatMessage(msg.replace("<lutador1>", lutador1.getName()).replace("<lutador2>", lutador2.getName())));
					}
				
					teleportLutadores(lutador2, lutador1);
					giveItem(lutador1);
					giveItem(lutador2);
				}else{
					Player lutador1 = participantes.get(0);
					Player lutador2 = participantes.get(1);
					lutadores.add(lutador1);
					lutadores.add(lutador2);
					
					for (String msg : getLutaMensagens()) {
						Bukkit.broadcastMessage(formatMessage(msg.replace("<lutador1>", lutador1.getName()).replace("<lutador2>", lutador2.getName())));
					}
				
					teleportLutadores(lutador2, lutador1);
					giveItem(lutador1);
					giveItem(lutador2);
				}
				
			}
		}.runTaskLater(Main.getInstance(), Main.getInstance().getConfig().getInt("TempoEntreLutas") * 20);
		
	}
	
	public static void venceuDuelo(Player vencedor) {
		int r = totalParticipantes.get(vencedor.getName());
		totalParticipantes.remove(vencedor.getName());
		totalParticipantes.put(vencedor.getName(), r+1);
		if (lutadores.get(1) == vencedor) {
			for (String msg  : getMorreuMensagens()) {
				Bukkit.broadcastMessage(formatMessage(msg).replace("<vencedor>", vencedor.getName()).replace("<perdedor>", lutadores.get(0).getName()));
			}
		} else {
			for (String msg  : getMorreuMensagens()) {
				Bukkit.broadcastMessage(formatMessage(msg).replace("<vencedor>", vencedor.getName()).replace("<perdedor>", lutadores.get(1).getName()));
			}
		}
		
		if (participantes.size() == 1) {
			Player p = participantes.get(0);
			setVencedor(p);
			teleportSaida(p);
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
			finalizar();
			return;
		}else{
			teleportEntrada(vencedor);
			vencedor.getInventory().clear();
			lutadores.clear();
		}
		
		iniciarLuta();
	}
	
	
	public static void setVencedor(Player p) {
    	if(Main.apiutility) {
    		Main.getMetodos().sendTweet(p.getName()+" VENCEU O EVENTO FIGHT.");
    	}
		for (String msg : getVenceuMensagens()) {
			Bukkit.broadcastMessage(formatMessage(msg).replace("<vencedor>", p.getName()));
		}
		
		if (Main.getInstance().getConfig().getBoolean("Premios.Dinheiro.Ativar")) {
			Hooks.eco.depositPlayer(participantes.get(0).getName(), Main.getInstance().getConfig().getInt("Premios.Dinheiro.Quantidade"));
		}
		
		files.getDataFile().set("Vencedor", p.getName());
		files.saveDataFile();
		
		PlayerUtils.desativarClanFF(p);
		
	}
	
	public static void giveItem(Player p) {
		if(p.hasPermission("pdgh.vip")) {
			ItemStack espada = new ItemStack(Material.WOOD_SWORD, 1);
			espada.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			ItemStack elmo = new ItemStack(Material.CHAINMAIL_HELMET, 1);
			elmo.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL , 1);
			ItemStack peito = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
			peito.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL , 1);
			ItemStack calca = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
			calca.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL , 1);
			ItemStack bota = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
			bota.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL , 1);
			p.getInventory().addItem(espada);
			p.getInventory().addItem(new ItemStack(Material.IRON_HOE, 1));
			p.getInventory().addItem(new ItemStack(Material.FLINT, 30));
			p.getInventory().setHelmet(elmo);
			p.getInventory().setChestplate(peito);
			p.getInventory().setLeggings(calca);
			p.getInventory().setBoots(bota);
		}else{
			ItemStack espada = new ItemStack(Material.WOOD_SWORD, 1);
			espada.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			ItemStack elmo = new ItemStack(Material.CHAINMAIL_HELMET, 1);
			elmo.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL , 1);
			ItemStack peito = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
			peito.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL , 1);
			ItemStack calca = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
			calca.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL , 1);
			ItemStack bota = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
			bota.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL , 1);
			p.getInventory().addItem(espada);
			p.getInventory().addItem(new ItemStack(Material.STONE_HOE, 1));
			p.getInventory().addItem(new ItemStack(Material.FLINT, 30));
			p.getInventory().setHelmet(elmo);
			p.getInventory().setChestplate(peito);
			p.getInventory().setLeggings(calca);
			p.getInventory().setBoots(bota);
		}
	}
}
