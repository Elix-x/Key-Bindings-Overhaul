package code.elix_x.coremods.keybindingsoverhaul.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import code.elix_x.coremods.keybindingsoverhaul.gui.NewGuiControls;

import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class OpenKeyGuiEvent {

	public OpenKeyGuiEvent() {
		
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void openGui(GuiOpenEvent event){
		if(event.gui instanceof GuiControls){
			event.gui = new NewGuiControls((GuiScreen) ReflectionHelper.getPrivateValue(GuiControls.class, (GuiControls) event.gui, "parentScreen", "field_146496_h"), Minecraft.getMinecraft().gameSettings);
		}
	}
	
}
