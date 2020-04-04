package RSA;

public interface Cipher<T> {
    String encrypt(String text, T key);
    String decrypt(String text, T key);

    class Helpers {
        public static <A> String arrayToString(byte[] ar)
        {
            StringBuilder s = new StringBuilder();
            s.append("{ ");
            for (byte a : ar) {
                s.append(Byte.toUnsignedLong(a)).append(", ");
            }
            s.append("}");
            return s.toString();
        }
        public static int getAlphabetIdx(char c)
        {
            return (c>='a' && c<='z')?c-'a' :
                    (c>='A' && c<='Z')?c-'A' :
                            -1;
        }
        public static char shiftAlphabet(char c, int i)
        {
            if(Character.isUpperCase(c))
                return (char)((c-'A' + i%26 + 26)%26 + 'A');
            else return (char)((c-'a' + i%26 + 26)%26 + 'a');
        }
    }

    default void run(String text, T key)
    {
        String enc = encrypt(text, key);
        System.out.println("Cipher: "+getClass().getSimpleName());
        System.out.println("Key: "+key);
        System.out.println("Plain Text: "+text);
        System.out.println("Cipher Text: "+enc);
        System.out.println("Decipher: "+decrypt(enc, key));
    }
}
