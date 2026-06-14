package indicafacil.auth.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * Essa util faz a parte simples de seguranca da senha.
 * Ela gera o hash e tambem compara a senha digitada com o valor salvo.
 */
public final class PasswordUtils {
    private PasswordUtils() {
    }

    public static String gerarHash(String senha) {
        try {
            // O SHA-256 ja resolve bem pra esse trabalho da faculdade.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(senha.getBytes(StandardCharsets.UTF_8));
            return bytesParaHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("Algoritmo de hash nao disponivel.", exception);
        }
    }

    public static boolean verificarSenha(String senhaInformada, String hashArmazenado) {
        return gerarHash(senhaInformada).equals(hashArmazenado);
    }

    private static String bytesParaHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();

        // Converte o array de bytes pra uma string hex que da pra salvar no banco.
        for (byte value : bytes) {
            builder.append(String.format("%02x", value));
        }

        return builder.toString();
    }
}
