package code.elix_x.coremods.keybindingsoverhaul.api;

import net.minecraft.client.settings.KeyBinding;
import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler.AdvancedKeyBinding;

/**
 * Main methods that you want to use...
 * @author elix_x
 *
 */
public class KBOAPI {

	/**
	 * Add {@link AdvancedKeyBinding} or replace existing one with same simple key binding.
	 * Allows changing default configuration of any key bindings.
	 * @param key = {@link AdvancedKeyBinding} to add. Note: simple key which has to be replaced is taken from it. Note: if no key binding with this simple key is found, it's just added to the list...
	 * @author elix_x
	 */
	public static void add(AdvancedKeyBinding key){
		KeyBindingHandler.add(key);
	}
	
	/**
	 * Finds {@link AdvancedKeyBinding} by simple key binding. Null if not found...
	 * @param key = Simple key binding used to find advanced key binding bound to it.
	 * @return {@link AdvancedKeyBinding} bound to this. Null if not found. Theoretically, null cannot be returned...
	 */
	public static AdvancedKeyBinding get(KeyBinding key){
		return KeyBindingHandler.get(key);
	}

}
