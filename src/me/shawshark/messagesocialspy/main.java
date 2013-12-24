package me.shawshark.messagesocialspy;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import lilypad.client.connect.api.Connect;
import lilypad.client.connect.api.event.EventListener;
import lilypad.client.connect.api.event.MessageEvent;
import lilypad.client.connect.api.request.RequestException;
import lilypad.client.connect.api.request.impl.MessageRequest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {

	Connect connect;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
        connect = Bukkit.getServer().getServicesManager().getRegistration(Connect.class).getProvider();
        connect.registerEvents(this);
	}
	
	@EventHandler
	public void onCommandEvent(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		String message = e.getMessage();
		if(e.getMessage().startsWith("/m") 
				|| (e.getMessage().startsWith("/message") 
						|| (e.getMessage().startsWith("/reply") 
								|| (e.getMessage().startsWith("/r") 
										|| e.getMessage().startsWith("whisper") || e.getMessage().startsWith("/msg"))))) {
			sendinfo(p.getName(), message);
		}
	}
	
	public void sendinfo(String pname, String message) {
		try {
			MessageRequest request = new MessageRequest(Collections.<String> emptyList(), 
					"messagesocialyspy", pname + "%,%" + message);
			connect.request(request); 
		} catch (UnsupportedEncodingException | RequestException e) {
			e.printStackTrace();
		} 
	}
	
    @EventListener
    public void onMessage(MessageEvent event) {
    	String[] data = null;
    	if(event.getChannel().equalsIgnoreCase("messagesocialyspy")) {
    		try { data = event.getMessageAsString().split("%,%"); } 
    			catch (UnsupportedEncodingException e) { e.printStackTrace();
    			return; 
    			}
    		String pname = data[0];
            String message = data[1];
            for(Player p : getServer().getOnlinePlayers()) {
            	if(p.hasPermission("message.socialspy")) {
            		p.sendMessage(ChatColor.RED + "[M]" + ChatColor.GOLD + " " + pname + ": " + message);
            	}
            }  
    	}
    } 
}
