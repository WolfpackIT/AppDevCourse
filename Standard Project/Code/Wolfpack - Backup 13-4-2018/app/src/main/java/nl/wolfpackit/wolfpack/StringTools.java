package nl.wolfpackit.wolfpack;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTools{
    public static String replace(String text, String regexString, RegexReplacer replacer) {
        Pattern regex = Pattern.compile(regexString);
        Matcher regexMatcher = regex.matcher(text);

        int offset = 0; //the offset when mapping a match to the text variable, as the replacements screw with these indices
        //go through matches
        while (regexMatcher.find()) {
            //get groups as array
            int groupCount = regexMatcher.groupCount()+1;
            String[] groups = new String[groupCount];
            for (int i = 0; i < groupCount; i++)
                groups[i] = regexMatcher.group(i);

            //get the replacement text
            String replacement = replacer.replace(groups);

            //replace the text
            text = text.substring(0, regexMatcher.start()+offset)+
                    replacement+
                    text.substring(regexMatcher.end()+offset, text.length());
            offset += replacement.length()-(regexMatcher.end()-regexMatcher.start());
        }

        return text;
    }
    public static String join(String delimiter, String[] items){
        String out = "";
        for(String item: items)
            out += delimiter+item;
        return out.substring(delimiter.length());
    }

    public interface RegexReplacer{
        String replace(String... groups);
    }
}
