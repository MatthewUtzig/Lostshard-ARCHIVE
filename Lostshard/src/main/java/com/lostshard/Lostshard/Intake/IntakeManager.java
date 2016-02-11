package com.lostshard.Lostshard.Intake;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.lostshard.Lostshard.Commands.AdminCommand;
import com.lostshard.Lostshard.Commands.BankCommand;
import com.lostshard.Lostshard.Commands.BlackSmithyCommand;
import com.lostshard.Lostshard.Commands.ChatCommand;
import com.lostshard.Lostshard.Commands.ChestRefillCommand;
import com.lostshard.Lostshard.Commands.ClanCommand;
import com.lostshard.Lostshard.Commands.ControlPointsCommand;
import com.lostshard.Lostshard.Commands.FishingCommand;
import com.lostshard.Lostshard.Commands.MageryCommand;
import com.lostshard.Lostshard.Commands.PartyCommands;
import com.lostshard.Lostshard.Commands.PlotCommand;
import com.lostshard.Lostshard.Commands.PlotNPCCommand;
import com.lostshard.Lostshard.Commands.ReloadCommand;
import com.lostshard.Lostshard.Commands.RuneBookCommand;
import com.lostshard.Lostshard.Commands.ScrollsCommand;
import com.lostshard.Lostshard.Commands.SkillsCommand;
import com.lostshard.Lostshard.Commands.StoreCommand;
import com.lostshard.Lostshard.Commands.SurvivalismCommand;
import com.lostshard.Lostshard.Commands.TamingCommand;
import com.lostshard.Lostshard.Commands.TitleCommand;
import com.lostshard.Lostshard.Commands.UtilsCommand;
import com.lostshard.Lostshard.Intake.Modules.OfflinePlayerModule;
import com.lostshard.Lostshard.Intake.Modules.PlayerModule;
import com.lostshard.Lostshard.Intake.Modules.PlotModule;
import com.lostshard.Lostshard.Intake.Modules.PseudoPlayerModule;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Utils.Output;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.CommandMapping;
import com.sk89q.intake.Intake;
import com.sk89q.intake.InvalidUsageException;
import com.sk89q.intake.InvocationCommandException;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.MissingArgumentException;
import com.sk89q.intake.argument.Namespace;
import com.sk89q.intake.argument.UnusedArgumentException;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.fluent.CommandGraph;
import com.sk89q.intake.parametric.Injector;
import com.sk89q.intake.parametric.ParametricBuilder;
import com.sk89q.intake.parametric.ProvisionException;
import com.sk89q.intake.parametric.provider.PrimitivesModule;
import com.sk89q.intake.util.auth.AuthorizationException;

public class IntakeManager implements CommandExecutor, TabCompleter {
	private final Dispatcher dispatcher;
	
	public IntakeManager() {
		Injector injector = Intake.createInjector();
		injector.install(new PrimitivesModule());
        injector.install(new PlayerModule());
        injector.install(new PlotModule());
        injector.install(new CommandSenderModule());
        injector.install(new PseudoPlayerModule());
        injector.install(new OfflinePlayerModule());

        ParametricBuilder builder = new ParametricBuilder(injector);
        builder.setAuthorizer(new LostshardAuthorizer());
        
        
        dispatcher =  new CommandGraph()
        .builder(builder)
        .commands()
        .registerMethods(new BankCommand())
        .registerMethods(new TamingCommand())
        .registerMethods(new AdminCommand())
        .registerMethods(new SurvivalismCommand())
        .registerMethods(new UtilsCommand())
        .registerMethods(new ChatCommand())
        .registerMethods(new ControlPointsCommand())
        .registerMethods(new ReloadCommand())
        .registerMethods(new FishingCommand())
        .registerMethods(new BlackSmithyCommand())
        .registerMethods(new StoreCommand())
        .registerMethods(new SkillsCommand())
        .registerMethods(new MageryCommand())
        .group("runebook")
        	.registerMethods(new RuneBookCommand())
        	.parent()
        .group("spellbook")
        	.registerMethods(new SkillsCommand())
        	.parent()
        .group("scrolls")
        	.registerMethods(new ScrollsCommand())
        	.parent()
        .group("titles")
        	.registerMethods(new TitleCommand())
        	.parent()
        .group("clan")
        	.registerMethods(new ClanCommand())
        	.parent()
        .group("dc")
        	.registerMethods(new ChestRefillCommand())
        	.parent()
        .group("party")
        	.registerMethods(new PartyCommands())
        	.parent()
        .group("plot")
        	.registerMethods(new PlotCommand())
        	.group("npc")
        		.registerMethods(new PlotNPCCommand())
        	.parent()
        .graph()
        .getDispatcher();
	}
	
	public void setCommandExecutors(Lostshard lostshard) {
		for(CommandMapping c : dispatcher.getCommands())
        	lostshard.getCommand(c.getPrimaryAlias()).setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(this.dispatcher.contains(cmd.getName())){
			Namespace namespace = new Namespace();
			namespace.put(CommandSender.class, sender);
			try {
				return this.dispatcher.call(cmd.getName()+" "+StringUtils.join(args, " "), namespace, ImmutableList.<String>of());
			} catch (InvalidUsageException e) {
				if(e.getCause() instanceof MissingArgumentException)
					Output.simpleError(sender, "Too few parameters!");
				else if(e.getCause() instanceof UnusedArgumentException)
					Output.simpleError(sender, "Too many parameters!");
				else if(e.getCause() instanceof ArgumentParseException) {
					ArgumentException cause = (ArgumentException) e.getCause();
					Output.simpleError(sender, cause.getMessage());
				}
				StringBuilder usage = new StringBuilder();
				usage.append("Usage: /");
				usage.append(Joiner.on(" ").join(e.getAliasStack()));
				usage.append(" "+e.getCommand().getDescription().getUsage());
				Output.simpleError(sender, usage.toString());
			} catch (CommandException e) {
				e.printStackTrace();
			} catch (InvocationCommandException e) {
				if(e.getCause() instanceof ProvisionException) {
					ProvisionException cause = (ProvisionException) e.getCause();
					Output.simpleError(sender, cause.getMessage());
				} else {
					Output.simpleError(sender, "Report this to an admin.");
					e.printStackTrace();
				}
			} catch (AuthorizationException e) {
				Output.simpleError(sender, "Sorry, you do not have permission.");
			}
		}
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String commandLabel, String[] args) {
		Namespace namespace = new Namespace();
		namespace.put(CommandSender.class, sender);
		try {
			List<String> test = this.dispatcher.getSuggestions(command.getName()+" "+StringUtils.join(args, " "), namespace);
			return test;
		} catch (CommandException e) {
			e.printStackTrace();
		}
		return ImmutableList.of();
	}

	/**
	 * @return the dispatcher
	 */
	public Dispatcher getDispatcher() {
		return this.dispatcher;
	}
}
