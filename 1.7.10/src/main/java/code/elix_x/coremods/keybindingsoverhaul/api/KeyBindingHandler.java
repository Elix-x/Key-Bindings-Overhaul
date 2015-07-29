package code.elix_x.coremods.keybindingsoverhaul.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler.AdvancedKeyBinding;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * Main class responsible for key combinations
 * Note: Don't use not annotated methods if you don't understand what they do...
 * @author elix_x
 *
 */
public class KeyBindingHandler {

	public static final Logger logger = LogManager.getLogger("KBO Key Handler");

	public static Configuration config;
	public static boolean allowInGuiKeyPress = false;

	public static Set<AdvancedKeyBinding> keys = new HashSet<AdvancedKeyBinding>();

	public static void preInit(FMLPreInitializationEvent event) {
		File configFile = new File(event.getModConfigurationDirectory(), "KBO.cfg");
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			logger.error("Caught exception while creating config file: ", e);
		}
		config = new Configuration(configFile);
		loadConfig();
		readConfigValues();
		saveConfig();
	}
	
	public static void loadConfig(){
		config.load();
	}
	
	public static void readConfigValues(){
		allowInGuiKeyPress = config.getBoolean("allowInGuiKeyPress", Configuration.CATEGORY_GENERAL, false, "If true, key presses while gui is opened will be detected and updated...");
	}
	
	public static void saveConfig(){
		config.save();
	}
	
	public static AdvancedKeyBinding get(KeyBinding key){
		for(AdvancedKeyBinding akey : keys){
			if(akey.getSimpleKeyBinding() == key){
				return akey;
			}
		}
		return null;
	}

	public static void add(AdvancedKeyBinding key){
		Iterator<AdvancedKeyBinding> iter = keys.iterator();
		while(iter.hasNext()){
			AdvancedKeyBinding akey = iter.next();
			if(akey.getSimpleKeyBinding() == key.keyBinding){
				iter.remove();
				break;
			}
		}
		keys.add(key);
	}

	public static void updateKeys(){
		if(allowInGuiKeyPress || Minecraft.getMinecraft().currentScreen == null){
			for(AdvancedKeyBinding key : keys){
				key.update();
			}
		}
	}

	public static void register(int id, KeyBinding keybinding) {
		keys.add(new AdvancedSimpleKeyBinding(keybinding, id));
	}

	public static void loadSettings(GameSettings gameSettings) {
		try{
			File settingsFile = ReflectionHelper.getPrivateValue(GameSettings.class,/* Minecraft.getMinecraft().gameSettings*/ gameSettings, "field_74354_ai", "optionsFile");
			if(!settingsFile.exists()){
				return;
			}

			String settings = "";

			BufferedReader bufferedreader = new BufferedReader(new FileReader(settingsFile));
			String s = "";

			while ((s = bufferedreader.readLine()) != null)
			{
				/*if(!s.startsWith("key_")){
					settings += s + "\n";
					System.out.println("Detected next line without key_ while scanning: " + s);
				}*/
				try
				{
					String[] astring = s.split(":");
					if(astring[0].startsWith("akey_")){
						setKeyValue(astring[0].replace("akey_", ""), astring[1]);
					}
				} catch(Exception e){
					/*System.out.println("Catched exception while loading settings:");
        			e.printStackTrace();*/
				}
			}

			bufferedreader.close();

			/*FileOutputStream out = new FileOutputStream(settingsFile);
			out.write(settings.getBytes());
			out.close();*/

		} catch(Exception e){
			System.out.println("Caught exception while loading settings:");
			e.printStackTrace();
		}
	}

	private static void setKeyValue(String name, String value) {
		for(AdvancedKeyBinding key : keys){
			if(key.getName().equals(name)){
				key.loadValue(value);
			}
		}
	}

	public static void saveSettings(PrintWriter writer) {
		for(AdvancedKeyBinding key : keys){
			writer.println("akey_" + key.getName() + ":" + key.saveValue());
		}
	}

	public static AdvancedKeyBinding getAdvancedKey(KeyBinding key){
		for(AdvancedKeyBinding k : keys){
			if(k.keyBinding == key){
				return k;
			}
		}
		return null;
	}

	/**
	 * Handles key combination and simple key binding.
	 * In order to work with other mods, updates each tick and sets pressing value of handled simple key to value of pressing of handled key combination.
	 * Needs defaultation specification...
	 * Note: Don't use not annotated methods if you don't understand what they do...
	 * @author elix_x
	 *
	 */
	public static abstract class AdvancedKeyBinding implements Comparable<AdvancedKeyBinding>{

		private KeyBinding keyBinding;
		public Set<Integer> keyIds = new HashSet<Integer>();

		public AdvancedKeyBinding(KeyBinding key) {
			keyBinding = key;
		}

		public String getName(){
			return keyBinding.getKeyDescription();
		}

		public void loadValue(String read){
			keyIds.clear();
			for(String s : read.split("\\+")){
				try{
					keyIds.add(Integer.parseInt(s));
				}catch(NumberFormatException e){

				}
			}
		}

		public String saveValue(){
			String ret = "";
			for(Integer i : keyIds){
				ret += i + "+";
			}
			return ret;
		}

		public String getDisplayString() {
			String ret = "";
			for(Integer i : keyIds){
				ret += GameSettings.getKeyDisplayString(i) + " + ";
			}
			if(ret.endsWith("+ ")){
				ret = ret.substring(0, ret.lastIndexOf("+ ") - 1);
			}
			return ret;
		}

		public KeyBinding getSimpleKeyBinding(){
			return keyBinding;
		}

		/**
		 * Check's if current key's value is default value.
		 * Used to enable "default" button in keys gui.
		 * @return if it is default.
		 */
		public abstract boolean isDefault();

		/**
		 * Default key values.
		 * Called when "default" button in keys gui is pressed.
		 */
		public abstract void def();

		public void update() {
			if(keyIds.isEmpty()){
				return;
			}

			boolean b = true;
			for(Integer i : keyIds){
				if(!isKeyDown(i)){
					b = false;
					break;
				}
			}
			if(b && !keyBinding.pressed){
				/*ReflectionHelper.setPrivateValue(KeyBinding.class, keyBinding, true, "field_74513_e", "pressed");

				int pressTime = ReflectionHelper.getPrivateValue(KeyBinding.class, keyBinding, "field_151474_i", "pressTime");
				ReflectionHelper.setPrivateValue(KeyBinding.class, keyBinding, pressTime + 1, "field_151474_i", "pressTime");*/
				keyBinding.pressed = true;
				keyBinding.pressTime++;
			} else if(!b) {
				/*ReflectionHelper.setPrivateValue(KeyBinding.class, keyBinding, false, "field_74513_e", "pressed");
				ReflectionHelper.setPrivateValue(KeyBinding.class, keyBinding, 0, "field_151474_i", "pressTime");*/
				keyBinding.pressed = false;
				keyBinding.pressTime = 0;
			}/* else if(b){
				
				
			}*/
			
			/*if(keyBinding == Minecraft.getMinecraft().gameSettings.keyBindUseItem){
				logger.info("Ticking right click key.");
				logger.info("Current press value is: " + (keyBinding.pressed ? "Pressed" : "Unpressed"));
//				if(b){
//					ReflectionHelper.setPrivateValue(Minecraft.class, Minecraft.getMinecraft(), 20, "rightClickDelayTimer");
//				}
				logger.info("MC's right click delay timer: " + ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), "rightClickDelayTimer"));
			}*/
			
		}

		public static boolean isKeyDown(int i) {
			return i < 0 ? Mouse.isButtonDown(i + 100) : Keyboard.isKeyDown(i);
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof AdvancedKeyBinding){
				return equals((AdvancedKeyBinding) obj);
			}
			return super.equals(obj);
		}

		public boolean equals(AdvancedKeyBinding key){
			if(key != null){
				if(!keyIds.isEmpty()){
					if(!key.keyIds.isEmpty()){
						return keyIds.containsAll(key.keyIds) && key.keyIds.containsAll(keyIds) && keyBinding.equals(key.keyBinding);
					}
				}
			}
			return false;
		}

		@Override
		public String toString() {
			return keyBinding.getKeyDescription() + "{" + keyIds + "}";
		}

		@Override
		public int compareTo(AdvancedKeyBinding key) {
			return keyBinding.compareTo(key.keyBinding);
		}

	}
	
	/**
	 * Advanced key binding with defaultation specified as resetting to original key binding's default value.
	 * Used to replace all vanilla key bindings...
	 * @author elix_x
	 *
	 */
	public static class AdvancedSimpleKeyBinding extends AdvancedKeyBinding {

		private final int def;
		
		public AdvancedSimpleKeyBinding(KeyBinding keybinding, int defId) {
			super(keybinding);
			def = defId;
		}
		
		public boolean isDefault(){
			return !keyIds.isEmpty() && keyIds.size() == 1 && keyIds.contains(def);
		}
		
		@Override
		public void def() {
			keyIds.clear();
			keyIds.add(def);
		}
		
	}

}
