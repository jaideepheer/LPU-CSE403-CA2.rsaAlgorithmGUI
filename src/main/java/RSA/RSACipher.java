package RSA;

import java.math.BigInteger;
import java.util.Random;

import static RSAGUI.RSAGUI.LOG;

public class RSACipher implements Cipher<RSACipher.KeyPair> {

    public static class KeyPair {

        public BigInteger getPrivate_exp() {
            return private_exp;
        }
        public BigInteger getPublic_exp() {
            return public_exp;
        }
        public BigInteger getN() {
            return n;
        }

        private BigInteger private_exp, public_exp, n;
        private KeyPair(){}

        public static KeyPair makeKeyPair(BigInteger private_exp, BigInteger public_exp, BigInteger n)
        {
            KeyPair k = new KeyPair();
            k.n = n;
            k.private_exp = private_exp;
            k.public_exp = public_exp;
            return k;
        }

        public static KeyPair generateKeyPair(int bitLength)
        {
            assert bitLength > 7; // At least 1 byte bit length

            LOG.debug(String.format("Generating %d-bit random prime numbers for RSA.", bitLength));

            Random rnd = new Random();
            BigInteger prime_1 = BigInteger.probablePrime(bitLength, rnd);
            BigInteger prime_2 = BigInteger.probablePrime(bitLength, rnd);

            LOG.debug(String.format("Prime_1: %s", prime_1));
            LOG.debug(String.format("Prime_2: %s", prime_2));

            LOG.debug("Generating RSA keypair.");
            KeyPair kp = new KeyPair();
            kp.n = prime_1.multiply(prime_2);

            BigInteger totient = prime_1.subtract(BigInteger.valueOf(1L)).multiply(prime_2.subtract(BigInteger.valueOf(1L)));
            LOG.debug(String.format("Totient(prime1)*Totient(prime2) for the generated primes is: %s", totient));

            kp.public_exp = BigInteger.probablePrime(totient.bitLength()-1, rnd);
            kp.private_exp = kp.public_exp.modInverse(totient);

            LOG.debug(String.format("Generated keypair is: %s", kp));
            return kp;
        }
        public static KeyPair generateKeyPair(){return generateKeyPair(512);}

        @Override
        public String toString() {
            return "KeyPair{" +
                    "private_exp=" + private_exp +
                    ", public_exp=" + public_exp +
                    ", n=" + n +
                    '}';
        }
    }

    @Override
    public String encrypt(String text, KeyPair key) {
        // N should be > text character
        //return Helpers.arrayToString(text.codePoints()
        //        .mapToObj((ch)->BigInteger.valueOf(ch).modPow(key.public_exp, key.n))
        //.flatMap((val)-> Arrays.stream(val.toByteArray())));
        return Helpers.arrayToString(new BigInteger(text.getBytes()).modPow(key.public_exp, key.n).toByteArray());
    }

    @Override
    public String decrypt(String text, KeyPair key) {
        return Helpers.arrayToString(new BigInteger(text.getBytes()).modPow(key.private_exp, key.n).toByteArray());
    }

    public String encrypt(BigInteger text, KeyPair key)
    {
        return text.modPow(key.public_exp, key.n).toString();
    }
    public String decrypt(BigInteger text, KeyPair key)
    {
        return text.modPow(key.private_exp, key.n).toString();
    }

    public static void main(String[] args) {
        RSACipher c = new RSACipher();
        KeyPair kp = RSACipher.KeyPair.generateKeyPair(8);
        c.run("Hello",kp);
    }
}
