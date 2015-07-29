package code.elix_x.coremods.keybindingsoverhaul.gui;

import java.util.HashSet;
import java.util.Set;

import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler;
import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler.AdvancedKeyBinding;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiKeyBindingList;
import net.minecraft.client.gui.GuiOptionButton;
import net.minecraft.client.gui.GuiOptionSlider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

@SideOnly(Side.CLIENT)
public class NewGuiControls extends GuiScreen
{
	private static final GameSettings.Options[] field_146492_g = new GameSettings.Options[] {GameSettings.Options.INVERT_MOUSE, GameSettings.Options.SENSITIVITY, GameSettings.Options.TOUCHSCREEN};
	/** A reference to the screen object that created this. Used for navigating between screens. */
	private GuiScreen parentScreen;
	protected String field_146495_a = "Controls";
	/** Reference to the GameSettings object. */
	private GameSettings options;
	/** The ID of the button that has been pressed. */
	public AdvancedKeyBinding buttonId = null;
	public Set<Integer> set = new HashSet<Integer>();
	public long field_152177_g;
	private NewGuiKeyBindingList keyBindingList;
	private GuiButton resetButton;

	public NewGuiControls(GuiScreen p_i1027_1_, GameSettings p_i1027_2_)
	{
		this.parentScreen = p_i1027_1_;
		this.options = p_i1027_2_;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		this.keyBindingList = new NewGuiKeyBindingList(this, this.mc);
		this.buttonList.add(new GuiButton(200, this.width / 2 - 155, this.height - 29, 150, 20, I18n.format("gui.done", new Object[0])));
		this.buttonList.add(this.resetButton = new GuiButton(201, this.width / 2 - 155 + 160, this.height - 29, 150, 20, I18n.format("controls.resetAll", new Object[0])));
		this.field_146495_a = I18n.format("controls.title", new Object[0]);
		int i = 0;
		GameSettings.Options[] aoptions = field_146492_g;
		int j = aoptions.length;

		for (int k = 0; k < j; ++k)
		{
			GameSettings.Options options = aoptions[k];

			if (options.getEnumFloat())
			{
				this.buttonList.add(new GuiOptionSlider(options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), options));
			}
			else
			{
				this.buttonList.add(new GuiOptionButton(options.returnEnumOrdinal(), this.width / 2 - 155 + i % 2 * 160, 18 + 24 * (i >> 1), options, this.options.getKeyBinding(options)));
			}

			++i;
		}
	}

	protected void actionPerformed(GuiButton p_146284_1_)
	{
		if (p_146284_1_.id == 200)
		{
			this.mc.displayGuiScreen(this.parentScreen);
		}
		else if (p_146284_1_.id == 201)
		{
			for(AdvancedKeyBinding key : KeyBindingHandler.keys){
				key.def();
			}

			KeyBinding.resetKeyBindingArrayAndHash();
		}
		else if (p_146284_1_.id < 100 && p_146284_1_ instanceof GuiOptionButton)
		{
			this.options.setOptionValue(((GuiOptionButton)p_146284_1_).returnEnumOptions(), 1);
			p_146284_1_.displayString = this.options.getKeyBinding(GameSettings.Options.getEnumOptions(p_146284_1_.id));
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int p_73864_1_, int p_73864_2_, int id)
	{
		if (this.buttonId != null)
		{
			/* this.options.setOptionKeyBinding(this.buttonId, -100 + p_73864_3_);
            this.buttonId = null;
            KeyBinding.resetKeyBindingArrayAndHash();*/
			set.add(-100 + id);
			buttonId.keyIds.clear();
			buttonId.keyIds.addAll(set);
		}
		else if (id != 0 || !this.keyBindingList.func_148179_a(p_73864_1_, p_73864_2_, id))
		{
			super.mouseClicked(p_73864_1_, p_73864_2_, id);
		}
	}

	/**
	 * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
	 * mouseMove, which==0 or which==1 is mouseUp
	 */
	protected void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
	{
		if (p_146286_3_ != 0 || !this.keyBindingList.func_148181_b(p_146286_1_, p_146286_2_, p_146286_3_))
		{
			super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char p_73869_1_, int id)
	{
		if (this.buttonId != null)
		{
			if (id == 1)
			{
				buttonId.keyIds.clear();
				buttonId.keyIds.addAll(set);
				buttonId = null;
				set.clear();
				field_152177_g = Minecraft.getSystemTime();
				KeyBinding.resetKeyBindingArrayAndHash();
			}
			else
			{
				set.add(id);
				this.buttonId.keyIds.clear();
				this.buttonId.keyIds.addAll(set);
			}
		}
		else
		{
			super.keyTyped(p_73869_1_, id);
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
	{
		this.drawDefaultBackground();
		this.keyBindingList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
		this.drawCenteredString(this.fontRendererObj, this.field_146495_a, this.width / 2, 8, 16777215);
		boolean allDefault = true;
		
		for(AdvancedKeyBinding key : KeyBindingHandler.keys){
			if(!key.isDefault()){
				allDefault = false;
				break;
			}
		}

		this.resetButton.enabled = !allDefault;
		super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
	}
}
