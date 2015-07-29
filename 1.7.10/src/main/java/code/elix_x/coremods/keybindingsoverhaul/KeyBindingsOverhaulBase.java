package code.elix_x.coremods.keybindingsoverhaul;

import net.minecraftforge.common.MinecraftForge;
import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler;
import code.elix_x.coremods.keybindingsoverhaul.events.ClientTickEvent;
import code.elix_x.coremods.keybindingsoverhaul.events.OnKeyConfigChangedEvent;
import code.elix_x.coremods.keybindingsoverhaul.events.OpenKeyGuiEvent;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = KeyBindingsOverhaulBase.MODID, name = KeyBindingsOverhaulBase.NAME, version = KeyBindingsOverhaulBase.VERSION, dependencies = "required-after:excore", guiFactory = "code.elix_x.coremods.keybindingsoverhaul.gui.KBOConfigsGUIFactory")
public class KeyBindingsOverhaulBase {

	public static final String MODID = "keybindingsoverhaul";
	public static final String NAME = "Key Bindings Overhaul";
	public static final String VERSION = "1.2.2";	

	@EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{ 
		if(event.getSide() == Side.CLIENT){
			KeyBindingHandler.preInit(event);
		}
	}


	@EventHandler
	public void init(FMLInitializationEvent event)
	{ 
		if(event.getSide() == Side.CLIENT){
			FMLCommonHandler.instance().bus().register(new ClientTickEvent());
			MinecraftForge.EVENT_BUS.register(new OpenKeyGuiEvent());
			FMLCommonHandler.instance().bus().register(new OnKeyConfigChangedEvent());
		}
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event)
	{ 
		if(event.getSide() == Side.CLIENT){

		}
	}

}
