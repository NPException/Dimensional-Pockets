package net.gtn.dimensionalpocket.oc.common.utils;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class Localise {

    public static final char FORMATTING_CHAR = '$';

    /**
     * This will translate the String given.
     * <p/>
     * It will also replace colour formatting in the string.
     * Following the simple pattern:
     * <p/>
     * $$ = $,
     * $0 = BLACK,
     * $1 = DARK_BLUE('1'),
     * $2 = DARK_GREEN('2'),
     * $3 = DARK_AQUA('3'),
     * $4 = DARK_RED('4'),
     * $5 = DARK_PURPLE('5'),
     * $6 = GOLD('6'),
     * $7 = GRAY('7'),
     * $8 = DARK_GRAY('8'),
     * $9 = BLUE('9'),
     * $a = GREEN('a'),
     * $b = AQUA('b'),
     * $c = RED('c'),
     * $d = LIGHT_PURPLE('d'),
     * $e = YELLOW('e'),
     * $f = WHITE('f'),
     * $k = OBFUSCATED('k', true),
     * $l = BOLD('l', true)
     * $m = STRIKE_THROUGH('m', true),
     * $n = UNDERLINE('n', true),
     * $o = ITALIC('o', true),
     * $r = RESET('r');
     * <p/>
     * As described by {@link net.minecraft.util.EnumChatFormatting}
     * If you want a back-slash by itself, you'll have to escape it with another '$'
     */
    public static String translate(String key) {
        return translate(key, true);
    }

    public static String translate(String key, boolean formatting) {
        String result = StatCollector.translateToLocal(key);
        if (formatting)
            for (EnumChatFormatting format : EnumChatFormatting.values())
                result = result.replace("" + FORMATTING_CHAR + format.getFormattingCode(), format.toString());
        return result;
    }

    public static String format(String key) {
        return StatCollector.translateToLocal(key);
    }

    public static String format(String key, Object... params) {
        String result = translate(key);
        if (params != null && params.length >= 0)
            for (Object param : params)
                result = result.replaceFirst("\\{\\}", String.valueOf(param));
        return result;
    }

    public static String safeTranslate(String key) {
        String translated = translate(key);
        return translated.equals(key) ? key : translated;
    }

    public static String safeTranslate(String key, String defaultString) {
        String translated = translate(key);
        return translated.equals(key) ? defaultString : translated;
    }

    public static List<String> wrapToSize(String text, int size) {
        return wrapToSize(text, size, false);
    }

    public static List<String> wrapToSize(String text, int size, boolean ignoreNewlines) {
        List<String> lines = new ArrayList<>();
        StringBuilder sb;
        String[] words;
        String space;

        if (ignoreNewlines) {
            text = text.replaceAll(System.lineSeparator(), " ");
            text = text.replaceAll("\\\\n", " ");

            words = text.split(" ");
            sb = new StringBuilder();
            space = "";
            for (String word : words) {
                if (sb.length() + space.length() + word.length() > size) {
                    lines.add(sb.toString());
                    sb = new StringBuilder();
                    space = "";
                }
                sb.append(space).append(word);
                space = " ";
            }
            lines.add(sb.toString());
        } else {
            for (String part : text.split("\\\\n")) {
                if (part.length() <= size) {
                    lines.add(part);
                } else {
                    words = part.split(" ");
                    sb = new StringBuilder();
                    space = "";
                    for (String word : words) {
                        if (sb.length() + space.length() + word.length() > size) {
                            lines.add(sb.toString());
                            sb = new StringBuilder();
                            space = "";
                        }
                        sb.append(space).append(word);
                        space = " ";
                    }
                    lines.add(sb.toString());
                }
            }
        }
        return lines;
    }
    
}
