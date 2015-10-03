package code.elix_x.coremods.keybindingsoverhaul;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import code.elix_x.coremods.keybindingsoverhaul.api.KBOAPI;
import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler;
import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler.AdvancedSimpleKeyBinding;
import code.elix_x.coremods.keybindingsoverhaul.events.ClientTickEvent;
import code.elix_x.coremods.keybindingsoverhaul.events.OnKeyConfigChangedEvent;
import code.elix_x.coremods.keybindingsoverhaul.events.OpenKeyGuiEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = KeyBindingsOverhaulBase.MODID, name = KeyBindingsOverhaulBase.NAME, version = KeyBindingsOverhaulBase.VERSION, dependencies = "required-after:excore", guiFactory = "code.elix_x.coremods.keybindingsoverhaul.gui.KBOConfigsGUIFactory")
public class KeyBindingsOverhaulBase {

	public static final String MODID = "keybindingsoverhaul";
	public static final String NAME = "Key Bindings Overhaul";
	public static final String VERSION = "1.3";	

	public static final Logger logger = LogManager.getLogger("Key Bindings Overhaul");

	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{ 
		KeyBindingHandler.preInit(event);
	}


	@EventHandler
	public void init(FMLInitializationEvent event)
	{ 
		FMLCommonHandler.instance().bus().register(new ClientTickEvent());
		MinecraftForge.EVENT_BUS.register(new OpenKeyGuiEvent());
		FMLCommonHandler.instance().bus().register(new OnKeyConfigChangedEvent());
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{ 
		
	}
}
