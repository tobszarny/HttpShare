package pl.biltech.httpshare.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tomek
 * 
 */
public enum Option {

	SHARE("--share"), DROP("--drop-in"), NO_UI("--no-ui");

	private final String modifier;

	private Option(String modiffier) {
		this.modifier = modiffier;
	}

	public String modifier() {
		return modifier;
	}

	public static Option byModifier(String modifier) {
		for (Option option : Option.values()) {
			if (option.modifier.equals(modifier)) {
				return option;
			}
		}
		return null;
	}

	public static List<String> modifiers() {
		List<String> modiffierList = new ArrayList<String>();
		for (Option option : Option.values()) {
			modiffierList.add(option.modifier);
		}
		return modiffierList;
	}

}
