package com.sonia.tpv.model;

public enum FaseDomicilio {
    PENDIENTE("Pendiente", "1. Pendientes"),
    RECEPCION("Pendiente", "1. Pendientes"),
    PREPARACION("Pendiente", "1. Pendientes"),
    EN_REPARTO("En Reparto", "2. En Reparto"),
    ENTREGADO("Entregado", "3. Entregados y Cobrados");

    private final String etiqueta;
    private final String tituloColumna;

    FaseDomicilio(String etiqueta, String tituloColumna) {
        this.etiqueta = etiqueta;
        this.tituloColumna = tituloColumna;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getTituloColumna() {
        return tituloColumna;
    }
}
