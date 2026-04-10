package indicafacil.auth.util;

/*
 * Aqui fica tudo que e regra de CPF.
 * Ela normaliza, valida, formata e mascara pra nao espalhar essa conta no resto do projeto.
 */
public final class CpfUtils {
    private CpfUtils() {
    }

    public static String normalizar(String cpf) {
        if (cpf == null) {
            return "";
        }

        return cpf.replaceAll("\\D", "");
    }

    public static boolean isValido(String cpf) {
        String cpfNormalizado = normalizar(cpf);

        if (cpfNormalizado.length() != 11) {
            return false;
        }
        if (todosDigitosIguais(cpfNormalizado)) {
            return false;
        }

        // Faz o calculo real dos digitos verificadores.
        int primeiroDigito = calcularDigitoVerificador(cpfNormalizado.substring(0, 9), 10);
        int segundoDigito = calcularDigitoVerificador(cpfNormalizado.substring(0, 9) + primeiroDigito, 11);

        return cpfNormalizado.equals(cpfNormalizado.substring(0, 9) + primeiroDigito + segundoDigito);
    }

    public static String formatar(String cpf) {
        String cpfNormalizado = normalizar(cpf);

        if (cpfNormalizado.length() != 11) {
            return cpfNormalizado;
        }

        return cpfNormalizado.substring(0, 3)
            + "."
            + cpfNormalizado.substring(3, 6)
            + "."
            + cpfNormalizado.substring(6, 9)
            + "-"
            + cpfNormalizado.substring(9);
    }

    public static String mascarar(String cpf) {
        String cpfNormalizado = normalizar(cpf);

        if (cpfNormalizado.length() != 11) {
            return cpfNormalizado;
        }

        return "***." + cpfNormalizado.substring(3, 6) + ".***-" + cpfNormalizado.substring(9);
    }

    private static int calcularDigitoVerificador(String base, int pesoInicial) {
        int soma = 0;
        int peso = pesoInicial;

        for (int i = 0; i < base.length(); i++) {
            int digito = Character.getNumericValue(base.charAt(i));
            soma += digito * peso;
            peso--;
        }

        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private static boolean todosDigitosIguais(String cpf) {
        char primeiroCaractere = cpf.charAt(0);

        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != primeiroCaractere) {
                return false;
            }
        }

        return true;
    }
}
