package code.elix_x.coremods.keybindingsoverhaul.core;

import java.io.PrintWriter;

import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

public class AsmHooks {

	/*
	 * Key Bindings
	 */
	/*public static void onTick(int id)
	{

	}

	public static void setKeyBindState(int id, boolean state)
	{

	}

	public static void resetKeyBindingArrayAndHash()
	{

	}*/

	public static void register(int id, KeyBinding keybinding){
		KeyBindingHandler.register(id, keybinding);
	}

	/*
	 * Minecraft
	 */

	/*public static void runTick(){
		InputHandler.runTick();
	}*/

	/*
	 * GameSettings
	 */
	
	public static void loadSettings(GameSettings settings){
		KeyBindingHandler.loadSettings(settings);
	}
	
	public static void saveSettings(PrintWriter printWriter){
		KeyBindingHandler.saveSettings(printWriter);
	}
}
