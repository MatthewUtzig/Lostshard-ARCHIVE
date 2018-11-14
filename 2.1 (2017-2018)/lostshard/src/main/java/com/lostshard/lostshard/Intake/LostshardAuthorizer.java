package com.lostshard.lostshard.Intake;

import org.bukkit.command.CommandSender;

import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.util.auth.Authorizer;

public class LostshardAuthorizer implements Authorizer {

	@Override
	public boolean testPermission(Namespace namespace, String perm) {
		return namespace.get(CommandSender.class).hasPermission(perm);
	}

}
