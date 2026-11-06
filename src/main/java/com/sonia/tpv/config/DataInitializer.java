package com.sonia.tpv.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sonia.tpv.model.Producto;
import com.sonia.tpv.model.Rol;
import com.sonia.tpv.model.Usuario;
import com.sonia.tpv.repository.ProductoHistorialRepository;
import com.sonia.tpv.repository.ProductoRepository;
import com.sonia.tpv.repository.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            ProductoHistorialRepository historialRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // --- Usuarios requeridos ---
            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario("admin", passwordEncoder.encode("admin123"), "Administrador", Rol.ADMIN);
                usuarioRepository.save(admin);
            }

            if (usuarioRepository.findByUsername("cajero").isEmpty()) {
                Usuario cajero = new Usuario("cajero", passwordEncoder.encode("cajero123"), "Cajero", Rol.CAJERO);
                usuarioRepository.save(cajero);
            }

            // --- CARTA OFICIAL RESTAURANTE CHINATOWN ---
            // Borra todo lo anterior que no pertenezca a la carta oficial y carga únicamente los 103 platos
            boolean tieneDatosAntiguos = productoRepository.findByNombre("Coca-Cola").isPresent()
                    || productoRepository.findByNombre("Rollito Primavera").isPresent()
                    || productoRepository.findByNombre("Agua 50cl").isPresent();

            if (tieneDatosAntiguos || productoRepository.count() == 0) {
                historialRepository.deleteAll();
                productoRepository.deleteAll();

                List<Producto> cartaOficial = new ArrayList<>();
                int orden = 1;

                // === 1. ENTRADA ===
                cartaOficial.add(new Producto("1. Ensalada china", new BigDecimal("4.95"), "Entrada", orden++));
                cartaOficial.add(new Producto("2. Ensalada de repollo", new BigDecimal("4.95"), "Entrada", orden++));
                cartaOficial.add(new Producto("3. Rollo de primavera", new BigDecimal("2.35"), "Entrada", orden++));
                cartaOficial.add(new Producto("3a. Ensalada china con rollo de primavera", new BigDecimal("5.15"), "Entrada", orden++));
                cartaOficial.add(new Producto("3c. Rollitos vietnamita", new BigDecimal("4.95"), "Entrada", orden++));
                cartaOficial.add(new Producto("4. Wan-tun frito", new BigDecimal("4.95"), "Entrada", orden++));
                cartaOficial.add(new Producto("5. Ensalada de gambas", new BigDecimal("5.70"), "Entrada", orden++));
                cartaOficial.add(new Producto("6. Ensalada de pollo, piña, tomate y manzana", new BigDecimal("6.25"), "Entrada", orden++));
                cartaOficial.add(new Producto("7. Pan de gambas", new BigDecimal("3.60"), "Entrada", orden++));
                cartaOficial.add(new Producto("8. Cóctel de gambas", new BigDecimal("6.55"), "Entrada", orden++));
                cartaOficial.add(new Producto("9. Pan chino", new BigDecimal("2.35"), "Entrada", orden++));
                cartaOficial.add(new Producto("1c. Ensalada de algas wakame", new BigDecimal("4.90"), "Entrada", orden++));
                cartaOficial.add(new Producto("2d. Shien ló Pao", new BigDecimal("4.45"), "Entrada", orden++));
                cartaOficial.add(new Producto("3d. Shia Chiao", new BigDecimal("5.45"), "Entrada", orden++));
                cartaOficial.add(new Producto("4d. Sau Mai", new BigDecimal("5.00"), "Entrada", orden++));
                cartaOficial.add(new Producto("5d. Kuotie", new BigDecimal("4.75"), "Entrada", orden++));
                cartaOficial.add(new Producto("6d. Siau long pao", new BigDecimal("4.85"), "Entrada", orden++));
                cartaOficial.add(new Producto("7d. Zil chil chaol", new BigDecimal("5.55"), "Entrada", orden++));
                cartaOficial.add(new Producto("8c. Gyozas de pollo a la plancha", new BigDecimal("5.90"), "Entrada", orden++));
                cartaOficial.add(new Producto("9c. Empanadillas vegetales Shuijing", new BigDecimal("4.70"), "Entrada", orden++));
                cartaOficial.add(new Producto("10c. Albóndigas de sepia y gambas", new BigDecimal("6.55"), "Entrada", orden++));
                cartaOficial.add(new Producto("11c. Samosa", new BigDecimal("3.95"), "Entrada", orden++));
                cartaOficial.add(new Producto("12c. Saumai de marisco", new BigDecimal("5.95"), "Entrada", orden++));
                cartaOficial.add(new Producto("13c. Combinado 1: Sau Mai", new BigDecimal("5.95"), "Entrada", orden++));
                cartaOficial.add(new Producto("14c. Combinado 2: Vapor", new BigDecimal("9.70"), "Entrada", orden++));
                cartaOficial.add(new Producto("15c. Combinado 3: Fritos", new BigDecimal("5.95"), "Entrada", orden++));

                // === 2. SOPA ===
                cartaOficial.add(new Producto("10. Sopa de la casa", new BigDecimal("5.10"), "Sopa", orden++));
                cartaOficial.add(new Producto("11. Sopa de aletas de tiburón", new BigDecimal("4.95"), "Sopa", orden++));
                cartaOficial.add(new Producto("12. Sopa agria y picante", new BigDecimal("4.95"), "Sopa", orden++));
                cartaOficial.add(new Producto("13. Sopa de fideos con zha chai (picante)", new BigDecimal("4.70"), "Sopa", orden++));
                cartaOficial.add(new Producto("14. Sopa de Wan-tun", new BigDecimal("5.30"), "Sopa", orden++));
                cartaOficial.add(new Producto("15. Sopa de pollo con champiñones", new BigDecimal("5.00"), "Sopa", orden++));
                cartaOficial.add(new Producto("16. Sopa de nido de golondrinas", new BigDecimal("5.00"), "Sopa", orden++));
                cartaOficial.add(new Producto("17. Sopa de maíz con carne de cangrejo", new BigDecimal("5.15"), "Sopa", orden++));
                cartaOficial.add(new Producto("18. Sopa de marisco", new BigDecimal("5.85"), "Sopa", orden++));

                // === 3. ARROZ & TALLARINES ===
                cartaOficial.add(new Producto("20. Arroz blanco", new BigDecimal("3.25"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("21. Arroz frito con tres delicias", new BigDecimal("6.00"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("21b. Arroz frito con pollo y curry", new BigDecimal("6.35"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("22. Arroz frito con gambas", new BigDecimal("7.15"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("23. Arroz frito al estilo yangchong", new BigDecimal("7.65"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("23b. Arroz frito con salsa miso", new BigDecimal("7.65"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("23c. Arroz frito con salsa tailandesa", new BigDecimal("7.90"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("25. Tallarines frito con tres delicias", new BigDecimal("6.95"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("26. Fideos de arroz chino", new BigDecimal("7.60"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("27. Tallarines de Pekín", new BigDecimal("8.40"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("27b. Pasta de arroz", new BigDecimal("8.10"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("28. Fideos de arroz chinos con gambas", new BigDecimal("8.60"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("29. Fideos de sojas", new BigDecimal("7.60"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("30. Tallarines con gambas", new BigDecimal("8.00"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("30b. Tallarines chino a la plancha", new BigDecimal("8.00"), "Arroz & Tallarines", orden++));
                cartaOficial.add(new Producto("30c. Tallarines udon con tres delicias", new BigDecimal("8.20"), "Arroz & Tallarines", orden++));

                // === 4. TORTILLA & VERDURAS ===
                cartaOficial.add(new Producto("31. Tortilla con gambas", new BigDecimal("6.75"), "Tortilla & Verduras", orden++));
                cartaOficial.add(new Producto("32. Tortilla con jamón", new BigDecimal("6.35"), "Tortilla & Verduras", orden++));
                cartaOficial.add(new Producto("33. Verdura china con setas", new BigDecimal("7.05"), "Tortilla & Verduras", orden++));
                cartaOficial.add(new Producto("34. Verduras variadas salteadas", new BigDecimal("6.55"), "Tortilla & Verduras", orden++));
                cartaOficial.add(new Producto("35b. Bambú y setas chinas salteadas", new BigDecimal("7.25"), "Tortilla & Verduras", orden++));
                cartaOficial.add(new Producto("38. Patatas fritas", new BigDecimal("4.75"), "Tortilla & Verduras", orden++));

                // === 5. PESCADO & GAMBAS ===
                cartaOficial.add(new Producto("39. Pescado frito con patatas o ensalada", new BigDecimal("7.65"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("40. Pescado frito con cebolleta y jengibre", new BigDecimal("7.70"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("41. Pescado agridulce", new BigDecimal("7.05"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("42. Gambas con curry", new BigDecimal("9.85"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("43. Langostinos fritos", new BigDecimal("9.85"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("44. Gambas agridulce", new BigDecimal("9.75"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("45. Gambas en salsa a la plancha", new BigDecimal("10.90"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("45c. Gambas con sal y pimienta", new BigDecimal("10.60"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("46. Verduras con gambas", new BigDecimal("10.40"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("47. Gambas con bambú y setas", new BigDecimal("10.95"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("49. Gambas con salsa picante", new BigDecimal("10.50"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("50. Gambas con salsa de ostras", new BigDecimal("10.40"), "Pescado & Gambas", orden++));
                cartaOficial.add(new Producto("50c. Gambas con salsa tailandesa", new BigDecimal("10.75"), "Pescado & Gambas", orden++));

                // === 6. TERNERA & CERDO ===
                cartaOficial.add(new Producto("51. Ternera con salsa de curry", new BigDecimal("9.50"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("51c. Ternera con salsa tailandesa", new BigDecimal("10.50"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("52. Ternera con salsa de ostras", new BigDecimal("9.60"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("53. Ternera con pimiento verde", new BigDecimal("9.60"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("54. Ternera con cebolla", new BigDecimal("9.65"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("55. Ternera con champiñones", new BigDecimal("9.65"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("56. Ternera salteada picante", new BigDecimal("9.70"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("56b. Ternera con salsa miso", new BigDecimal("9.85"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("57. Verduras con ternera", new BigDecimal("9.60"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("58. Ternera con bambú y setas chinas", new BigDecimal("9.85"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("59. Ternera con patatas fritas", new BigDecimal("9.60"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("60. Ternera con salsa a la plancha", new BigDecimal("10.75"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("61. Cerdo agridulce", new BigDecimal("7.55"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("62. Costillas asadas", new BigDecimal("10.80"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("64. Costillas agridulces", new BigDecimal("9.35"), "Ternera & Cerdo", orden++));
                cartaOficial.add(new Producto("65. Cerdo con bambú y setas chinas", new BigDecimal("8.25"), "Ternera & Cerdo", orden++));

                // === 7. POLLO ===
                cartaOficial.add(new Producto("71. Pollo con salsa de curry", new BigDecimal("7.95"), "Pollo", orden++));
                cartaOficial.add(new Producto("71c. Pollo con salsa tailandesa", new BigDecimal("8.85"), "Pollo", orden++));
                cartaOficial.add(new Producto("72. Pollo con salsa picante", new BigDecimal("8.00"), "Pollo", orden++));
                cartaOficial.add(new Producto("72b. Pollo con salsa miso", new BigDecimal("8.50"), "Pollo", orden++));
                cartaOficial.add(new Producto("73. Pollo con almendras", new BigDecimal("8.35"), "Pollo", orden++));
                cartaOficial.add(new Producto("74. Verduras con pollo", new BigDecimal("7.55"), "Pollo", orden++));
                cartaOficial.add(new Producto("76. Pollo con piña (agridulce)", new BigDecimal("7.95"), "Pollo", orden++));
                cartaOficial.add(new Producto("77. Pollo agridulce", new BigDecimal("7.95"), "Pollo", orden++));
                cartaOficial.add(new Producto("78. Pollo con champiñones", new BigDecimal("7.95"), "Pollo", orden++));
                cartaOficial.add(new Producto("80. Pollo con bambú y setas chinas", new BigDecimal("8.55"), "Pollo", orden++));
                cartaOficial.add(new Producto("81. Pollo con limón", new BigDecimal("8.20"), "Pollo", orden++));
                cartaOficial.add(new Producto("82. Bolitas de pollo", new BigDecimal("6.95"), "Pollo", orden++));
                cartaOficial.add(new Producto("83. Pechuga de pollo rebozado", new BigDecimal("7.95"), "Pollo", orden++));
                cartaOficial.add(new Producto("84. Pollo con salsa a la plancha", new BigDecimal("10.30"), "Pollo", orden++));

                // === 8. MARISCOS & PATOS ===
                cartaOficial.add(new Producto("86. Mariscos variados salteados", new BigDecimal("10.30"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("87. Mariscos especiales a la plancha", new BigDecimal("12.85"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("89. Cangrejo especial a la plancha", new BigDecimal("13.75"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("90. Sepia con pimientos y setas", new BigDecimal("10.10"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("91. Sepia especial a la plancha", new BigDecimal("10.95"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("93. Pato asado a la pekinesa con crepes", new BigDecimal("12.00"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("94. Pato con naranja", new BigDecimal("10.95"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("95. Pato a la plancha", new BigDecimal("11.95"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("96. Pato con piña", new BigDecimal("10.95"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("97. Pato con limón", new BigDecimal("10.95"), "Mariscos & Patos", orden++));
                cartaOficial.add(new Producto("98. Pato con bambú y setas", new BigDecimal("11.35"), "Mariscos & Patos", orden++));

                // === 9. PLATOS ESPECIALES ===
                cartaOficial.add(new Producto("99. Ku-Bak tres delicias", new BigDecimal("8.75"), "Platos Especiales", orden++));
                cartaOficial.add(new Producto("100. Ku-Bak con gambas", new BigDecimal("9.35"), "Platos Especiales", orden++));
                cartaOficial.add(new Producto("101. Familia feliz a la plancha", new BigDecimal("10.25"), "Platos Especiales", orden++));
                cartaOficial.add(new Producto("102. Entremeses variados", new BigDecimal("9.00"), "Platos Especiales", orden++));
                cartaOficial.add(new Producto("103. Ancas de ranas a la plancha", new BigDecimal("9.70"), "Platos Especiales", orden++));
                cartaOficial.add(new Producto("103c. Ancas de ranas con sal y pimienta", new BigDecimal("10.40"), "Platos Especiales", orden++));
                cartaOficial.add(new Producto("105. Ternera \"China Town\"", new BigDecimal("8.75"), "Platos Especiales", orden++));
                cartaOficial.add(new Producto("106. Hormigas subiendo al árbol Ku-Bak", new BigDecimal("8.70"), "Platos Especiales", orden++));
                cartaOficial.add(new Producto("P1. Bistec a la plancha", new BigDecimal("9.35"), "Platos Especiales", orden++));
                cartaOficial.add(new Producto("P2. Filete de pollo a la plancha", new BigDecimal("7.70"), "Platos Especiales", orden++));

                // === 10. MENÚS ESPECIALES ===
                cartaOficial.add(new Producto("Menú 2 personas Tipo A", new BigDecimal("29.90"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 2 personas Tipo B", new BigDecimal("34.90"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 3 personas Tipo A", new BigDecimal("44.85"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 3 personas Tipo B", new BigDecimal("52.35"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 4 personas Tipo A", new BigDecimal("59.80"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 4 personas Tipo B", new BigDecimal("69.80"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 5 personas Tipo A", new BigDecimal("74.75"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 5 personas Tipo B", new BigDecimal("87.25"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 6 personas Tipo B", new BigDecimal("104.70"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 7 personas Tipo A", new BigDecimal("104.65"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 8 personas Tipo B", new BigDecimal("139.60"), "Menús Especiales", orden++));
                cartaOficial.add(new Producto("Menú 10 personas Tipo B", new BigDecimal("174.50"), "Menús Especiales", orden++));

                productoRepository.saveAll(cartaOficial);
            }

            // --- MENÚS ESPECIALES: se añaden si aún no existen (sin borrar nada) ---
            if (productoRepository.findByNombre("Menú 2 personas Tipo A").isEmpty()) {
                long maxOrden = productoRepository.findAll().stream()
                        .mapToLong(p -> p.getOrden() != null ? p.getOrden() : 0)
                        .max().orElse(0);
                int ordenMenu = (int) maxOrden + 1;
                List<Producto> menus = new ArrayList<>();
                menus.add(new Producto("Menú 2 personas Tipo A",  new BigDecimal("29.90"),  "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 2 personas Tipo B",  new BigDecimal("34.90"),  "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 3 personas Tipo A",  new BigDecimal("44.85"),  "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 3 personas Tipo B",  new BigDecimal("52.35"),  "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 4 personas Tipo A",  new BigDecimal("59.80"),  "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 4 personas Tipo B",  new BigDecimal("69.80"),  "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 5 personas Tipo A",  new BigDecimal("74.75"),  "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 5 personas Tipo B",  new BigDecimal("87.25"),  "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 6 personas Tipo B",  new BigDecimal("104.70"), "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 7 personas Tipo A",  new BigDecimal("104.65"), "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 8 personas Tipo B",  new BigDecimal("139.60"), "Menús Especiales", ordenMenu++));
                menus.add(new Producto("Menú 10 personas Tipo B", new BigDecimal("174.50"), "Menús Especiales", ordenMenu++));
                productoRepository.saveAll(menus);
            }

            // --- MENÚS DEL DÍA: se añaden si aún no existen (sin borrar nada) ---
            if (productoRepository.findByNombre("Menú del Día Estándar").isEmpty()) {
                long maxOrden2 = productoRepository.findAll().stream()
                        .mapToLong(p -> p.getOrden() != null ? p.getOrden() : 0)
                        .max().orElse(0);
                int ordenDia = (int) maxOrden2 + 1;
                List<Producto> menusDia = new ArrayList<>();
                menusDia.add(new Producto("Menú del Día Estándar", new BigDecimal("13.45"), "Menú del Día", ordenDia++));
                menusDia.add(new Producto("Menú del Día Premium",  new BigDecimal("14.95"), "Menú del Día", ordenDia++));
                productoRepository.saveAll(menusDia);
            }
        };
    }
}
