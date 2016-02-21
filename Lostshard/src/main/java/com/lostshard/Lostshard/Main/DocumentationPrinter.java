package com.lostshard.Lostshard.Main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.lostshard.CommandManager.IntakeManager;
import com.sk89q.intake.CommandMapping;

public class DocumentationPrinter {

	public static void main(String[] args) {
		
		System.out.println("Creating plugin.yml");
		
		Map<String, Object> yml = new HashMap<String, Object>();
		
		yml.put("name", "Lostshard");
		yml.put("main", "com.lostshard.Lostshard.Main.Lostshard");
		yml.put("version", 0.2);
		yml.put("softdepend", new String[] {"Citizens"});
		
		IntakeManager intake = new IntakeManager();
		
		Map<String, Object> commands = new HashMap<String, Object>();
		
		for(CommandMapping cmd : intake.getDispatcher().getCommands()) {
			System.out.println("Added the command: "+cmd.getPrimaryAlias());
			Map<String, Object> desc = new HashMap<String, Object>();
			desc.put("description", cmd.getDescription().getShortDescription());
			desc.put("aliases", cmd.getAllAliases());
			StringBuilder usage = new StringBuilder();
			usage.append("/"+cmd.getPrimaryAlias());
			if(!cmd.getDescription().getUsage().isEmpty())
				usage.append(" "+cmd.getDescription().getUsage());
			desc.put("usage", usage.toString());
			commands.put(cmd.getPrimaryAlias(), desc);
		}
		
		yml.put("commands", commands);
		
		Yaml yaml = new Yaml();
		
		try {
			FileWriter w = new FileWriter("plugin.yml");
			try{
				yaml.dump(yml, w);
			} finally {
				w.flush();
				w.close();
            }
		} catch (IOException e) {
			System.out.println("error");
        	throw new RuntimeException(e);
		}
		
		System.out.println("plugin.yml have succesfully created");
	}
}
