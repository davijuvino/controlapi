package br.com.controlapi.service.util;

import org.apache.commons.text.RandomStringGenerator;


/**
 * Classe utilitária para gerar Strings aleatórias.
 */
public final class RandomUtil {

    private static final int DEFAULT = 20;

    private RandomUtil() {
    }

    /**
     * Gerar uma senha.
     * @return a senha gerada
     */
    public static String generatePassword() {
        return new RandomStringGenerator.Builder().withinRange('a', 'z').get()
                .generate(DEFAULT);

    }

    /**
     * Gerar uma chave de ativação.
     * @return a chave de ativação gerada
     */
    public static String generateActivationKey() {
        RandomStringGenerator randomAlphanumeric = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .get();
        return randomAlphanumeric.generate(DEFAULT);
    }

    /**
     * Gerar uma chave de redefinição de senha.
     * @return a chave de redefinição de senha gerada
     */
    public static String generateResetKey() {
        RandomStringGenerator randomAlphanumeric = new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(Character::isLetterOrDigit)
                .get();
        return randomAlphanumeric.generate(DEFAULT);

    }
}
