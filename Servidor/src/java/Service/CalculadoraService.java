package Service;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servicio web para operaciones de una calculadora
 */
@WebService(serviceName = "CalculadoraService")
public class CalculadoraService {
    
    // URL de la base de datos, usuario y contraseña
    private static final String DB_URL = "jdbc:mysql://localhost:3306/calculadora_service";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "12345678";

    // Método para obtener una conexión a la base de datos
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Método para sumar dos números
     * @param a
     * @param b
     * @return 
     * 
     */
    @WebMethod(operationName = "sumar")
    public double sumar(@WebParam(name = "a") double a, @WebParam(name = "b") double b) {
        double resultado = a + b;
        guardarOperacion("sumar", a, b, resultado);
        return resultado;
    }

    /**
     * Método para restar dos números
     * @param a
     * @param b
     * @return 
     */
    @WebMethod(operationName = "restar")
    public double restar(@WebParam(name = "a") double a, @WebParam(name = "b") double b) {
        double resultado = a - b;
        guardarOperacion("restar", a, b, resultado);
        return resultado;
    }

    /**
     * Método para multiplicar dos números
     * @param a
     * @param b
     * @return 
     */
    @WebMethod(operationName = "multiplicar")
    public double multiplicar(@WebParam(name = "a") double a, @WebParam(name = "b") double b) {
        double resultado = a * b;
        guardarOperacion("multiplicar", a, b, resultado);
        return resultado;
    }

    /**
     * Método para dividir dos números
     * @param a
     * @param b
     * @return 
     */
    @WebMethod(operationName = "dividir")
    public double dividir(@WebParam(name = "a") double a, @WebParam(name = "b") double b) throws IllegalArgumentException {
        if (b == 0) {
            throw new IllegalArgumentException("División por cero no permitida.");
        }
        double resultado = a / b;
        guardarOperacion("dividir", a, b, resultado);
        return resultado;
    }
    
     /**
     * Método para guardar la operación en la base de datos
     * @param a
     * @param b
     * @return 
     */
    
    private void guardarOperacion(String tipo, double operando1, double operando2, double resultado) {
        String sql = "INSERT INTO operaciones (tipo, operando1, operando2, resultado) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, tipo);
            pstmt.setDouble(2, operando1);
            pstmt.setDouble(3, operando2);
            pstmt.setDouble(4, resultado);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al guardar la operación en la base de datos: " + e.getMessage());
        }
    }
}
