import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        try {
            // Dodawanie produktów
            Product.clearProducts();
            Product.addProducts(NonFoodProduct::fromCsv, Paths.get("data/nonfood"));
            Product.addProducts(FoodProduct::fromCsv, Paths.get("data/food"));

            // Tworzenie koszyka
            Cart cart = new Cart();

            // Dodawanie produktów do koszyka
            Product[] allProducts = Product.getNonFoodProducts();
            for (Product product : allProducts) {
                cart.addProduct(product, 3); // Dodawanie każdego produktu w ilości 3 sztuk
            }

            allProducts = Product.getFoodProducts();
            for (Product product : allProducts) {
                cart.addProduct(product, 2); // Dodawanie każdego produktu w ilości 2 sztuk
            }

            // Obliczanie wartości koszyka dla kwietnia 2024
            double totalPrice = cart.getPrice(2024, 4);
            System.out.println("Total price in April 2024: " + totalPrice);

            // Obliczanie inflacji między kwietniem 2024 a lipcem 2024
            double inflation = cart.getInflation(2024, 4, 2024, 7);
            System.out.println("Inflation between April and July 2024: " + inflation + "%");

        } catch (AmbigiousProductException | IndexOutOfBoundsException e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
