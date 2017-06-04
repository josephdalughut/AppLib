package ng.joey.lib.java.security;

import org.spongycastle.asn1.ASN1Encoding;
import org.spongycastle.asn1.pkcs.PrivateKeyInfo;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.crypto.AsymmetricBlockCipher;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.InvalidCipherTextException;
import org.spongycastle.crypto.engines.RSAEngine;
import org.spongycastle.crypto.generators.RSAKeyPairGenerator;
import org.spongycastle.crypto.params.AsymmetricKeyParameter;
import org.spongycastle.crypto.params.RSAKeyGenerationParameters;
import org.spongycastle.crypto.util.PrivateKeyFactory;
import org.spongycastle.crypto.util.PrivateKeyInfoFactory;
import org.spongycastle.crypto.util.PublicKeyFactory;
import org.spongycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.spongycastle.jcajce.provider.digest.SHA3;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Joey Dalughut on 8/6/16 at 3:59 PM.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class Crypto{

    /**
     * Class to handle all forms of encryption needed by the application
     */

    public static final class Constants {
        public static final String CHARSET  = "UTF-8";
        public static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";
        public static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
        public static final String RSA = "RSA";
        public static final String AES = "AES";
    }

    /**
     * BCrypt Predicted Costs according to moores law
     * | Date     |  Iterations    | Cost |
     |----------|----------------|------|
     | 1/1/2000 |            64  | 6    |
     | 7/1/2001 |           128  | 7    |
     | 1/1/2003 |           256  | 8    |
     | 7/1/2004 |           512  | 9    |
     | 1/1/2006 |         1,024  | 10   |
     | 6/1/2007 |         2,048  | 11   |
     | 1/1/2009 |         4,096  | 12   |
     | 6/1/2010 |         8,192  | 13   |
     | 1/1/2012 |        16,384  | 14   |
     | 7/1/2013 |        32,768  | 15   |
     | 1/1/2015 |        65,536  | 16   |
     | 6/1/2016 |       131,070  | 17   |
     | 1/1/2018 |       262,144  | 18   |
     | 6/1/2019 |       524,288  | 19   |
     | 1/1/2021 |     1,048,576  | 20   |
     | 7/1/2022 |     2,097,152  | 21   |
     | 1/1/2024 |     4,194,304  | 22   |
     | 6/1/2025 |     8,388,472  | 23   |
     | 1/1/2027 |    16,777,216  | 24   |
     | 6/1/2028 |    33,553,843  | 25   |
     | 1/1/2030 |    67,108,864  | 26   |
     | 7/1/2031 |   134,217,728  | 27   |
     | 1/1/2033 |   268,435,456  | 28   |
     | 1/1/2035 |   536,870,912  | 29   |
     | 1/1/2036 | 1,073,741,824  | 30   |
     | 6/1/2037 | 2,147,437,008  | 31   |
     */

    static {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

    /**
     * Utility class to handle generating hashes using BCrypt, SHA224, SHA364, SHA512...
     */
    public static class HASH {

        /**
         * Utility class to handle generating hashes using the Bcrypt algorithm
         */
        public static class BCRYPT {

            /**
             * Check a plain text against hash to see if it matches
             * @param plain the plain text to check
             * @param hash the hash text we're checking against
             * @return <code>true</code> if the check matched
             * @throws IllegalArgumentException if {@param plain} {@param hash}
             */
            public static boolean check(String plain, String hash) throws IllegalArgumentException {
                if(ng.joey.lib.java.util.Value.IS.emptyValue(plain)) throw new IllegalArgumentException("parameter : plain : emptyValue");
                if(ng.joey.lib.java.util.Value.IS.emptyValue(hash)) throw new IllegalArgumentException("parameter : hash : emptyValue");
                return BCrypt.checkpw(plain, hash);
            }

            /**
             * Generate a hash from a plain text.
             * This method uses the default {@link BCrypt} salt generation method. To use your own
             * salt, call hash(plain, salt) where the salt has been generated using the {@link BCrypt}
             * class method genSalt(int rounds);
             * @param plain the plain text to hash
             * @return the hashed string
             * @throws IllegalArgumentException if {@param plain} is emptyValue or if the hash could not
             * be generated
             */
            public static String hash(String plain) throws IllegalArgumentException {
                if(ng.joey.lib.java.util.Value.IS.emptyValue(plain)) throw new IllegalArgumentException("parameter : plain : emptyValue");
                return BCrypt.hashpw(plain, BCrypt.gensalt());
            }

            /**
             * Generate a hash from a plain text.
             * @param salt the salt to use in hashing, preferably generated using the {@link BCrypt}
             * class method genSalt(int rounds);
             * @param plain the plain text to hash
             * @return the hashed string
             * @throws IllegalArgumentException if any of {@param plain} or {@param salt}is emptyValue
             * or if the hash could not be generated
             */
            public static String hash(String plain, String salt) throws IllegalArgumentException{
                if(ng.joey.lib.java.util.Value.IS.emptyValue(plain)) throw new IllegalStateException("paremeter : plain : emptyValue");
                if(ng.joey.lib.java.util.Value.IS.emptyValue(salt)) throw new IllegalArgumentException("parameter : salt : emptyValue");
                return BCrypt.hashpw(plain, salt);
            }

        }

        /**
         * Utility class to handle generating hashes using the SHA algorithm
         */
        public static class SHA {

            /**
             * Generate a hash based on sha-224. The default charset is UTF-8
             * @param plain the string to be hashed
             * @return the sha-224 of {@param plain}
             */
            public static String _224(String plain) {
                try {
                    SHA3.Digest224 digest = new SHA3.Digest224();
                    digest.reset();
                    digest.update(plain.getBytes(Constants.CHARSET));
                    return Base64.getEncoder().encodeToString(digest.digest());
                }catch (Exception e){
                    return null;
                }
            }

            /**
             * Generate the bytes of a hash based on sha-224. The default charset is UTF-8
             * @param plain the string to be hashed
             * @return the sha-224 bytes of {@param plain}
             */
            public static byte[] _224Bytes(String plain) {
                try {
                    SHA3.Digest224 digest = new SHA3.Digest224();
                    digest.reset();
                    digest.update(plain.getBytes(Constants.CHARSET));
                    return Base64.getEncoder().encode(digest.digest());
                }catch (Exception e){
                    return null;
                }
            }

            /**
             * Generate a hash based on sha-256. The default charset is UTF-8
             * @param plain the string to be hashed
             * @return the sha-256 of {@param plain}
             */
            public static String _256(String plain){
                try {
                    SHA3.Digest256 digest = new SHA3.Digest256();
                    digest.reset();
                    digest.update(plain.getBytes(Constants.CHARSET));
                    return Base64.getEncoder().encodeToString(digest.digest());
                }catch (Exception e){
                    return null;
                }
            }

            /**
             * Generate the bytes of a hash based on sha-256. The default charset is UTF-8
             * @param plain the string to be hashed
             * @return the sha-256 bytes of {@param plain}
             */
            public static byte[] _256Bytes(String plain){
                try {
                    SHA3.Digest256 digest = new SHA3.Digest256();
                    digest.reset();
                    digest.update(plain.getBytes(Constants.CHARSET));
                    return Base64.getEncoder().encode(digest.digest());
                }catch (Exception e){
                    return null;
                }
            }

            /**
             * Generate a hash based on sha-384. The default charset is UTF-8
             * @param plain the string to be hashed
             * @return the sha-384 of {@param plain}
             */
            public static String _384(String plain){
                try {
                    SHA3.Digest384 digest = new SHA3.Digest384();
                    digest.reset();
                    digest.update(plain.getBytes(Constants.CHARSET));
                    return Base64.getEncoder().encodeToString(digest.digest());
                }catch (Exception e){
                    return null;
                }
            }

            /**
             * Generate the bytes of a hash based on sha-384. The default charset is UTF-8
             * @param plain the string to be hashed
             * @return the sha-384 bytes of {@param plain}
             */
            public static byte[] _384Bytes(String plain){
                try {
                    SHA3.Digest384 digest = new SHA3.Digest384();
                    digest.reset();
                    digest.update(plain.getBytes(Constants.CHARSET));
                    return Base64.getEncoder().encode(digest.digest());
                }catch (Exception e){
                    return null;
                }
            }

            /**
             * Generate a hash based on sha-512. The default charset is UTF-8
             * @param plain the string to be hashed
             * @return the sha-512 of {@param plain}
             */
            public static String _512(String plain){
                try{
                    SHA3.Digest512 digest = new SHA3.Digest512();
                    digest.reset();
                    digest.update(plain.getBytes(Constants.CHARSET));
                    return Base64.getEncoder().encodeToString(digest.digest());
                }catch(Exception e){
                    return null;
                }
            }

            /**
             * Generate the bytes of a hash based on sha-512. The default charset is UTF-8
             * @param plain the string to be hashed
             * @return the sha-512 bytes of {@param plain}
             */
            public static byte[] _512Bytes(String plain){
                try{
                    SHA3.Digest512 digest = new SHA3.Digest512();
                    digest.reset();
                    digest.update(plain.getBytes(Constants.CHARSET));
                    return Base64.getEncoder().encode(digest.digest());
                }catch(Exception e){
                    return null;
                }
            }

        }

    }

    /**
     * Utility class to handle encryption and decryption in AES
     */
    public static class AES {

        /**
         * Encrypt a string using the AES algorithm.
         *  Default Transformation : 'AES/CBC/PKCS5Padding'
         *  A secret key would be created using your {@param key} and the default Secret key
         *  algorithm : 'PBKDF2WithHmacSHA1'
         * @param key the key to use for encryption.
         * @param value the string to be encrypted
         * @return the encrypted value
         * @throws Exception if there's was an exception. For this method, this will theoretically
         * never happen. This has also been practically tested.
         */
        public static String encrypt(String key, String value) throws Exception{
            return encrypt(key, value, generateSecretKey(key));
        }

        /**
         * Encrypt a string using the AES algorithm.
         *  Default Transformation : 'AES/CBC/PKCS5Padding'
         *  Default Secret key algorithm : 'PBKDF2WithHmacSHA1'
         * @param key the key to use for encryption.
         * @param value the string to be encrypted
         * @param secretKey the {@link SecretKey} to be used for encryption
         * @return the encrypted value
         * @throws Exception if there's was an exception. For this method, this will theoretically
         * never happen. This has also been practically tested.
         */
        public static String encrypt(String key, String value, SecretKey secretKey) throws Exception{
            if(ng.joey.lib.java.util.Value.IS.emptyValue(key)) throw new IllegalArgumentException("parameter : key : emptyValue");
            if(ng.joey.lib.java.util.Value.IS.emptyValue(value)) throw new IllegalArgumentException("parameter : value : emptyValue");
            if(ng.joey.lib.java.util.Value.IS.emptyValue(secretKey)) throw new IllegalArgumentException("paremeter : secretKey : invald");
            try {
                Cipher cipher = Cipher.getInstance(Constants.AES_TRANSFORMATION);
                byte[] l = new byte[cipher.getBlockSize()];
                System.arraycopy(HASH.SHA._384Bytes(key), 0, l, 0, cipher.getBlockSize());
                IvParameterSpec ivSpec = new IvParameterSpec(l);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
                return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(Constants.CHARSET)));
            } catch (NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
                throw e;
            }
        }

        /**
         * Decrypt a string using the AES algorithm.
         *  Default Transformation : 'AES/CBC/PKCS5Padding'
         *  Default Secret key algorithm : 'PBKDF2WithHmacSHA1'
         * @param key the key to use for the decryption
         * @param value the string to be decrypted
         * @return the decrypted value
         * @throws Exception if there's was an exception. For this method, this will theoretically
         * never happen. This has also been practically tested.
         */
        public static String decrypt(String key, String value) throws Exception{
            return decrypt(key, value, generateSecretKey(key));
        }

        /**
         * Decrypt a string using the AES algorithm.
         *  Default Transformation : 'AES/CBC/PKCS5Padding'
         *  A secret key would be created using your {@param key} and the default Secret key
         *  algorithm : 'PBKDF2WithHmacSHA1'
         * @param key the key to use for the decryption
         * @param value the string to be decrypted
         * @return the decrypted value
         * @throws Exception if there's was an exception. For this method, this will theoretically
         * never happen. This has also been practically tested.
         */
        public static String decrypt(String key, String value, SecretKey secretKey) throws Exception{
            try {
                Cipher cipher = Cipher.getInstance(Constants.AES_TRANSFORMATION);
                byte[] l = new byte[cipher.getBlockSize()];
                System.arraycopy(HASH.SHA._384Bytes(key), 0, l, 0, cipher.getBlockSize());
                IvParameterSpec ivSpec = new IvParameterSpec(l);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
                return new String(cipher.doFinal(Base64.getDecoder().decode(value.getBytes(Constants.CHARSET))), Constants.CHARSET);
            } catch (NoSuchPaddingException | UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException e) {
                throw e;
            }
        }

        /**
         * Generate a {@link SecretKey} using the default secret key algorithm 'PBKDF2WithHmacSHA1'
         * @param key the string to generate the {@link SecretKey} with
         * @return the generated {@link SecretKey}
         * @throws NoSuchAlgorithmException if the secret key algorithm specified is invalid. This
         * however shouldn't happen for this method.
         * @throws InvalidKeySpecException if the {@link KeySpec} specified is invalid. This however
         * shouldn't happen for this method.
         */
        public static SecretKey generateSecretKey(String key) throws NoSuchAlgorithmException, InvalidKeySpecException {
            final int iterations = 1000, outputKeyLength = 256;
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(Constants.SECRET_KEY_ALGORITHM);
            KeySpec keySpec = new PBEKeySpec(key.toCharArray(), HASH.SHA._224Bytes(key), iterations, outputKeyLength);
            SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
            return new SecretKeySpec(secretKey.getEncoded(), Constants.AES);
        }

        private static String getRandomAESKey() throws NoSuchAlgorithmException {
            java.security.Key key;
            SecureRandom rand = new SecureRandom();
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(rand);
            generator.init(256);
            key = generator.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
        }

    }

    /**
     * Utility class to handle encryption and decryption in RSA
     */
    public static class RSA {


        public static RSAKeyPairInfo generateKeyPairInfo() {
            try {
                AsymmetricCipherKeyPair pair = generateKeyPair();
                return new RSAKeyPairInfo(pair.getPrivate(), pair.getPublic());
            } catch (IOException e) {
                return null;
            }
        }
        public static AsymmetricCipherKeyPair generateKeyPair() {
            RSAKeyPairGenerator generator = new RSAKeyPairGenerator();
            RSAKeyGenerationParameters parameters = new RSAKeyGenerationParameters(BigInteger.valueOf(65537), new SecureRandom(), 2048, 5);
            generator.init(parameters);
            AsymmetricCipherKeyPair pair = generator.generateKeyPair();
            return pair;
        }

        /**
         * Utility class for holding String pairs of private and public RSA as strings.
         * Helper methods are available for converting strings to {@link AsymmetricKeyParameter}'s
         * and vice-versa.
         */
        public static class RSAKeyPairInfo {
            public String PRIVATE; //private key string
            public String PUBLIC; //public key string

            public RSAKeyPairInfo(String PRIVATE, String PUBLIC) {
                this.PRIVATE = PRIVATE;
                this.PUBLIC = PUBLIC;
            }

            public RSAKeyPairInfo(AsymmetricKeyParameter privateKey, AsymmetricKeyParameter publicKey) throws IOException{
                this(fromRSAPrivateKey(privateKey), fromRSAPublicKey(publicKey));
            }

            /**
             * Generate an {@link AsymmetricKeyParameter} from its public string representation
             * @param key the string to be converted
             * @return the generated {@link AsymmetricKeyParameter}
             * @throws IOException if an error occured during the conversion
             */
            public static AsymmetricKeyParameter toPublicKey(String key) throws IOException {
                return PublicKeyFactory.createKey(Base64.getDecoder().decode(key));
            }

            /**
             * Generate an {@link AsymmetricKeyParameter} from its private string representation
             * @param key the string to be converted
             * @return the generated {@link AsymmetricKeyParameter}
             * @throws IOException if an error occured during the conversion
             */
            public static AsymmetricKeyParameter toPrivateKey(String key) throws IOException {
                return PrivateKeyFactory.createKey(Base64.getDecoder().decode(key));
            }

            /**
             * Convert a private {@link AsymmetricKeyParameter} to its string representation
             * @param privateKey the {@link AsymmetricKeyParameter} to be converted
             * @return the generated string
             * @throws IOException if an error occured during the conversion
             */
            public static String fromRSAPrivateKey(AsymmetricKeyParameter privateKey) throws IOException {
                PrivateKeyInfo privateKeyInfo = PrivateKeyInfoFactory.createPrivateKeyInfo(privateKey);
                byte[] serializedPrivateBytes = privateKeyInfo.toASN1Primitive().getEncoded(ASN1Encoding.DER);
                return Base64.getEncoder().encodeToString(serializedPrivateBytes);
            }

            /**
             * Convert a public {@link AsymmetricKeyParameter} to its string representation
             * @param publicKey the {@link AsymmetricKeyParameter} to be converted
             * @return the generated string
             * @throws IOException if an error occured during the conversion
             */
            public static String fromRSAPublicKey(AsymmetricKeyParameter publicKey) throws IOException {
                SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKey);
                byte[] serializedPublicBytes = publicKeyInfo.toASN1Primitive().getEncoded(ASN1Encoding.DER);
                return Base64.getEncoder().encodeToString(serializedPublicBytes);
            }

        }

        /**
         * Encrypt a string using an rsa public key
         *
         * Note that if you have the key as a string, use one of the methods implemented in the
         * {@link RSAKeyPairInfo} class to convert to an {@link AsymmetricKeyParameter}
         *
         * @param string the string to be encrypted
         * @param publicKey the public key to use for encryption
         * @return the encrypted string
         * @throws Exception if an error occurred during encryption
         */
        public static String encrypt(String string, AsymmetricKeyParameter publicKey) throws Exception {
            Security.addProvider(new BouncyCastleProvider());
            byte[] data = string.getBytes(Constants.CHARSET);
            RSAEngine engine = new RSAEngine();
            engine.init(true, publicKey);
            byte[] hexEncodedCipher = engine.processBlock(data, 0, data.length);
            return getHexString(hexEncodedCipher);
        }

        /**
         * Decrypt a string using an rsa private key
         *
         * Note that if you have the key as a string, use one of the methods implemented in the
         * {@link RSAKeyPairInfo} class to convert to an {@link AsymmetricKeyParameter}
         *
         * @param string the string to be encrypted
         * @param privateKey the private key to use for decryption
         * @return the decrypted string
         * @throws InvalidCipherTextException if an error occurred during decryption
         */
        public static String decrypt(String string, AsymmetricKeyParameter privateKey) throws InvalidCipherTextException {
            AsymmetricBlockCipher engine = new RSAEngine();
            engine.init(false, privateKey);
            byte[] encryptedBytes = hexStringToByteArray(string);
            byte[] hexEncodedCipher = engine.processBlock(encryptedBytes, 0, encryptedBytes.length);
            return new String(hexEncodedCipher);
        }

        private static String getHexString(byte[] b) throws Exception {
            String result = "";
            for (int i = 0; i < b.length; i++) {
                result +=
                        Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
            return result;
        }

        private static byte[] hexStringToByteArray(String s) {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
            }
            return data;
        }

    }


    /**
     * Utiity class for generating Random values
     */
    public static class Random extends java.util.Random {

        /**
         * Generate a uuid string
         * Universally Unique Identifier
         * @return a unique random string
         */
        public static String uuid(){
            return UUID.randomUUID().toString();
        }

        /**
         * Generate a uuid string, without separators
         * Universally Unique Identifier
         * @return a unique random string without seperators
         */
        public static String uuidClear(){
            return uuid().replaceAll("-", "");
        }

        public Long nextLong(long n) {
            long bits, val;
            do {
                bits = (nextLong() << 1) >>> 1;
                val = bits % n;
            } while (bits-val+(n-1) < 0L);
            return val;
        }

    }

    /**
     * This class consists exclusively of static methods for obtaining
     * encoders and decoders for the BB encoding scheme. The
     * implementation of this class supports the following types of BB
     * as specified in
     * <a href="http://www.ietf.org/rfc/rfc4648.txt">RFC 4648</a> and
     * <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>.
     *
     * <ul>
     * <li><a name="basic"><b>Basic</b></a>
     * <p> Uses "The BB Alphabet" as specified in Table 1 of
     *     RFC 4648 and RFC 2045 for encoding and decoding operation.
     *     The encoder does not add any line feed (line separator)
     *     character. The decoder rejects data that contains characters
     *     outside the base64 alphabet.</p></li>
     *
     * <li><a name="url"><b>URL and Filename safe</b></a>
     * <p> Uses the "URL and Filename safe BB Alphabet" as specified
     *     in Table 2 of RFC 4648 for encoding and decoding. The
     *     encoder does not add any line feed (line separator) character.
     *     The decoder rejects data that contains characters outside the
     *     base64 alphabet.</p></li>
     *
     * <li><a name="mime"><b>MIME</b></a>
     * <p> Uses the "The BB Alphabet" as specified in Table 1 of
     *     RFC 2045 for encoding and decoding operation. The encoded output
     *     must be represented in lines of no more than 76 characters each
     *     and uses a carriage return {@code '\r'} followed immediately by
     *     a linefeed {@code '\n'} as the line separator. No line separator
     *     is added to the end of the encoded output. All line separators
     *     or other characters not found in the base64 alphabet table are
     *     ignored in decoding operation.</p></li>
     * </ul>
     *
     * <p> Unless otherwise noted, passing a {@code null} argument to a
     * method of this class will cause a {@link NullPointerException
     * NullPointerException} to be thrown.
     *
     * @author  Xueming Shen
     * @since   1.8
     */

    
}