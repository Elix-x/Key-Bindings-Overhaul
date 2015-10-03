package code.elix_x.coremods.keybindingsoverhaul.gui;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.EnumChatFormatting;

import org.apache.commons.lang3.ArrayUtils;

import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler;
import code.elix_x.coremods.keybindingsoverhaul.api.KeyBindingHandler.AdvancedKeyBinding;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NewGuiKeyBindingList extends GuiListExtended
{
	private final NewGuiControls guiControls;
	private final Minecraft mc;
	private final GuiListExtended.IGuiListEntry[] field_148190_m;
	private int maxListLabelWidth = 0;

	public NewGuiKeyBindingList(NewGuiControls newGuiControls, Minecraft p_i45031_2_)
	{
		super(p_i45031_2_, newGuiControls.width, newGuiControls.height, 63, newGuiControls.height - 32, 20);
		this.guiControls = newGuiControls;
		this.mc = p_i45031_2_;
		AdvancedKeyBinding[] keys = KeyBindingHandler.keys.toArray(new AdvancedKeyBinding[]{});
		Arrays.sort(keys);
		this.field_148190_m = new GuiListExtended.IGuiListEntry[keys.length + KeyBinding.getKeybinds().size()];
		int i = 0;
		String s = null;
		for(AdvancedKeyBinding key : keys){
			KeyBinding keybinding = key.getSimpleKeyBinding();
			String s1 = keybinding.getKeyCategory();
			if (!s1.equals(s))
			{
				s = s1;
				this.field_148190_m[i++] = new NewGuiKeyBindingList.CategoryEntry(s1);
			}

			int l = p_i45031_2_.fontRenderer.getStringWidth(I18n.format(keybinding.getKeyDescription(), new Object[0]));

			if (l > this.maxListLabelWidth)
			{
				this.maxListLabelWidth = l;
			}

			this.field_148190_m[i++] = new KeyEntry(key, null);
		}
	}

	protected int getSize()
	{
		return this.field_148190_m.length;
	}

	/**
	 * Gets the IGuiListEntry object for the given index
	 */
	public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_)
	{
		return this.field_148190_m[p_148180_1_];
	}

	protected int getScrollBarX()
	{
		return super.getScrollBarX() + 15;
	}

	/**
	 * Gets the width of the list
	 */
	public int getListWidth()
	{
		return super.getListWidth() + 32;
	}

	@SideOnly(Side.CLIENT)
	public class CategoryEntry implements GuiListExtended.IGuiListEntry
	{
		private final String field_148285_b;
		private final int field_148286_c;

		public CategoryEntry(String p_i45028_2_)
		{
			this.field_148285_b = I18n.format(p_i45028_2_, new Object[0]);
			this.field_148286_c = NewGuiKeyBindingList.this.mc.fontRenderer.getStringWidth(this.field_148285_b);
		}

		public void drawEntry(int p_148279_1_, int p_148279_2_, int p_148279_3_, int p_148279_4_, int p_148279_5_, Tessellator p_148279_6_, int p_148279_7_, int p_148279_8_, boolean p_148279_9_)
		{
			NewGuiKeyBindingList.this.mc.fontRenderer.drawString(this.field_148285_b, NewGuiKeyBindingList.this.mc.currentScreen.width / 2 - this.field_148286_c / 2, p_148279_3_ + p_148279_5_ - NewGuiKeyBindingList.this.mc.fontRenderer.FONT_HEIGHT - 1, 16777215);
		}

		/**
		 * Returns true if the mouse has been pressed on this control.
		 */
		public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
		{
			return false;
		}

		/**
		 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
		 */
		public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_) {}
	}

	@SideOnly(Side.CLIENT)
	public class KeyEntry implements GuiListExtended.IGuiListEntry
	{
		private final AdvancedKeyBinding aKey;
		private final String keyDesc;
		private final GuiButton btnChangeKeyBinding;
		private final GuiButton btnReset;

		private KeyEntry(AdvancedKeyBinding key)
		{
			this.aKey = key;
			this.keyDesc = I18n.format(key.getSimpleKeyBinding().getKeyDescription(), new Object[0]);
			this.btnChangeKeyBinding = new GuiButton(0, 0, 0, 75, 18, I18n.format(key.getSimpleKeyBinding().getKeyDescription(), new Object[0]));
			this.btnReset = new GuiButton(0, 0, 0, 50, 18, I18n.format("controls.reset", new Object[0]));
		}

		public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, Tessellator tess, int mouseX, int mouseY, boolean isSelected)
		{
			boolean flag1 = guiControls.buttonId != null && guiControls.buttonId == this.aKey;
			mc.fontRenderer.drawString(this.keyDesc, x + 90 - maxListLabelWidth, y + slotHeight / 2 - mc.fontRenderer.FONT_HEIGHT / 2, 16777215);
			this.btnReset.xPosition = x + 190;
			this.btnReset.yPosition = y;
			this.btnReset.enabled = !aKey.isDefault();
			this.btnReset.drawButton(NewGuiKeyBindingList.this.mc, mouseX, mouseY);
			this.btnChangeKeyBinding.xPosition = x + 105;
			this.btnChangeKeyBinding.yPosition = y;
			this.btnChangeKeyBinding.displayString = aKey.getDisplayString();
			boolean flag2 = false;

			for (AdvancedKeyBinding key : KeyBindingHandler.keys) {
				if (key != null && key != aKey && key.equals(aKey)) {
					flag2 = true;
					break;
				}
			}

			if (flag1) {
				this.btnChangeKeyBinding.displayString = EnumChatFormatting.WHITE + "> " + EnumChatFormatting.GRAY + this.btnChangeKeyBinding.displayString + EnumChatFormatting.WHITE + " <";
			} else if(flag2 && aKey.keyIds.size() > 1){
				this.btnChangeKeyBinding.displayString = EnumChatFormatting.GREEN + this.btnChangeKeyBinding.displayString;
			} else if (flag2) {
				this.btnChangeKeyBinding.displayString = EnumChatFormatting.YELLOW + this.btnChangeKeyBinding.displayString;
			} else if(aKey.keyIds.size() > 1){
				this.btnChangeKeyBinding.displayString = EnumChatFormatting.BLUE + this.btnChangeKeyBinding.displayString;
			}

			this.btnChangeKeyBinding.drawButton(NewGuiKeyBindingList.this.mc, mouseX, mouseY);
		}

		/**
		 * Returns true if the mouse has been pressed on this control.
		 */
		public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
		{
			if (this.btnChangeKeyBinding.mousePressed(NewGuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_))
			{
				guiControls.buttonId = aKey;
				return true;
			}
			else if (this.btnReset.mousePressed(NewGuiKeyBindingList.this.mc, p_148278_2_, p_148278_3_))
			{
				aKey.def();
				KeyBinding.resetKeyBindingArrayAndHash();
				return true;
			}
			else
			{
				return false;
			}
		}

		/**
		 * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
		 */
		public void mouseReleased(int p_148277_1_, int p_148277_2_, int p_148277_3_, int p_148277_4_, int p_148277_5_, int p_148277_6_)
		{
			this.btnChangeKeyBinding.mouseReleased(p_148277_2_, p_148277_3_);
			this.btnReset.mouseReleased(p_148277_2_, p_148277_3_);
		}

		KeyEntry(AdvancedKeyBinding p_i45030_2_, Object p_i45030_3_)
		{
			this(p_i45030_2_);
		}
	}
}