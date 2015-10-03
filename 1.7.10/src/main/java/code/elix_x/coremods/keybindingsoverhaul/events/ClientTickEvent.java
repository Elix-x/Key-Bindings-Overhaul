package code.elix_x.coremods.keybindingsoverhaul.events;

import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class ClientTickEvent {

	public ClientTickEvent() {
		
	}
	
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event){
		if(event.phase == Phase.START){
			KeyBindingHandler.updateKeys();
		}
	}
}
