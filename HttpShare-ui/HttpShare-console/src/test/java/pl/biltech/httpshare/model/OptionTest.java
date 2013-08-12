package pl.biltech.httpshare.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;

import java.util.List;

import org.junit.Test;

/**
 * @author tomek
 * 
 */
public class OptionTest {

	@Test
	public void shouldReturnOptionAfterModifier() throws Exception {
		Option result = Option.byModifier("--no-ui");

		assertThat(result, equalTo(Option.NO_UI));
	}

	@Test
	public void shouldReturnNullForNonExistentModifier() throws Exception {
		Option result = Option.byModifier("thereIsNoModifierLikeThis :-)");

		assertThat(result, nullValue());
	}

	@Test
	public void shouldReturnAllModifiers() throws Exception {
		List<String> modifiers = Option.modifiers();

		// returned modifiers contain all modifiers for Options
		for (Option option : Option.values()) {
			assertThat(modifiers, hasItem(option.modifier()));
		}

		// this will prevent from picking up wider collections than one
		// containing valid modifiers only
		assertThat(Option.values().length, equalTo(modifiers.size()));
	}
}
