package net.gtn.dimensionalpocket.oc.common.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public abstract class CommandAbstract extends CommandBase {

    private String commandName = "";
    private String commandUsage = "";

    public CommandAbstract(String commandName, String commandUsage) {
        this.commandName = commandName.toLowerCase();
        this.commandUsage = commandUsage;
    }

    public CommandAbstract register() {
        CommandHandler ch = (CommandHandler) MinecraftServer.getServer().getCommandManager();
        ch.registerCommand(this);
        return this;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + commandName + " " + commandUsage;
    }

    public void sendCommandUsage(ICommandSender commandSender) {
        sendChatMessage(commandSender, getCommandUsage(commandSender));
    }

    public void sendChatMessage(ICommandSender commandSender, String string) {
        commandSender.addChatMessage(new ChatComponentText(string));
    }

    /**
     * Oxford/Serial comma.
     * joinNiceString("Science", "Art", "Religion") returns "Science, Art, and Religion"
     */
    public static String joinNiceString(Object[] objects) {
        StringBuilder stringbuilder = new StringBuilder();

        for (int i = 0; i < objects.length; ++i) {
            if (i > 0) {
                stringbuilder.append(", ");
                if (i == objects.length - 1) {
						stringbuilder.append("and ");
					}
            }

            stringbuilder.append(objects[i].toString());
        }

        return stringbuilder.toString();
    }

    /**
     * Oxford/Serial comma.
     * joinNiceString("Science", "Art", "Religion") returns "Science, Art, and Religion"
     */
    public static IChatComponent joinNiceString(IChatComponent[] components) {
        ChatComponentText chatcomponenttext = new ChatComponentText("");

        for (int i = 0; i < components.length; ++i) {
            if (i > 0) {
                chatcomponenttext.appendText(", ");
                if (i == components.length - 1) {
						chatcomponenttext.appendText("and ");
					}
            }

            chatcomponenttext.appendSibling(components[i]);
        }

        return chatcomponenttext;
    }

    /**
     * Used to parse "~" at the start of an argument, so that the position can be set relative to the current one.
     * This can also depend on the value that follows directly afterward.
     *
     * @param sender   The sender in question, whether a player or console.
     * @param position The value that will be taken and adjusted accordingly, should it need adjusting.
     * @param argument The string that contains the argument you wish to parse.
     * @return The value given any necessary shifts.
     */
    public static double parseRelativePosition(ICommandSender sender, double position, String argument) {
        return parseRelativePosition(sender, position, argument, -30000000, 30000000);
    }

    /**
     * Used to parse "~" at the start of an argument, so that the position can be set relative to the current one.
     * This can also depend on the value that follows directly afterward.
     *
     * @param sender     The sender in question, whether a player or console.
     * @param position   The value that will be taken and adjusted accordingly, should it need adjusting.
     * @param argument   The string that contains the argument you wish to parse.
     * @param lowerBound If the adjusted number is beyond this, throws a {@link net.minecraft.command.NumberInvalidException}
     * @param upperBound If the adjusted number is beyond this, throws a {@link net.minecraft.command.NumberInvalidException}
     * @return The value given any necessary shifts.
     */
    public static double parseRelativePosition(ICommandSender sender, double position, String argument, int lowerBound, int upperBound) {
        return CommandBase.clamp_double(sender, position, argument, lowerBound, upperBound);
    }

    public static int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException numberformatexception) {
            throw new NumberInvalidException("commands.generic.num.invalid", number);
        }
    }

    public static boolean parseBoolean(String booleanValue) {
        if (!booleanValue.equals("true") && !booleanValue.equals("1")) {
            if (!booleanValue.equals("false") && !booleanValue.equals("0")) {
					throw new CommandException("commands.generic.boolean.invalid", booleanValue);
				}
            return false;
        }
        return true;
    }

}
