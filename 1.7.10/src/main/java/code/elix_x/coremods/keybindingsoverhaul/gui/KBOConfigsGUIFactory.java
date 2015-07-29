package code.elix_x.coremods.keybindingsoverhaul.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import code.elix_x.coremods.keybindingsoverhaul.KeyBindingsOverhaulBase;
import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler;
import code.elix_x.mods.powerofbreathing.BreathModBase;
import code.elix_x.mods.powerofbreathing.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.config.DummyConfigElement.DummyCategoryElement;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class KBOConfigsGUIFactory implements IModGuiFactory{

	public KBOConfigsGUIFactory() {

	}

	@Override
	public void initialize(Minecraft minecraftInstance) {

	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return KBOConfigsGUI.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

	public static class KBOConfigsGUI extends GuiConfig{

		public KBOConfigsGUI(GuiScreen parent) {
			super(parent, getConfigElements(), KeyBindingsOverhaulBase.MODID, false, false, I18n.format(KeyBindingsOverhaulBase.MODID + ".config.main.title"), getAbridgedConfigPath(KeyBindingHandler.config.getConfigFile().getAbsolutePath()));
		}

		private static List<IConfigElement> getConfigElements()
		{
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			Map<String, ConfigCategory> categories = ReflectionHelper.getPrivateValue(Configuration.class, KeyBindingHandler.config, "categories");
			for(Entry<String, ConfigCategory> entry : categories.entrySet()){
				if(entry.getValue().getName().equals(Configuration.CATEGORY_GENERAL)){
					list.addAll(getConfigElementsForCategory(entry.getValue()));
				} else {
					list.add(new DummyCategoryElement(entry.getKey(), KeyBindingsOverhaulBase.MODID + ".config." + entry.getKey(), getConfigElementsForCategory(entry.getValue())));
				}
			}
			return list;
		}

		private static List<IConfigElement> getConfigElementsForCategory(ConfigCategory category) {
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			for(ConfigCategory children : category.getChildren()){
				list.add(new DummyCategoryElement(children.getName(), children.getLanguagekey(), getConfigElementsForCategory(children)));
			}
			for(Entry<String, Property> e : category.entrySet()){
				list.add(ConfigElement.getTypedElement(e.getValue()));
			}
			return list;
		}

	}

}
