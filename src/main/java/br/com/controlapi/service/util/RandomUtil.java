package br.com.controlapi.service.util;

import org.apache.commons.text.RandomStringGenerator;

/**
 * Classe utilitária para gerar Strings aleatórias.
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;

    private RandomUtil() {
    }

    /**
     * Gerar uma senha.
     *
     * @return a senha gerada
     */
    public static String generatePassword() {
        return new RandomStringGenerator.Builder().withinRange('a', 'z').get()
                .generate(DEF_COUNT);

    }

    /**
     * Gerar uma chave de ativação.
     *
     * @return a chave de ativação gerada
     */
    public static String generateActivationKey() {
        RandomStringGenerator randomAlphanumeric = new RandomStringGenerator.Builder()
                .withinRange(0, 20)
                .get();
        return randomAlphanumeric.generate(DEF_COUNT);
    }

    /**
     * Gerar uma chave de redefinição de senha.
     *
     * @return a chave de redefinição de senha gerada
     */
    public static String generateResetKey() {
        RandomStringGenerator randomAlphanumeric = new RandomStringGenerator.Builder()
                .withinRange(0, 20)
                .get();
        return randomAlphanumeric.generate(DEF_COUNT);
    }
}

