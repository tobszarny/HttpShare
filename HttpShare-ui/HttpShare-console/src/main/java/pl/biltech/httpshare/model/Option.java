package pl.biltech.httpshare.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tomek
 * 
 */
public enum Option {

	SHARE("--share"), DROP("--drop-in"), NO_UI("--no-ui");

	private final String modiffier;

	private Option(String modiffier) {
		this.modiffier = modiffier;
	}

	public String modifier() {
		return modiffier;
	}

	public static Option byModifier(String modiffier) {
		for (Option option : Option.values()) {
			if (option.modiffier.equals(modiffier)) {
				return option;
			}
		}
		return null;
	}

	public static List<String> modifiers() {
		List<String> modiffierList = new ArrayList<String>();
		for (Option option : Option.values()) {
			modiffierList.add(option.modiffier);
		}
		return modiffierList;
	}

}
