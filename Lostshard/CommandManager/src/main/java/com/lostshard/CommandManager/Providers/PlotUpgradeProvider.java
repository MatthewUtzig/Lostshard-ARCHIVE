package com.lostshard.CommandManager.Providers;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Pattern;

import com.lostshard.Lostshard.Objects.Plot.Plot.PlotUpgrade;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class PlotUpgradeProvider implements Provider<PlotUpgrade> {
	
	private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");
	
	@Override
	public boolean isProvided() {
		return false;
	}

	@Override
	public PlotUpgrade get(CommandArgs arguments, List<? extends Annotation> modifiers)
			throws ArgumentException, ProvisionException {
		String name = arguments.next();
        String test = simplify(name);

        for (PlotUpgrade e : PlotUpgrade.values()) {
            if (simplify(e.name()).equalsIgnoreCase(test)) {
                return e;
            }
        }

        throw new ArgumentParseException("The upgrade \""+test+"\" does not exist.");
	}

	@Override
	public List<String> getSuggestions(String prefix) {
		return null;
	}
	
    private static String simplify(String t) {
        return NON_ALPHANUMERIC.matcher(t.toLowerCase()).replaceAll("");
    }

}
