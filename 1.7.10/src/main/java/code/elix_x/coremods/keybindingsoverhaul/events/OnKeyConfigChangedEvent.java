package code.elix_x.coremods.keybindingsoverhaul.events;

import code.elix_x.coremods.keybindingsoverhaul.KeyBindingsOverhaulBase;
import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class OnKeyConfigChangedEvent {
	
	public OnKeyConfigChangedEvent() {
		
	}
	
	@SubscribeEvent
	public void changed(OnConfigChangedEvent event){
		if(event.modID.equals(KeyBindingsOverhaulBase.MODID)){
			KeyBindingHandler.readConfigValues();
			KeyBindingHandler.saveConfig();
		}
	}

}
