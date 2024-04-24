import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Klasa reprezentująca produkt spożywczy.
 */
public class FoodProduct extends Product {
    // Tablica dwuwymiarowa przechowująca ceny produktu według województw
    private Double[][] pricesByProvince;

    /**
     * Prywatny konstruktor inicjalizujący nazwę produktu i tablicę cen według województw.
     * @param name nazwa produktu
     * @param pricesByProvince tablica cen według województw
     */
    private FoodProduct(String name, Double[][] pricesByProvince) {
        super(name);
        this.pricesByProvince = pricesByProvince;
    }

    /**
     * Metoda fabryczna fromCsv służąca do tworzenia obiektu FoodProduct na podstawie pliku CSV.
     * @param path ścieżka do pliku CSV
     * @return obiekt FoodProduct
     */
    public static FoodProduct fromCsv(Path path) {
        String name;
        Double[][] pricesByProvince;

        try {
            Scanner scanner = new Scanner(path);
            name = scanner.nextLine(); // Odczytanie nazwy produktu
            scanner.nextLine(); // Pominięcie drugiej linii z nagłówkiem tabeli

            // Odczytanie cen dla poszczególnych województw
            pricesByProvince = new Double[16][]; // 16 województw w Polsce
            int i = 0;
            while (scanner.hasNextLine()) {
                String[] prices = scanner.nextLine().split(";");
                pricesByProvince[i] = Arrays.stream(prices)
                        .map(value -> value.replace(",", ".")) // Zamiana przecinka na kropkę
                        .map(Double::valueOf) // Konwersja na Double
                        .toArray(Double[]::new);
                i++;
            }

            scanner.close();

            return new FoodProduct(name, pricesByProvince);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metoda nadpisująca getPrice z klasy Product, zwraca średnią cenę produktu dla podanego roku i miesiąca.
     * @param year rok
     * @param month miesiąc
     * @return średnia cena produktu
     */
    @Override
    public double getPrice(int year, int month) {
        if (year < 2010 || year > 2022 || month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Invalid year or month");
        }
        // Obliczenie średniej ceny z wszystkich województw
        double sum = 0;
        int count = 0;
        for (Double[] prices : pricesByProvince) {
            if (prices != null && prices.length >= month) {
                sum += prices[month - 1]; // Indeks miesiąca jest przesunięty o 1
                count++;
            }
        }
        if (count == 0) {
            throw new IndexOutOfBoundsException("No data available for the specified year and month");
        }
        return sum / count;
    }

    /**
     * Metoda getPrice z uwzględnieniem województwa, zwraca cenę produktu dla danego roku, miesiąca i województwa.
     * @param year rok
     * @param month miesiąc
     * @param province województwo
     * @return cena produktu
     */
    public double getPrice(int year, int month, String province) {
        if (year < 2010 || year > 2022 || month < 1 || month > 12) {
            throw new IndexOutOfBoundsException("Invalid year or month");
        }
        // Znalezienie indeksu województwa
        int provinceIndex = getProvinceIndex(province);
        if (provinceIndex == -1) {
            throw new IndexOutOfBoundsException("Province not found");
        }
        // Sprawdzenie czy dane dla danego województwa są dostępne w danym miesiącu
        if (pricesByProvince[provinceIndex] == null || pricesByProvince[provinceIndex].length < month) {
            throw new IndexOutOfBoundsException("No data available for the specified year, month, and province");
        }
        return pricesByProvince[provinceIndex][month - 1]; // Indeks miesiąca jest przesunięty o 1
    }

    /**
     * Metoda pomocnicza zwracająca indeks województwa na podstawie jego nazwy.
     * @param province nazwa województwa
     * @return indeks województwa lub -1, jeśli województwo nie zostało znalezione
     */
    private int getProvinceIndex(String province) {
        String[] provinces = {
                "DOLNOŚLĄSKIE", "KUJAWSKO-POMORSKIE", "LUBELSKIE", "LUBUSKIE", "ŁÓDZKIE",
                "MAŁOPOLSKIE", "MAZOWIECKIE", "OPOLSKIE", "PODKARPACKIE", "PODLASKIE",
                "POMORSKIE", "ŚLĄSKIE", "ŚWIĘTOKRZYSKIE", "WARMIŃSKO-MAZURSKIE", "WIELKOPOLSKIE", "ZACHODNIOPOMORSKIE"
        };
        for (int i = 0; i < provinces.length; i++) {
            if (provinces[i].equals(province)) {
                return i;
            }
        }
        return -1; // Jeśli województwo nie zostanie znalezione
    }
}
