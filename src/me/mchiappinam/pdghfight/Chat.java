package me.mchiappinam.pdghfight;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;

public class Chat implements Listener {

	@EventHandler
	void onChat(ChatMessageEvent e) {
		if (Main.getInstance().getConfig().getBoolean("Premios.Tag.Ativar")) {
			if (e.getTags().contains("fight") && e.getSender().getName().equals(Files.getInstance().getDataFile().getString("Vencedor"))) {
				e.setTagValue("fight", Main.getInstance().getConfig().getString("Premios.Tag.Formato").replace("&", "§"));
			}
		}
	}
}
