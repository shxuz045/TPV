package com.sonia.tpv.util;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sonia.tpv.model.Producto;

public class ProductoComparator implements Comparator<Producto> {

    public static final List<String> CATEGORIAS_ORDEN = List.of(
            "Entrada",
            "Sopa",
            "Arroz & Tallarines",
            "Tortilla & Verduras",
            "Pescado & Gambas",
            "Ternera & Cerdo",
            "Pollo",
            "Mariscos & Patos",
            "Platos Especiales",
            "Menús Especiales",
            "Menú del Día"
    );

    private static final Pattern PATRON_CODIGO = Pattern.compile("(?i)(?:^|\\s)([A-Za-z]*)(\\d+)([A-Za-z]*)");

    @Override
    public int compare(Producto p1, Producto p2) {
        if (p1 == null && p2 == null) return 0;
        if (p1 == null) return 1;
        if (p2 == null) return -1;

        // 1. Comparar categoría según orden oficial del restaurante
        int cat1 = getCategoryRank(p1.getCategoria());
        int cat2 = getCategoryRank(p2.getCategoria());
        if (cat1 != cat2) {
            return Integer.compare(cat1, cat2);
        }

        // Si ambas son categorías no oficiales, agrupar por nombre de categoría
        String c1 = p1.getCategoria() != null ? p1.getCategoria().trim() : "";
        String c2 = p2.getCategoria() != null ? p2.getCategoria().trim() : "";
        if (!c1.equalsIgnoreCase(c2)) {
            return c1.compareToIgnoreCase(c2);
        }

        // 2. Dentro de la misma categoría, ordenar estrictamente por código numérico del plato
        return compareNombresNumerico(p1.getNombre(), p2.getNombre());
    }

    public static int getCategoryRank(String cat) {
        if (cat == null) return 999;
        for (int i = 0; i < CATEGORIAS_ORDEN.size(); i++) {
            if (CATEGORIAS_ORDEN.get(i).equalsIgnoreCase(cat.trim())) {
                return i;
            }
        }
        return 999;
    }

    /**
     * Extrae el código numérico (con letras opcionales, ej: 8, 8b, 8c, P1, 103c) del nombre del plato.
     * Retorna null si el plato no tiene número.
     */
    public static String extraerCodigo(String nombre) {
        if (nombre == null) return null;
        Matcher m = PATRON_CODIGO.matcher(nombre.trim());
        if (m.find()) {
            String pref = m.group(1) != null ? m.group(1).trim() : "";
            String num = m.group(2) != null ? m.group(2).trim() : "";
            String suf = m.group(3) != null ? m.group(3).trim() : "";
            return (pref + num + suf).toLowerCase();
        }
        return null;
    }

    public static int compareNombresNumerico(String n1, String n2) {
        if (n1 == null && n2 == null) return 0;
        if (n1 == null) return 1;
        if (n2 == null) return -1;

        Matcher m1 = PATRON_CODIGO.matcher(n1.trim());
        Matcher m2 = PATRON_CODIGO.matcher(n2.trim());

        boolean f1 = m1.find();
        boolean f2 = m2.find();

        if (f1 && f2) {
            String prefix1 = m1.group(1) != null ? m1.group(1).toLowerCase() : "";
            String prefix2 = m2.group(1) != null ? m2.group(1).toLowerCase() : "";

            // Sin prefijo va antes de con prefijo (ej: platos numéricos antes que "P1")
            if (!prefix1.equals(prefix2)) {
                if (prefix1.isEmpty()) return -1;
                if (prefix2.isEmpty()) return 1;
                return prefix1.compareTo(prefix2);
            }

            int num1 = Integer.parseInt(m1.group(2));
            int num2 = Integer.parseInt(m2.group(2));
            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }

            String suffix1 = m1.group(3) != null ? m1.group(3).toLowerCase() : "";
            String suffix2 = m2.group(3) != null ? m2.group(3).toLowerCase() : "";

            // Sin sufijo va antes que con sufijo (8 va antes que 8b, 8b va antes que 8c)
            if (!suffix1.equals(suffix2)) {
                if (suffix1.isEmpty()) return -1;
                if (suffix2.isEmpty()) return 1;
                return suffix1.compareTo(suffix2);
            }
        } else if (f1) {
            return -1; // Con número va antes que sin número
        } else if (f2) {
            return 1;
        }

        return n1.compareToIgnoreCase(n2);
    }
}

