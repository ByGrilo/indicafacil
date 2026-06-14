package indicafacil.auth.util;

/*
 * Classe pequena so pra deixar o tratamento de e-mail num lugar so.
 * Assim o service e o repositorio nao precisam repetir a mesma regra.
 */
public final class EmailUtils {
    private EmailUtils() {
    }

    public static String normalizar(String email) {
        if (email == null) {
            return "";
        }

        return email.trim().toLowerCase();
    }

    public static boolean isValido(String email) {
        String emailNormalizado = normalizar(email);
        if (emailNormalizado.isEmpty()) {
            return false;
        }

        return emailNormalizado.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$");
    }
}
