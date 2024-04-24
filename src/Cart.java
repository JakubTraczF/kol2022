import java.util.HashMap;
import java.util.Map;

public class Cart {
    // Mapa przechowująca produkty w koszyku wraz z ich ilościami
    private Map<Product, Integer> products = new HashMap<>();

    // Metoda dodająca produkt do koszyka
    public void addProduct(Product product, int amount) {
        // Sprawdzenie, czy ilość produktów jest dodatnia
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be a positive integer");
        }
        // Aktualizacja ilości produktów w koszyku
        products.put(product, products.getOrDefault(product, 0) + amount);
    }

    // Metoda obliczająca wartość koszyka w danym roku i miesiącu
    public double getPrice(int year, int month) {
        // Zmienna przechowująca całkowitą wartość koszyka
        double totalPrice = 0.0;
        // Iteracja przez wszystkie produkty w koszyku
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            // Pobranie produktu oraz jego ilości z mapy
            Product product = entry.getKey();
            int amount = entry.getValue();
            // Dodanie ceny produktu pomnożonej przez jego ilość do całkowitej wartości koszyka
            totalPrice += product.getPrice(year, month) * amount;
        }
        // Zwrócenie całkowitej wartości koszyka
        return totalPrice;
    }

    // Metoda obliczająca inflację między dwoma wskazanymi miesiącami
    public double getInflation(int year1, int month1, int year2, int month2) {
        // Obliczenie wartości koszyka w pierwszym i drugim okresie
        double price1 = getPrice(year1, month1);
        double price2 = getPrice(year2, month2);
        // Obliczenie liczby miesięcy między okresami
        int months = (year2 - year1) * 12 + (month2 - month1);
        // Obliczenie inflacji na podstawie wzoru
        return ((price2 - price1) / price1) * 100 / months * 12;
    }
}
