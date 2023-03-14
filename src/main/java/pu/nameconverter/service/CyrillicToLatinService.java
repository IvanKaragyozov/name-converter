package pu.nameconverter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pu.nameconverter.domain.entity.CyrillicToLatin;
import pu.nameconverter.repository.CyrillicToLatinRepository;

import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

@Service
public class CyrillicToLatinService {

    private final CyrillicToLatinRepository cyrillicToLatinRepository;

    @Autowired
    public CyrillicToLatinService(CyrillicToLatinRepository cyrillicToLatinRepository) {
        this.cyrillicToLatinRepository = cyrillicToLatinRepository;
    }

    private static final Map<Character, String> CYRILLIC_TO_LATIN_MAP = Map.ofEntries(
            entry('а', "a"), entry('б', "b"), entry('в', "v"), entry('г', "g"), entry('д', "d"), entry('е', "e"),
            entry('ж', "zh"), entry('з', "z"), entry('и', "i"), entry('й', "y"), entry('к', "k"), entry('л', "l"),
            entry('м', "m"), entry('н', "n"), entry('о', "o"), entry('п', "p"), entry('р', "r"), entry('с', "s"),
            entry('т', "t"), entry('у', "u"), entry('ф', "f"), entry('х', "h"), entry('ц', "ts"), entry('ч', "ch"),
            entry('ш', "sh"), entry('щ', "sch"), entry('ъ', "а"), entry('ь', ""), entry('ю', "yu"), entry('я', "ya")
    );

    public String convertCyrillicNamesInText(String cyrillicText) {
        StringBuilder latinTextBuilder = new StringBuilder();
        StringBuilder cyrillicNameBuilder = new StringBuilder();
        boolean inName = false;

        for (char c : cyrillicText.toCharArray()) {

            if (Character.isLetter(c)) {
                if (!inName) {
                    inName = true;
                    cyrillicNameBuilder.setLength(0);
                }
                cyrillicNameBuilder.append(c);
            } else {
                if (inName) {
                    inName = false;
                    String cyrillicName = cyrillicNameBuilder.toString();
                    String latinName = convertCyrillicToLatin(cyrillicName);

                    Optional<CyrillicToLatin> optionalEntity = findByCyrillicName(cyrillicName);
                    if (optionalEntity.isPresent()) {
                        latinName = optionalEntity.get().getLatinName();
                    } else {
                        if (isName(cyrillicName)) {
                            CyrillicToLatin entity = new CyrillicToLatin();
                            entity.setCyrillicName(cyrillicName);
                            entity.setLatinName(latinName);
                            cyrillicToLatinRepository.save(entity);
                        }
                    }

                    latinTextBuilder.append(latinName).append(c);
                } else {
                    latinTextBuilder.append(c);
                }
            }
        }
        if (inName) {
            String cyrillicName = cyrillicNameBuilder.toString();
            String latinName = convertCyrillicToLatin(cyrillicName);

            Optional<CyrillicToLatin> optionalEntity = findByCyrillicName(cyrillicName);
            if (optionalEntity.isPresent()) {
                latinName = optionalEntity.get().getLatinName();
            } else {
                if (isName(cyrillicName)) {
                    CyrillicToLatin entity = new CyrillicToLatin();
                    entity.setCyrillicName(cyrillicName);
                    entity.setLatinName(latinName);
                    cyrillicToLatinRepository.save(entity);
                }
            }

            latinTextBuilder.append(latinName);
        }
        return latinTextBuilder.toString();
    }

    private boolean isName(String text) {
        // regex for a name starting with capital letter and containing only lowercase letters
        Pattern pattern = Pattern.compile("^\\p{Lu}[\\p{Ll} ]*$");
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    private String convertCyrillicToLatin(String cyrillicWord) {
        StringBuilder latinWord = new StringBuilder();

        for (char c : cyrillicWord.toCharArray()) {

            String latinChar = CYRILLIC_TO_LATIN_MAP.get(Character.toLowerCase(c));

            if (latinChar != null) {
                latinWord.append(Character.isUpperCase(c)
                        ? latinChar.substring(0, 1).toUpperCase() + latinChar.substring(1)
                        : latinChar);

            } else {
                latinWord.append(c);
            }
        }

        return latinWord.toString();
    }

    private Optional<CyrillicToLatin> findByCyrillicName(String name){
        return this.cyrillicToLatinRepository
                .findByCyrillicName(name);
    }

}
