package service;

import model.ExchangeRateResponse;

import java.util.Scanner;
import java.util.Set;

public class Menu {

    private static final String URL_EXCHANGERATE_API = "https://v6.exchangerate-api.com/v6/API_KEY/latest/BASE_CURRENCY";
    private static final String API_KEY = "984f8607c22d872817c52ce6";
    private static final Set<String> CURRENCY_CODE = Set.of("ARS", "BOB", "BRL", "CLP", "COP", "USD");
    private static final Scanner scanner = new Scanner(System.in);
    private static final String OPCIONES_MENU = """
            ================== MENU ===================
            1. Cambio de moneda
            2. Cambiar la Base de la moneda
            0. Salir
            """;
    private static final String OPCIONES_TIPO_CAMBIO = """
            --------------------------------
            Código Moneda | Nombre moneda
            -------------------------------
            ARS           | Peso argentino
            BOB           | Boliviano boliviano
            BRL           | Real brasileño
            CLP           | Peso chileno
            COP           | Peso colombiano
            USD           | Dólar estadounidense
            """;


    private String codigoMonedaBase = "USD";

    public void ejecutarMenu() {
        int opc;
        do {
            System.out.println(OPCIONES_MENU);
            opc = scanner.nextInt();
            switch (opc) {
                case 0 -> System.out.println("\n\n¡Hasta la próxima :D !\n\n");
                case 1 -> convertirMoneda();
                case 2 -> cambiarMonedaBase();
                default -> System.err.println("Favor de elegir una opción valida");
            }
        } while (opc != 0);
    }

    public void convertirMoneda() {
        final var url = URL_EXCHANGERATE_API.replace("API_KEY", API_KEY)
                .replace("BASE_CURRENCY", codigoMonedaBase);
        var codigoMonedaTipoCambio = validarCodigoMoneda("Ingresa el 'Codigo de Moneda' a convertir el tipo de cambio: \n" + OPCIONES_TIPO_CAMBIO);
        final var valorTipoCambio = ConsumoAPI.get(url, ExchangeRateResponse.class)
                .conversion_rates().get(codigoMonedaTipoCambio);
        final var cantidad = validarCantidad("Ingresa la cantidad: ");
        System.out.println("----------------- RESULTADO CONVERSION ------------- ");
        System.out.println("Moneda base: " + codigoMonedaBase);
        System.out.println("Moneda a convertir: " + codigoMonedaTipoCambio);
        System.out.println("Tipo Cambio: " + valorTipoCambio);
        System.out.println("Total: " + valorTipoCambio * cantidad);
        System.out.println("----------------------------------------------------\n");
    }

    public void cambiarMonedaBase() {
        codigoMonedaBase = validarCodigoMoneda("Ingresa el 'Codigo de Moneda Base' al que quieres cambiar: \n" + OPCIONES_TIPO_CAMBIO);
    }

    // VALIDACIONES
    private String validarCodigoMoneda(String mensaje) {
        String codigoMoneda;
        boolean noValido;
        do {
            System.out.println(mensaje);
            codigoMoneda = scanner.next().toUpperCase().trim();
            noValido = !CURRENCY_CODE.contains(codigoMoneda);
            if (noValido) {
                System.err.println("\nxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                System.err.println("Ingrese una opcion valida: ");
                System.err.println(CURRENCY_CODE);
                System.err.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx\n");
            }
        } while (noValido);
        return codigoMoneda;
    }

    private double validarCantidad(String mensaje) {
        double cantidad = 0.0;
        boolean noValido = true;
        do {
            try {
                System.out.println(mensaje);
                var cantidadStr = scanner.next().trim();
                cantidad = Double.parseDouble(cantidadStr);
                noValido = false;
            } catch (Exception e) {
                System.err.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
                System.err.println("Ingrese una cantidad valida ");
            }
        } while (noValido);
        return cantidad;
    }
}
