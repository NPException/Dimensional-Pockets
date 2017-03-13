package net.gtn.dimensionalpocket.client.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.gtn.dimensionalpocket.oc.api.configuration.ConfigHandler;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;


public class RenderTweakCommand implements ICommand {

	private static final String OPTION_COLORBLIND = "colorblindMode";
	private static final String OPTION_FANCY = "fancyRendering";
	private static final String OPTION_FIELD_SHADER = "useFieldShader";
	private static final String OPTION_PARTICLE_PLANES = "particlePlanes";
	private static final String OPTION_SAVE = "save";
	private static final String OPTION_HELP = "help";

	private static final List<String> ALL_OPTIONS = new ArrayList<>();
	private static final List<String> SHORT_DESCRIPTIONS = new ArrayList<>();

	static {
		ALL_OPTIONS.add(OPTION_COLORBLIND);
		SHORT_DESCRIPTIONS.add("turn colorblind mode on/off");

		ALL_OPTIONS.add(OPTION_FANCY);
		SHORT_DESCRIPTIONS.add("force fancy rendering. 0-auto, 1-off, 2-on");

		ALL_OPTIONS.add(OPTION_FIELD_SHADER);
		SHORT_DESCRIPTIONS.add("turn shader for particle field on/off");

		ALL_OPTIONS.add(OPTION_PARTICLE_PLANES);
		SHORT_DESCRIPTIONS.add("adjust the 'thickness' of the particle field");

		ALL_OPTIONS.add(OPTION_SAVE);
		SHORT_DESCRIPTIONS.add("saves the current render settings");

		ALL_OPTIONS.add(OPTION_HELP);
		SHORT_DESCRIPTIONS.add("shows this help information");
	}

	private List<String> aliases = Arrays.asList("dimptweak", "dptweak");

	@Override
	public int compareTo(Object o) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "dptweak";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "§lUsage: §rdptweak <§ooption§r> [§ovalue§r]";
	}

	@Override
	public List<String> getCommandAliases() {
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if (args.length > 0) {
			switch (args[0]) {
				case OPTION_COLORBLIND:
					if (args.length > 1) {
						Reference.COLOR_BLIND_MODE = Boolean.parseBoolean(args[1]);
						sender.addChatMessage(new ChatComponentText(OPTION_COLORBLIND + " set to: " + Reference.COLOR_BLIND_MODE));
					} else {
						sender.addChatMessage(new ChatComponentText("Current value for " + OPTION_COLORBLIND + ": " + Reference.COLOR_BLIND_MODE));
					}
					return;
				case OPTION_FANCY:
					if (args.length > 1) {
						Reference.FORCE_FANCY_RENDERING = Integer.parseInt(args[1]);
						sender.addChatMessage(new ChatComponentText(OPTION_FANCY + " set to: " + Reference.FORCE_FANCY_RENDERING));
					} else {
						sender.addChatMessage(new ChatComponentText("Current value for " + OPTION_FANCY + ": " + Reference.FORCE_FANCY_RENDERING));
					}
					return;
				case OPTION_FIELD_SHADER:
					if (args.length > 1) {
						Reference.USE_SHADER_FOR_PARTICLE_FIELD = Boolean.parseBoolean(args[1]);
						sender.addChatMessage(new ChatComponentText(OPTION_FIELD_SHADER + " set to: " + Reference.USE_SHADER_FOR_PARTICLE_FIELD));
					} else {
						sender.addChatMessage(new ChatComponentText("Current value for " + OPTION_FIELD_SHADER + ": " + Reference.USE_SHADER_FOR_PARTICLE_FIELD));
					}
					return;
				case OPTION_PARTICLE_PLANES:
					if (args.length > 1) {
						try {
							int newValue = Integer.parseInt(args[1]);
							if (newValue >= 1 && newValue <= 50) {
								Reference.NUMBER_OF_PARTICLE_PLANES = newValue;
								sender.addChatMessage(new ChatComponentText(OPTION_PARTICLE_PLANES + " set to: " + Reference.NUMBER_OF_PARTICLE_PLANES));
								return;
							}
						} catch (NumberFormatException ignored) {
							// ignore
						}
						sender.addChatMessage(new ChatComponentText("Valid values for " + OPTION_PARTICLE_PLANES + " range from 1 to 50"));
					}
					sender.addChatMessage(new ChatComponentText("Current value for " + OPTION_PARTICLE_PLANES + ": " + Reference.NUMBER_OF_PARTICLE_PLANES));
					return;
				case OPTION_SAVE:
					ConfigHandler.save(Reference.MOD_ID);
					sender.addChatMessage(new ChatComponentText("Saved current graphics settings to DP config file."));
					return;
				case OPTION_HELP:
				default:
					// fall through to no args call
			}
		}
		sender.addChatMessage(new ChatComponentText(""));
		sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
		sender.addChatMessage(new ChatComponentText("Valid options: "));
		for (int i = 0; i < ALL_OPTIONS.size(); i++) {
			ChatComponentText option = new ChatComponentText("  " + ALL_OPTIONS.get(i));
			ChatStyle style = option.getChatStyle();
			style.setColor(EnumChatFormatting.GOLD);
			sender.addChatMessage(option);

			ChatComponentText desc = new ChatComponentText("    " + SHORT_DESCRIPTIONS.get(i));
			style = desc.getChatStyle();
			style.setItalic(Boolean.TRUE);
			style.setColor(EnumChatFormatting.GRAY);
			sender.addChatMessage(desc);
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
		if (args.length > 1) {
			return null;
		}
		List<String> result = new ArrayList<>(ALL_OPTIONS.size());
		for (String option : ALL_OPTIONS) {
			if (option.toLowerCase().startsWith(args[0].toLowerCase())) {
				result.add(option);
			}
		}
		return result;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		return false;
	}

}
