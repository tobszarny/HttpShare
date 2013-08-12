package pl.biltech.httpshare.controller;

import java.util.Arrays;
import java.util.List;

import pl.biltech.httpshare.annotation.VisibleForTesting;

/**
 * @author tomek
 * 
 */
public class ConsoleArgumentsParser {

	private static final List<String> OPTION_STRINGS = Arrays.asList("--share",
			"--drop-in", "--no-ui");
	private static final String ARGUMENT_VALUE_SEPARATOR = "=";

	public ConsoleArgumentsParser() {

	}

	public void parse(String[] args) {

	}

	@VisibleForTesting void validateOption(String arg) {
		if(!OPTION_STRINGS.contains(arg)) {
			throw new IllegalArgumentException(arg);
		}
	}
	
	@VisibleForTesting void validateValue(String arg, String value) {
		for (String option : OPTION_STRINGS) {
			
		}
		
		if(OPTION_STRINGS.get(0).equals(arg)) {
			
		}
	}

}
