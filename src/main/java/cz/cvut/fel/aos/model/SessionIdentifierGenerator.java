package cz.cvut.fel.aos.model;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.math.BigInteger;

/**
 * Komponenta ktora vygeneruje random heslo
 */
@Component
public final class SessionIdentifierGenerator {
    private static SecureRandom random = new SecureRandom();

    /**
     * Generacia random hesla
     * @return - vygenerovane nahodne heslo
     */
    public static String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}
