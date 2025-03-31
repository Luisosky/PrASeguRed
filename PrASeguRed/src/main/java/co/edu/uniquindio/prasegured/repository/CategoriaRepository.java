package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Reporte;

public interface CategoriaRepository {
    Reporte findByReporte(Reporte reporte);
}
