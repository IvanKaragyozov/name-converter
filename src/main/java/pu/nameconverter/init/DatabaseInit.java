package pu.nameconverter.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pu.nameconverter.service.CyrillicToLatinService;

import java.util.Scanner;

@Component
public class DatabaseInit implements CommandLineRunner {

    private final CyrillicToLatinService cyrillicToLatinService;

    public DatabaseInit(CyrillicToLatinService cyrillicToLatinService) {
        this.cyrillicToLatinService = cyrillicToLatinService;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter a cyrillic text (or 'exit' to quit): ");
            String cyrillicName = scanner.nextLine();

            if (cyrillicName.equalsIgnoreCase("exit")) {
                break;
            }

            String latinText = this.cyrillicToLatinService.convertCyrillicNamesInText(cyrillicName);
            System.out.printf("The latin equivalent is is: %s%n", latinText);
        }

        scanner.close();
    }
}
