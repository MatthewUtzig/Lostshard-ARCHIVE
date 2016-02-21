package com.lostshard.CommandManager.Providers;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;
import com.lostshard.Lostshard.NPC.NPCType;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class NPCTypeProvider implements Provider<NPCType> {

	private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");
	
	@Override
	public boolean isProvided() {
		return false;
	}

	@Override
	public NPCType get(CommandArgs arguments, List<? extends Annotation> modifiers)
			throws ArgumentException, ProvisionException {
		String name = arguments.next();
        String test = simplify(name);

        CommandSender sender = arguments.getNamespace().get(CommandSender.class);
        
        for (NPCType e : NPCType.values()) {
        	if(!sender.isOp() && (e.equals(NPCType.GUARD) || e.equals(NPCType.BOUNTYMASTER))) {
        		throw new ArgumentParseException("The npc type \""+test+"\" does not exist.");
        	}
            if (simplify(e.name()).equalsIgnoreCase(test)) {
                return e;
            }
        }

        throw new ArgumentParseException("The npc type \""+test+"\" does not exist.");
	}

	@Override
	public List<String> getSuggestions(String prefix) {
		return ImmutableList.of();
	}

	private static String simplify(String t) {
        return NON_ALPHANUMERIC.matcher(t.toLowerCase()).replaceAll("");
    }
}
