import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Klasa reprezentująca produkt spoza kategorii spożywczych.
 */
public class NonFoodProduct {
    // Pola przechowujące nazwę produktu oraz tablicę cen
    String name;
    Double[] prices;

    /**
     * Prywatny konstruktor inicjalizujący nazwę i ceny produktu.
     * @param name nazwa produktu
     * @param prices tablica cen
     */
    private NonFoodProduct(String name, Double[] prices) {
        this.name = name;
        this.prices = prices;
    }

    /**
     * Metoda fabryczna fromCsv służąca do tworzenia obiektu NonFoodProduct na podstawie pliku CSV.
     * @param path ścieżka do pliku CSV
     * @return obiekt NonFoodProduct
     */
    public static NonFoodProduct fromCsv(Path path) {
        String name;
        Double[] prices;

        try {
            Scanner scanner = new Scanner(path);
            name = scanner.nextLine(); // Odczytanie pierwszej linii i zapisanie jej jako nazwa
            scanner.nextLine();  // Pominięcie drugiej linii z nagłówkiem tabeli

            // Odczytanie kolejnej linii z cenami i podzielenie jej na tablicę
            prices = Arrays.stream(scanner.nextLine().split(";"))
                    .map(value -> value.replace(",",".")) // Zamiana przecinka na kropkę
                    .map(Double::valueOf) // Konwersja na Double
                    .toArray(Double[]::new); // Utworzenie tablicy cen

            scanner.close();

            // Tworzymy obiekt NonFoodProduct używając prywatnego konstruktora klasy
            return new NonFoodProduct(name, prices);

        } catch (IOException e) {
            // Rzucenie wyjątku w przypadku błędu podczas odczytu pliku
            throw new RuntimeException(e);
        }
    }
}
