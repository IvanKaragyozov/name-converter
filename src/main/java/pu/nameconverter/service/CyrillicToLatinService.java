package pu.nameconverter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pu.nameconverter.domain.entity.CyrillicToLatin;
import pu.nameconverter.repository.CyrillicToLatinRepository;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pu.nameconverter.domain.enums.Letters.CYRILLIC_TO_LATIN_MAP;

@Service
public class CyrillicToLatinService {

    private final CyrillicToLatinRepository cyrillicToLatinRepository;

    @Autowired
    public CyrillicToLatinService(CyrillicToLatinRepository cyrillicToLatinRepository) {
        this.cyrillicToLatinRepository = cyrillicToLatinRepository;
    }

    public String convertCyrillicNamesInText(String cyrillicText) {
        StringBuilder latinTextBuilder = new StringBuilder();
        StringBuilder cyrillicNameBuilder = new StringBuilder();

        for (char c : cyrillicText.toCharArray()) {

            if (Character.isLetter(c)) {
                cyrillicNameBuilder.append(c);
            } else {

                String cyrillicName = cyrillicNameBuilder.toString();
                if (!cyrillicName.isEmpty()) {
                    String latinName = convertCyrillicToLatin(cyrillicName);

                    Optional<CyrillicToLatin> optionalEntity = findByCyrillicName(cyrillicName);
                    if (optionalEntity.isPresent()) {

                        latinName = optionalEntity.get().getLatinName();

                    } else if (isName(cyrillicName)) {
                        CyrillicToLatin entity = new CyrillicToLatin();

                        entity.setCyrillicName(cyrillicName);
                        entity.setLatinName(latinName);

                        this.cyrillicToLatinRepository.save(entity);
                    }

                    latinTextBuilder.append(latinName);
                    cyrillicNameBuilder.setLength(0);
                }

                latinTextBuilder.append(c);
            }
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
