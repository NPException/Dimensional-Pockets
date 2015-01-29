package net.gtn.dimensionalpocket.client.commands;

import java.util.ArrayList;
import java.util.List;

import net.gtn.dimensionalpocket.common.lib.Reference;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class RenderTweakCommand implements ICommand {
    
    private static final String OPTION_COLORBLIND = "colorblindMode";
    private static final String OPTION_FANCY = "fancyRendering";
    private static final String OPTION_PLANE_COUNT = "particlePlanes";
    
    private static final List<String> ALL_OPTIONS = new ArrayList<>();
    static{ 
        ALL_OPTIONS.add(OPTION_COLORBLIND);
        ALL_OPTIONS.add(OPTION_FANCY);
        ALL_OPTIONS.add(OPTION_PLANE_COUNT);
    }
    
    private List<String> aliases = new ArrayList<>();
    {
        aliases.add("dimptweak");
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return "dimptweak";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "Usage: dimptweak <option> [value]";
    }

    @Override
    public List getCommandAliases() {
        return aliases;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case OPTION_COLORBLIND:
                    if (args.length > 1) {
                        boolean newValue = Boolean.parseBoolean(args[1]);
                        Reference.COLOR_BLIND_MODE = newValue;
                    } else {
                        sender.addChatMessage(new ChatComponentText("Current value for " + OPTION_COLORBLIND + ": " + Reference.COLOR_BLIND_MODE));
                    }
                    return;
                case OPTION_FANCY:
                    if (args.length > 1) {
                        boolean newValue = Boolean.parseBoolean(args[1]);
                        Reference.USE_FANCY_RENDERING = newValue;
                    } else {
                        sender.addChatMessage(new ChatComponentText("Current value for " + OPTION_FANCY + ": " + Reference.USE_FANCY_RENDERING));
                    }
                    return;
                case OPTION_PLANE_COUNT:
                    if (args.length > 1) {
                        try {
                            int newValue = Integer.parseInt(args[1]);
                            if (newValue >= 1 && newValue <= 50) {
                                Reference.NUMBER_OF_PARTICLE_PLANES = newValue;
                                return;
                            }
                        } catch (NumberFormatException nfe) {/* do nothing */ }
                        sender.addChatMessage(new ChatComponentText("Valid values for " + OPTION_PLANE_COUNT + " range from 1 to 50"));
                    }
                    sender.addChatMessage(new ChatComponentText("Current value for " + OPTION_PLANE_COUNT + ": " + Reference.NUMBER_OF_PARTICLE_PLANES));
                    return;
            }
        }
        sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return (sender instanceof EntityPlayer);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length > 1)
            return null;
        List<String> result = new ArrayList<>(ALL_OPTIONS.size());
        for (String option : ALL_OPTIONS) {
            if (option.toLowerCase().startsWith(args[0].toLowerCase()))
                result.add(option);
        }
        return result;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

}
