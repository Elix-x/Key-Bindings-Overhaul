package code.elix_x.coremods.keybindingsoverhaul;

import java.io.File;
import java.util.Map;

import code.elix_x.coremods.keybindingsoverhaul.core.KeysOverhaulTransformer;
import code.elix_x.coremods.keybindingsoverhaul.core.KeysOverhaulTranslator;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Name(value = "keysoverhaul")
@TransformerExclusions(value = "code.elix_x.coremods.keybindingsoverhaul.core")
@MCVersion(value = "1.7.10")
public final class KeyBindingsOverhaulCore implements IFMLLoadingPlugin{

	//Dfml.coreMods.load=code.elix_x.coremods.keybindingsoverhaul.KeyBindingsOverhaulCore
	
	public static final String Transformer = KeysOverhaulTransformer.class.getName();
	
	public static final String[] transformers = new String[]{Transformer};
	
	public static File mcDir;
	
	@Override
	public String[] getASMTransformerClass() {
		return transformers;
//		return new String[]{};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return KeysOverhaulTranslator.class.getName();
	}

	@Override
	public void injectData(Map<String, Object> data) {
		mcDir = (File) data.get("mcLocation");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
