package pu.nameconverter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pu.nameconverter.entity.CyrillicToLatin;
import pu.nameconverter.repository.CyrillicToLatinRepository;

import java.util.Map;
import java.util.Optional;

import static java.util.Map.entry;

@Service
public class CyrillicToLatinService {

    private final CyrillicToLatinRepository cyrillicToLatinRepository;

    @Autowired
    public CyrillicToLatinService(CyrillicToLatinRepository cyrillicToLatinRepository) {
        this.cyrillicToLatinRepository = cyrillicToLatinRepository;
    }

    public String convertCyrillicToLatin(String cyrillicName){

        if (cyrillicName == null || cyrillicName.isEmpty()) {
            throw new IllegalArgumentException("Cyrillic name cannot be null or empty.");
        }

        String latinName = cyrillicToLatin(cyrillicName);

        Optional<CyrillicToLatin> entity = findByCyrillicName(cyrillicName);

        if (entity.isPresent()) {
            throw new IllegalArgumentException("Name is already in the database.");
        }

        CyrillicToLatin cyrillicToLatin = findByCyrillicName(cyrillicName).orElse(new CyrillicToLatin());

        cyrillicToLatin.setLatinName(latinName);
        cyrillicToLatin.setCyrillicName(cyrillicName);
        this.cyrillicToLatinRepository.save(cyrillicToLatin);

        return latinName;
    }

    private static final Map<Character, String> CYRILLIC_TO_LATIN_MAP = Map.ofEntries(
            entry('а', "a"), entry('б', "b"), entry('в', "v"), entry('г', "g"), entry('д', "d"), entry('е', "e"),
            entry('ж', "zh"), entry('з', "z"), entry('и', "i"), entry('й', "y"), entry('к', "k"), entry('л', "l"),
            entry('м', "m"), entry('н', "n"), entry('о', "o"), entry('п', "p"), entry('р', "r"), entry('с', "s"),
            entry('т', "t"), entry('у', "u"), entry('ф', "f"), entry('х', "h"), entry('ц', "ts"), entry('ч', "ch"),
            entry('ш', "sh"), entry('щ', "sch"), entry('ъ', ""), entry('ь', ""), entry('ю', "yu"), entry('я', "ya")
    );

    private String cyrillicToLatin(String cyrillicWord) {
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
                //.orElse(new CyrillicToLatin());
    }

}
