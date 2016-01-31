package com.lostshard.Lostshard.Utils;

import org.bukkit.entity.Player;

public class Title {

	@Deprecated
	public static void sendFullTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {
		sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
	}

	@Deprecated
	public static void sendSubtitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
		sendTitle(player, fadeIn, stay, fadeOut, null, message);
	}

	public static void sendTabTitle(Player player, String header, String footer) {
		// if (header == null)
		// header = "";
		// header = ChatColor.translateAlternateColorCodes('&', header);
		//
		// if (footer == null)
		// footer = "";
		// footer = ChatColor.translateAlternateColorCodes('&', footer);
		//
		// header = header.replaceAll("%player%", player.getDisplayName());
		// footer = footer.replaceAll("%player%", player.getDisplayName());
		//
		// final PlayerConnection connection = ((CraftPlayer)
		// player).getHandle().playerConnection;
		// final IChatBaseComponent tabTitle = IChatBaseComponent.ChatSerializer
		// .a("{\"text\": \"" + header + "\"}");
		// final IChatBaseComponent tabFoot = IChatBaseComponent.ChatSerializer
		// .a("{\"text\": \"" + footer + "\"}");
		// final PacketPlayOutPlayerListHeaderFooter headerPacket = new
		// PacketPlayOutPlayerListHeaderFooter(
		// tabTitle);
		//
		// try {
		// final Field field = headerPacket.getClass().getDeclaredField("b");
		// field.setAccessible(true);
		// field.set(headerPacket, tabFoot);
		// } catch (final Exception e) {
		// e.printStackTrace();
		// } finally {
		// connection.sendPacket(headerPacket);
		// }
	}

	@Deprecated
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
		sendTitle(player, fadeIn, stay, fadeOut, message, null);
	}

	@SuppressWarnings("deprecation")
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {
		player.sendTitle(title, subtitle);
		// final PlayerConnection connection = ((CraftPlayer)
		// player).getHandle().playerConnection;
		//
		// final PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(
		// PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay,
		// fadeOut);
		// connection.sendPacket(packetPlayOutTimes);
		//
		// if (subtitle != null) {
		// subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
		// subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
		// final IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer
		// .a("{\"text\": \"" + subtitle + "\"}");
		// final PacketPlayOutTitle packetPlayOutSubTitle = new
		// PacketPlayOutTitle(
		// PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
		// connection.sendPacket(packetPlayOutSubTitle);
		// }
		//
		// if (title != null) {
		// title = title.replaceAll("%player%", player.getDisplayName());
		// title = ChatColor.translateAlternateColorCodes('&', title);
		// final IChatBaseComponent titleMain =
		// IChatBaseComponent.ChatSerializer
		// .a("{\"text\": \"" + title + "\"}");
		// final PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(
		// PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
		// connection.sendPacket(packetPlayOutTitle);
		// }
	}

}
