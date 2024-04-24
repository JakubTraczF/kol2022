import java.util.List;

/**
 * Klasa reprezentująca wyjątek rzucany, gdy nazwa produktu jest wieloznaczna.
 */
public class AmbigiousProductException extends RuntimeException {
    // Pole przechowujące listę nazw wieloznacznych produktów
    private List<String> ambiguousProducts;

    /**
     * Konstruktor inicjalizujący listę nazw wieloznacznych produktów.
     * @param ambiguousProducts lista nazw wieloznacznych produktów
     */
    public AmbigiousProductException(List<String> ambiguousProducts) {
        this.ambiguousProducts = ambiguousProducts;
    }

    /**
     * Przesłonięta metoda getMessage zwracająca komunikat błędu zawierający listę nazw wieloznacznych produktów.
     * @return komunikat błędu
     */
    @Override
    public String getMessage() {
        // Tworzenie początku komunikatu
        StringBuilder message = new StringBuilder("Ambiguous product name prefix. Ambiguous products: ");
        // Dodawanie nazw produktów do komunikatu
        for (String product : ambiguousProducts) {
            message.append(product).append(", ");
        }
        // Usuwanie ostatniego przecinka i spacji
        message.delete(message.length() - 2, message.length());
        // Zwracanie komunikatu jako tekst
        return message.toString();
    }
}
