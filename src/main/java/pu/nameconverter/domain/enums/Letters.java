package pu.nameconverter.domain.enums;

import java.util.Map;

import static java.util.Map.entry;

public enum Letters {
    ;

    public static final Map<Character, String> CYRILLIC_TO_LATIN_MAP = Map.ofEntries(
            entry('а', "a"), entry('б', "b"), entry('в', "v"), entry('г', "g"), entry('д', "d"), entry('е', "e"),
            entry('ж', "zh"), entry('з', "z"), entry('и', "i"), entry('й', "y"), entry('к', "k"), entry('л', "l"),
            entry('м', "m"), entry('н', "n"), entry('о', "o"), entry('п', "p"), entry('р', "r"), entry('с', "s"),
            entry('т', "t"), entry('у', "u"), entry('ф', "f"), entry('х', "h"), entry('ц', "ts"), entry('ч', "ch"),
            entry('ш', "sh"), entry('щ', "sch"), entry('ъ', "а"), entry('ь', ""), entry('ю', "yu"), entry('я', "ya")
    );
}
