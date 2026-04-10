package indicafacil.auth.util;

/*
 * Essa classe cuida do telefone.
 * Ela limpa os caracteres, valida tamanho e devolve formatado quando a tela precisa.
 */
public final class TelefoneUtils {
    private TelefoneUtils() {
    }

    public static String normalizar(String telefone) {
        if (telefone == null) {
            return "";
        }

        return telefone.replaceAll("\\D", "");
    }

    public static boolean isValido(String telefone) {
        String telefoneNormalizado = normalizar(telefone);
        return telefoneNormalizado.isEmpty()
            || telefoneNormalizado.length() == 10
            || telefoneNormalizado.length() == 11;
    }

    public static String formatar(String telefone) {
        String telefoneNormalizado = normalizar(telefone);

        if (telefoneNormalizado.length() == 11) {
            return "(" + telefoneNormalizado.substring(0, 2) + ") "
                + telefoneNormalizado.substring(2, 7) + "-"
                + telefoneNormalizado.substring(7, 11);
        }

        if (telefoneNormalizado.length() == 10) {
            return "(" + telefoneNormalizado.substring(0, 2) + ") "
                + telefoneNormalizado.substring(2, 6) + "-"
                + telefoneNormalizado.substring(6, 10);
        }

        return telefoneNormalizado;
    }
}
