package net.origamiking.mcmods.mod_manager.utils;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Markdown {

    private String text;
    private final Pattern boldPattern = Pattern.compile("\\*\\*(.*?)\\*\\*");
    private final Pattern linkPattern = Pattern.compile("\\[(.*?)]\\((.*?)\\)");
    private final Pattern imagePattern = Pattern.compile("!\\[(.*?)]\\((.*?)\\)");

    public Markdown(String text) {
        this.text = text;
    }

    public List<MutableText> toText() {
        text = text.replace("\u00A0", " ");
        text = text.replace("\r", "");
        text = text.replace("<br>", "\n").replace("<br/>", "\n");
        String[] lines = text.split("\n");
        List<MutableText> texts = new ArrayList<>();
        for (String line : lines) {
            if (imagePattern.matcher(line).find()) {
                continue;
            }
            texts.add(processLine(line));
        }
        return texts;
    }

    private MutableText processLine(String text) {
        if (boldPattern.matcher(text).find()) {
            return extractBoldText(text);
        }
        if (linkPattern.matcher(text).find()) {
            return extractLinkText(text);
        }
        return Text.literal(text);
    }

    private MutableText extractLinkText(String text) {
        Matcher matcher = linkPattern.matcher(text);
        if (!matcher.find()) {
            return Text.literal(text);
        }
        String linkText = matcher.group(1);
        int begin = text.indexOf(linkText);
        MutableText preText = Text.literal(text.substring(0, begin).replace("\\[", ""));
        MutableText matchedText = Text.literal(linkText).formatted(Formatting.UNDERLINE, Formatting.BLUE);
        matchedText.setStyle(matchedText.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, matcher.group(2))));
        return preText.append(matchedText).append(extractLinkText(text.substring(begin + 3 + linkText.length() + matcher.group(2).length())));
    }

    private MutableText extractBoldText(String text) {
        Matcher matcher = boldPattern.matcher(text);
        if (!matcher.find()) {
            return Text.literal(text);
        }
        String boldText = matcher.group(1);
        int begin = text.indexOf(boldText);
        MutableText preText = Text.literal(text.substring(0, begin).replace("\\*\\*", ""));
        MutableText matchedText = Text.literal(boldText).formatted(Formatting.BOLD);
        return preText.append(matchedText).append(extractBoldText(text.substring(begin + 2 + boldText.length())));
    }
}