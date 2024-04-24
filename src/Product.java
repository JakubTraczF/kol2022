import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;

/**
 * Abstrakcyjna klasa reprezentująca produkt.
 */
public abstract class Product {
    // Pole przechowujące nazwę produktu
    private String name;

    /**
     * Konstruktor inicjalizujący nazwę produktu.
     * @param name nazwa produktu
     */
    protected Product(String name) {
        this.name = name;
    }

    /**
     * Metoda zwracająca nazwę produktu.
     * @return nazwa produktu
     */
    public String getName() {
        return name;
    }

    /**
     * Abstrakcyjna metoda zwracająca cenę produktu dla danego roku i miesiąca.
     * @param year rok
     * @param month miesiąc
     * @return cena produktu
     */
    public abstract double getPrice(int year, int month);

    // Lista przechowująca produkty
    private static List<Product> products = new ArrayList<>();

    /**
     * Metoda czyszcząca listę produktów.
     */
    public static void clearProducts() {
        products.clear();
    }

    /**
     * Metoda dodająca produkty na podstawie danych z pliku CSV.
     * @param fromCsvFunction funkcja tworząca produkt z pliku CSV
     * @param path ścieżka do pliku lub katalogu zawierającego pliki CSV
     */
    public static void addProducts(Function<Path, Product> fromCsvFunction, Path path) {
        try {
            if (Files.isDirectory(path)) {
                // Jeśli podana ścieżka jest katalogiem, dodajemy produkty z każdego pliku CSV w katalogu
                Files.list(path)
                        .filter(p -> p.toString().endsWith(".csv"))
                        .forEach(p -> products.add(fromCsvFunction.apply(p)));
            } else {
                // Jeśli podana ścieżka wskazuje na pojedynczy plik, dodajemy produkty z tego pliku
                products.add(fromCsvFunction.apply(path));
            }
        } catch (IOException e) {
            // Obsługa błędów związanych z odczytem plików
            throw new RuntimeException("Error while reading files", e);
        }
    }

    /**
     * Metoda zwracająca tablicę produktów, których nazwy zaczynają się od danego prefiksu.
     * @param prefix prefiks nazwy produktu
     * @return tablica produktów
     * @throws AmbigiousProductException wyjątek rzucany, gdy nazwa jest wieloznaczna
     */
    public static Product[] getProducts(String prefix) throws AmbigiousProductException {
        List<Product> matchedProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().startsWith(prefix)) {
                matchedProducts.add(product);
            }
        }

        if (matchedProducts.isEmpty()) {
            // Jeśli nie znaleziono żadnego produktu dla danego prefiksu
            throw new IndexOutOfBoundsException("No products found for the given prefix");
        } else if (matchedProducts.size() == 1) {
            // Jeśli znaleziono dokładnie jeden produkt dla danego prefiksu
            return matchedProducts.toArray(new Product[0]);
        } else {
            // Jeśli znaleziono więcej niż jeden produkt dla danego prefiksu
            List<String> productNames = new ArrayList<>();
            for (Product product : matchedProducts) {
                productNames.add(product.getName());
            }
            throw new AmbigiousProductException(productNames);
        }
    }

    // Metoda fromCsv może być również przeniesiona, ale tutaj nie jest potrzebna
}
