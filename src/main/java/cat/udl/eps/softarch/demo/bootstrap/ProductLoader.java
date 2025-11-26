package cat.udl.eps.softarch.demo.bootstrap;

import cat.udl.eps.softarch.demo.domain.Product;
import cat.udl.eps.softarch.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ProductLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            loadData();
        }
    }

    private void loadData() {
        // 1. CAFÈ D'ESPECIALITAT (El producte estrella)
        Product cafeEtiopia = Product.builder()
                .name("Etiòpia Yirgacheffe (250g)")
                .description("Cafè en gra amb notes florals i cítriques. Torrat mitjà ideal per a filtre.")
                .price(new BigDecimal("14.50"))
                .stock(20)
                .isAvailable(true)
                .brand("Nomad Coffee")
                .barcode("8410000000010")
                .kcal(2)
                .ingredients(Set.of("100% Cafè Aràbica rentat"))
                .allergens(Set.of())
                .rating(4.8)
                .isPartOfLoyaltyProgram(true)
                .pointsGiven(15)
                .pointsCost(150)
                .build();

        // 2. BRIOIXERIA (Per testejar al·lèrgens i descripcions curtes)
        Product croissant = Product.builder()
                .name("Croissant de Mantega Artesà")
                .description("Fet cada matí al nostre obrador. Cruixent per fora, tendre per dins.")
                .price(new BigDecimal("2.20"))
                .stock(15)
                .isAvailable(true)
                .brand("Obrador Local")
                .barcode("8410000000020")
                .kcal(280)
                .carbs(30)
                .fats(16)
                .proteins(5)
                .ingredients(Set.of("Farina", "Mantega", "Sucre", "Llevat"))
                .allergens(Set.of("Gluten", "Làctics", "Ou")) // Molts al·lèrgens per testejar la UI
                .rating(4.5)
                .isPartOfLoyaltyProgram(true)
                .pointsGiven(2)
                .pointsCost(20)
                .build();

        // 3. BEGUDA FREDA (Per testejar estoc baix)
        Product coldBrew = Product.builder()
                .name("Cold Brew Organic")
                .description("Cafè infusionat en fred durant 24h. Intens i refrescant.")
                .price(new BigDecimal("4.50"))
                .stock(3) // ESTOC BAIX: Només en queden 3!
                .isAvailable(true)
                .brand("MyCoffee House")
                .barcode("8410000000030")
                .kcal(5)
                .ingredients(Set.of("Aigua filtrada", "Cafè"))
                .rating(4.2)
                .isPartOfLoyaltyProgram(true)
                .pointsGiven(5)
                .pointsCost(50)
                .build();

        // 4. PRODUCTE ESGOTAT (Per testejar el color vermell / botó deshabilitat)
        Product galetaXoco = Product.builder()
                .name("Cookie XXL Xocolata")
                .description("Galeta estil americà amb trossos de xocolata belga.")
                .price(new BigDecimal("2.50"))
                .stock(0) // ESGOTAT
                .isAvailable(true)
                .brand("Obrador Local")
                .barcode("8410000000040")
                .kcal(350)
                .allergens(Set.of("Gluten", "Fruits secs", "Soja"))
                .rating(4.9)
                .isPartOfLoyaltyProgram(false) // No dona punts
                .build();

        // 5. PRODUCTE VEGÀ / ALTERNATIU
        Product lletCivada = Product.builder()
                .name("Llet de Civada Barista")
                .description("L'alternativa vegetal perfecta per fer escuma al teu cappuccino.")
                .price(new BigDecimal("3.20"))
                .stock(50)
                .isAvailable(true)
                .brand("Oatly")
                .barcode("8410000000050")
                .kcal(60)
                .carbs(8)
                .ingredients(Set.of("Aigua", "Civada", "Oli de colza"))
                .allergens(Set.of("Gluten"))
                .rating(4.0)
                .isPartOfLoyaltyProgram(true)
                .pointsGiven(3)
                .pointsCost(30)
                .build();

        // 6. PRODUCTE CAR (Pack regal)
        Product packRegal = Product.builder()
                .name("Pack Degustació Barista")
                .description("Inclou 3 bosses de cafè d'origen (Kènia, Colòmbia, Brasil) i una tassa exclusiva.")
                .price(new BigDecimal("45.00"))
                .stock(10)
                .isAvailable(true)
                .brand("MyCoffee House")
                .barcode("8410000000060")
                .rating(5.0)
                .isPartOfLoyaltyProgram(true)
                .pointsGiven(50)
                .pointsCost(500)
                .build();

        // Guardem tots els productes
        productRepository.saveAll(Arrays.asList(cafeEtiopia, croissant, coldBrew, galetaXoco, lletCivada, packRegal));

        System.out.println("------------------------------------------------");
        System.out.println("☕ MENU CAFETERIA CARREGAT: 6 Productes deliciosos ☕");
        System.out.println("------------------------------------------------");
    }
}