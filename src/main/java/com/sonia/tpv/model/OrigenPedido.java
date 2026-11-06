package com.sonia.tpv.model;

public enum OrigenPedido {
    TELEFONO("Teléfono", "📞"),
    WEB("Web", "🌐");

    private final String etiqueta;
    private final String icono;

    OrigenPedido(String etiqueta, String icono) {
        this.etiqueta = etiqueta;
        this.icono = icono;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getIcono() {
        return icono;
    }
}
