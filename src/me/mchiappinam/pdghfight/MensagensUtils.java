package me.mchiappinam.pdghfight;

import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class MensagensUtils {
	
	static FileConfiguration mensagens = Files.getInstance().getMensagensFile();
	
	public static String getPrefix() {
		return formatMessage(mensagens.getString("PREFIX"));
	}

	public static String formatMessage(String msg) {
		return msg
				.replace("&", "�")
				.replace("&", "�")
				.replace("voce", "voc�")
				.replace("Voce", "Voc�")
				.replace("nao", "n�o")
				.replace("Nao", "N�o")
				.replace("ninguem", "ningu�m")
				.replace("Ninguem", "Ningu�m")
				.replace("numero", "n�mero")
				.replace("Numero", "N�mero")
				.replace("sera", "ser�")
				.replace("proxima", "pr�xima")
				.replace("Proxima", "Pr�xima")
				.replace("<prefix>", mensagens.getString("PREFIX").replace("&", "�"))
				.replace("<participantes>", Integer.toString(FightUtils.participantes.size()))
				.replace("%>>%", "�")
				.replace("<status>", Boolean.toString(FightUtils.isAcontecendo()).replace("true", "Sim").replace("false", "N�o"));
	}
	
	public static String getMensagem(String mensagem) {
		return formatMessage(mensagens.getString(mensagem));
	}
	
	public static List<String> getIniciandoMensagens() {
		return mensagens.getStringList("AVISO_INICIANDO");
	}
	
	public static List<String> getMinPlayerMensagens() {
		return mensagens.getStringList("MINIMO_PLAYERS");
	}
	
	public static List<String> getIniciadoMensagens() {
		return mensagens.getStringList("EVENTO_INICIADO");
	}
	
	public static List<String> getProximaLutaMensagens() {
		return mensagens.getStringList("PROXIMA_LUTA");
	}
	
	public static List<String> getLutaMensagens() {
		return mensagens.getStringList("INICIANDO_LUTA");
	}
	
	public static List<String> getFinalizadoPorAdmMensagens() {
		return mensagens.getStringList("FINALIZADO_POR_ADMIN");
	}
	
	public static List<String> getMorreuMensagens() {
		return mensagens.getStringList("MORREU");
	}
	
	public static List<String> getVenceuMensagens() {
		return mensagens.getStringList("VENCEU");
	}
	
	public static List<String> getStatusTemplate() {
		return mensagens.getStringList("TEMPLATE_STATUS");
	}
}
