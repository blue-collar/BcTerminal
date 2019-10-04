package com.telewave.lib.base.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @Author: rick_tan
 * @Date: 19-7-23
 * @Version: v1.0
 * @Des 包含加解密工具
 */
public class EncryptUtils {
    private static final String Algorithm = "DESede";

    //生成MD5
    public static String getMD5(String message) {
        String md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象
            byte[] messageByte = message.getBytes("UTF-8");
            byte[] md5Byte = md.digest(messageByte);              // 获得MD5字节数组,16*8=128位
            md5 = bytesToHex(md5Byte);                            // 转换为16进制字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    // 二进制转十六进制
    public static String bytesToHex(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toLowerCase();
    }

    /**
     * 加密方法
     *
     * @param src 源数据的字节数组
     * @return 返回base64字符串
     */
    public static String encryptModeToString(String src, String key) throws Exception {
        if (src != null) {
            try {
                byte[] des = src.getBytes();
                SecretKey deskey = new SecretKeySpec(
                        getKeybyte(initKeyString(key)), Algorithm); // 生成密钥
                Cipher c1 = Cipher.getInstance(Algorithm); // 实例化负责加密/解密的Cipher工具类
                c1.init(Cipher.ENCRYPT_MODE, deskey); // 初始化为加密模式
                return encryptBASE64(c1.doFinal(des));
            } catch (java.security.NoSuchAlgorithmException e1) {
                throw e1;
            } catch (javax.crypto.NoSuchPaddingException e2) {
                throw e2;
            } catch (Exception e3) {
                throw e3;
            }
        }
        return null;
    }

    /**
     * 初始化密钥
     *
     * @throws Exception
     */
    public static String initKeyString(String key) throws Exception {
        return encryptBASE64(build3DesKey(key));
    }

    /**
     * 获取密钥
     *
     * @param key base64码
     * @throws Exception
     */
    public static byte[] getKeybyte(String key) throws Exception {
        return decryptBASE64(key);
    }

    /***
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (Base64Util.encodeToString(key, Base64Util.DEFAULT));
    }

    /****
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (Base64Util.decode(key, Base64Util.DEFAULT));
    }

    /**
     * 根据字符串生成密钥字节数组
     *
     * @param keyStr 密钥字符串
     */
    public static byte[] build3DesKey(String keyStr)
            throws Exception {
        byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8"); // 将字符串转成字节数组

        /*
         * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
         */
        if (key.length > temp.length) {
            // 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            // 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    private static class Base64Util {
        /**
         * Default values for encoder/decoder flags.
         */
        public static final int DEFAULT = 0;

        /**
         * Encoder flag bit to omit the padding '=' characters at the end
         * of the output (if any).
         */
        public static final int NO_PADDING = 1;

        /**
         * Encoder flag bit to omit all line terminators (i.e., the output
         * will be on one long line).
         */
        public static final int NO_WRAP = 2;

        /**
         * Encoder flag bit to indicate lines should be terminated with a
         * CRLF pair instead of just an LF.  Has no effect if {@code
         * NO_WRAP}* is specified as well.
         */
        public static final int CRLF = 4;

        /**
         * Encoder/decoder flag bit to indicate using the "URL and
         * filename safe" variant of Base64Util (see RFC 3548 section 4) where
         * {@code -} and {@code _} are used in place of {@code +} and
         * {@code /}.
         */
        public static final int URL_SAFE = 8;

        /**
         * Flag to pass to {@link android.util.Base64OutputStream} to indicate that it
         * should not onClose the output stream it is wrapping when it
         * itself is closed.
         */
        public static final int NO_CLOSE = 16;

        //  --------------------------------------------------------
        //  shared code
        //  --------------------------------------------------------

        /**
         * The type Coder.
         */
        public static abstract class Coder {
            /**
             * The Output.
             */
            public byte[] output;
            /**
             * The Op.
             */
            public int op;

            /**
             * Encode/decode another block of input data.  this.output is
             * provided by the caller, and must be big enough to hold all
             * the coded data.  On exit, this.opwill be set to the length
             * of the coded data.
             *
             * @param input  the input
             * @param offset the offset
             * @param len    the len
             * @param finish true if this is the final call to process for        this object.  Will
             *               finalize the coder state and        include any final bytes in the output.
             * @return true if the input so far is good; false if some         error has been detected in
             * the input stream..
             */
            public abstract boolean process(byte[] input, int offset, int len, boolean finish);

            /**
             * Max output size int.
             *
             * @param len the len
             * @return the maximum number of bytes a call to process() could produce for the given number
             * of input bytes.  This may be an overestimate.
             */
            public abstract int maxOutputSize(int len);
        }

        //  --------------------------------------------------------
        //  decoding
        //  --------------------------------------------------------

        /**
         * Decode the Base64Util-encoded data in input and return the data in
         * a new byte array.
         * <p/>
         * <p>The padding '=' characters at the end are considered optional, but
         * if any are present, there must be the correct number of them.
         *
         * @param str   the input String to decode, which is converted to               bytes using the
         *              default charset
         * @param flags controls certain features of the decoded output.               Pass {@code
         *              DEFAULT} to decode standard Base64Util.
         * @return the byte [ ]
         */
        public static byte[] decode(String str, int flags) {
            return decode(str.getBytes(), flags);
        }

        /**
         * Decode the Base64Util-encoded data in input and return the data in
         * a new byte array.
         * <p/>
         * <p>The padding '=' characters at the end are considered optional, but
         * if any are present, there must be the correct number of them.
         *
         * @param input the input array to decode
         * @param flags controls certain features of the decoded output.               Pass {@code
         *              DEFAULT} to decode standard Base64Util.
         * @return the byte [ ]
         */
        public static byte[] decode(byte[] input, int flags) {
            return decode(input, 0, input.length, flags);
        }

        /**
         * Decode the Base64Util-encoded data in input and return the data in
         * a new byte array.
         * <p/>
         * <p>The padding '=' characters at the end are considered optional, but
         * if any are present, there must be the correct number of them.
         *
         * @param input  the data to decode
         * @param offset the position within the input array at which to start
         * @param len    the number of bytes of input to decode
         * @param flags  controls certain features of the decoded output.               Pass {@code
         *               DEFAULT} to decode standard Base64Util.
         * @return the byte [ ]
         */
        public static byte[] decode(byte[] input, int offset, int len, int flags) {
            // Allocate space for the most data the input could represent.
            // (It could contain less if it contains whitespace, etc.)
            Decoder decoder = new Decoder(flags, new byte[len * 3 / 4]);

            if (!decoder.process(input, offset, len, true)) {
                Log.e("Base64Util", "decode(),process err");
                return new byte[0];
                //            throw new IllegalArgumentException("bad base-64");
            }

            // Maybe we got lucky and allocated exactly enough output space.
            if (decoder.op == decoder.output.length) {
                return decoder.output;
            }

            // Need to shorten the array, so allocate a new one of the
            // right size and copy.
            byte[] temp = new byte[decoder.op];
            System.arraycopy(decoder.output, 0, temp, 0, decoder.op);
            return temp;
        }

        /**
         * The type Decoder.
         */
        /* package */ static class Decoder extends Coder {
            /**
             * Lookup table for turning bytes into their position in the
             * Base64Util alphabet.
             */
            private static final int DECODE[] = {
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
                    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1,
                    -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
                    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            };

            /**
             * Decode lookup table for the "web safe" variant (RFC 3548
             * sec. 4) where - and _ replace + and /.
             */
            private static final int DECODE_WEBSAFE[] = {
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1,
                    52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1,
                    -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
                    15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63,
                    -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
                    41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            };

            /**
             * Non-data values in the DECODE arrays.
             */
            private static final int SKIP = -1;
            private static final int EQUALS = -2;

            /**
             * States 0-3 are reading through the next input tuple.
             * State 4 is having read one '=' and expecting exactly
             * one more.
             * State 5 is expecting no more data or padding characters
             * in the input.
             * State 6 is the error state; an error has been detected
             * in the input and no future input can "fix" it.
             */
            private int state;   // state number (0 to 6)
            private int value;

            final private int[] alphabet;

            /**
             * Instantiates a new Decoder.
             *
             * @param flags  the flags
             * @param output the output
             */
            public Decoder(int flags, byte[] output) {
                this.output = output;

                alphabet = ((flags & URL_SAFE) == 0) ? DECODE : DECODE_WEBSAFE;
                state = 0;
                value = 0;
            }

            /**
             * @return an overestimate for the number of bytes {@code
             * len} bytes could decode to.
             */
            public int maxOutputSize(int len) {
                return len * 3 / 4 + 10;
            }

            /**
             * Decode another block of input data.
             *
             * @return true if the state machine is still healthy.  false if
             * bad base-64 data has been detected in the input stream.
             */
            public boolean process(byte[] input, int offset, int len, boolean finish) {
                if (this.state == 6) return false;

                int p = offset;
                len += offset;

                // Using local variables makes the decoder about 12%
                // faster than if we manipulate the member variables in
                // the loop.  (Even alphabet makes a measurable
                // difference, which is somewhat surprising to me since
                // the member variable is final.)
                int state = this.state;
                int value = this.value;
                int op = 0;
                final byte[] output = this.output;
                final int[] alphabet = this.alphabet;

                while (p < len) {
                    // Try the fast path:  we're starting a new tuple and the
                    // next four bytes of the input stream are all data
                    // bytes.  This corresponds to going through states
                    // 0-1-2-3-0.  We expect to use this method for most of
                    // the data.
                    //
                    // If any of the next four bytes of input are non-data
                    // (whitespace, etc.), value will end up negative.  (All
                    // the non-data values in decode are small negative
                    // numbers, so shifting any of them up and or'ing them
                    // together will result in a value with its top bit set.)
                    //
                    // You can remove this whole block and the output should
                    // be the same, just slower.
                    if (state == 0) {
                        while (p + 4 <= len &&
                                (value = ((alphabet[input[p] & 0xff] << 18) |
                                        (alphabet[input[p + 1] & 0xff] << 12) |
                                        (alphabet[input[p + 2] & 0xff] << 6) |
                                        (alphabet[input[p + 3] & 0xff]))) >= 0) {
                            output[op + 2] = (byte) value;
                            output[op + 1] = (byte) (value >> 8);
                            output[op] = (byte) (value >> 16);
                            op += 3;
                            p += 4;
                        }
                        if (p >= len) break;
                    }

                    // The fast path isn't available -- either we've read a
                    // partial tuple, or the next four input bytes aren't all
                    // data, or whatever.  Fall back to the slower state
                    // machine implementation.

                    int d = alphabet[input[p++] & 0xff];

                    switch (state) {
                        case 0:
                            if (d >= 0) {
                                value = d;
                                ++state;
                            } else if (d != SKIP) {
                                this.state = 6;
                                return false;
                            }
                            break;

                        case 1:
                            if (d >= 0) {
                                value = (value << 6) | d;
                                ++state;
                            } else if (d != SKIP) {
                                this.state = 6;
                                return false;
                            }
                            break;

                        case 2:
                            if (d >= 0) {
                                value = (value << 6) | d;
                                ++state;
                            } else if (d == EQUALS) {
                                // Emit the last (partial) output tuple;
                                // expect exactly one more padding character.
                                output[op++] = (byte) (value >> 4);
                                state = 4;
                            } else if (d != SKIP) {
                                this.state = 6;
                                return false;
                            }
                            break;

                        case 3:
                            if (d >= 0) {
                                // Emit the output triple and return to state 0.
                                value = (value << 6) | d;
                                output[op + 2] = (byte) value;
                                output[op + 1] = (byte) (value >> 8);
                                output[op] = (byte) (value >> 16);
                                op += 3;
                                state = 0;
                            } else if (d == EQUALS) {
                                // Emit the last (partial) output tuple;
                                // expect no further data or padding characters.
                                output[op + 1] = (byte) (value >> 2);
                                output[op] = (byte) (value >> 10);
                                op += 2;
                                state = 5;
                            } else if (d != SKIP) {
                                this.state = 6;
                                return false;
                            }
                            break;

                        case 4:
                            if (d == EQUALS) {
                                ++state;
                            } else if (d != SKIP) {
                                this.state = 6;
                                return false;
                            }
                            break;

                        case 5:
                            if (d != SKIP) {
                                this.state = 6;
                                return false;
                            }
                            break;
                    }
                }

                if (!finish) {
                    // We're out of input, but a future call could provide
                    // more.
                    this.state = state;
                    this.value = value;
                    this.op = op;
                    return true;
                }

                // Done reading input.  Now figure out where we are left in
                // the state machine and finish up.

                switch (state) {
                    case 0:
                        // Output length is a multiple of three.  Fine.
                        break;
                    case 1:
                        // Read one extra input byte, which isn't enough to
                        // make another output byte.  Illegal.
                        this.state = 6;
                        return false;
                    case 2:
                        // Read two extra input bytes, enough to emit 1 more
                        // output byte.  Fine.
                        output[op++] = (byte) (value >> 4);
                        break;
                    case 3:
                        // Read three extra input bytes, enough to emit 2 more
                        // output bytes.  Fine.
                        output[op++] = (byte) (value >> 10);
                        output[op++] = (byte) (value >> 2);
                        break;
                    case 4:
                        // Read one padding '=' when we expected 2.  Illegal.
                        this.state = 6;
                        return false;
                    case 5:
                        // Read all the padding '='s we expected and no more.
                        // Fine.
                        break;
                }

                this.state = state;
                this.op = op;
                return true;
            }
        }

        //  --------------------------------------------------------
        //  encoding
        //  --------------------------------------------------------

        /**
         * Base64Util-encode the given data and return a newly allocated
         * String with the result.
         *
         * @param input the data to encode
         * @param flags controls certain features of the encoded output.               Passing {@code
         *              DEFAULT} results in output that               adheres to RFC 2045.
         * @return the string
         */
        public static String encodeToString(byte[] input, int flags) {
            try {
                return new String(encode(input, flags), "US-ASCII");
            } catch (UnsupportedEncodingException e) {
                // US-ASCII is guaranteed to be available.
                throw new AssertionError(e);
            }
        }

        /**
         * Base64Util-encode the given data and return a newly allocated
         * String with the result.
         *
         * @param input  the data to encode
         * @param offset the position within the input array at which to               start
         * @param len    the number of bytes of input to encode
         * @param flags  controls certain features of the encoded output.               Passing {@code
         *               DEFAULT} results in output that               adheres to RFC 2045.
         * @return the string
         */
        public static String encodeToString(byte[] input, int offset, int len, int flags) {
            try {
                return new String(encode(input, offset, len, flags), "US-ASCII");
            } catch (UnsupportedEncodingException e) {
                // US-ASCII is guaranteed to be available.
                throw new AssertionError(e);
            }
        }

        /**
         * Base64Util-encode the given data and return a newly allocated
         * byte[] with the result.
         *
         * @param input the data to encode
         * @param flags controls certain features of the encoded output.               Passing {@code
         *              DEFAULT} results in output that               adheres to RFC 2045.
         * @return the byte [ ]
         */
        public static byte[] encode(byte[] input, int flags) {
            return encode(input, 0, input.length, flags);
        }

        /**
         * Base64Util-encode the given data and return a newly allocated
         * byte[] with the result.
         *
         * @param input  the data to encode
         * @param offset the position within the input array at which to               start
         * @param len    the number of bytes of input to encode
         * @param flags  controls certain features of the encoded output.               Passing {@code
         *               DEFAULT} results in output that               adheres to RFC 2045.
         * @return the byte [ ]
         */
        public static byte[] encode(byte[] input, int offset, int len, int flags) {
            Encoder encoder = new Encoder(flags, null);

            // Compute the exact length of the array we will produce.
            int output_len = len / 3 * 4;

            // Account for the tail of the data and the padding bytes, if any.
            if (encoder.do_padding) {
                if (len % 3 > 0) {
                    output_len += 4;
                }
            } else {
                switch (len % 3) {
                    case 0:
                        break;
                    case 1:
                        output_len += 2;
                        break;
                    case 2:
                        output_len += 3;
                        break;
                }
            }

            // Account for the newlines, if any.
            if (encoder.do_newline && len > 0) {
                output_len += (((len - 1) / (3 * Encoder.LINE_GROUPS)) + 1) *
                        (encoder.do_cr ? 2 : 1);
            }

            encoder.output = new byte[output_len];
            encoder.process(input, offset, len, true);

            assert encoder.op == output_len;

            return encoder.output;
        }

        /**
         * The type Encoder.
         */
        /* package */ static class Encoder extends Coder {
            /**
             * Emit a new line every this many output tuples.  Corresponds to
             * a 76-character line length (the maximum allowable according to
             * <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>).
             */
            public static final int LINE_GROUPS = 19;

            /**
             * Lookup table for turning Base64Util alphabet positions (6 bits)
             * into output bytes.
             */
            private static final byte ENCODE[] = {
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                    'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/',
            };

            /**
             * Lookup table for turning Base64Util alphabet positions (6 bits)
             * into output bytes.
             */
            private static final byte ENCODE_WEBSAFE[] = {
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
                    'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
                    'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_',
            };

            final private byte[] tail;
            /**
             * The Tail len.
             */
            /* package */ int tailLen;
            private int count;

            /**
             * The Do padding.
             */
            final public boolean do_padding;
            /**
             * The Do newline.
             */
            final public boolean do_newline;
            /**
             * The Do cr.
             */
            final public boolean do_cr;
            final private byte[] alphabet;

            /**
             * Instantiates a new Encoder.
             *
             * @param flags  the flags
             * @param output the output
             */
            public Encoder(int flags, byte[] output) {
                this.output = output;

                do_padding = (flags & NO_PADDING) == 0;
                do_newline = (flags & NO_WRAP) == 0;
                do_cr = (flags & CRLF) != 0;
                alphabet = ((flags & URL_SAFE) == 0) ? ENCODE : ENCODE_WEBSAFE;

                tail = new byte[2];
                tailLen = 0;

                count = do_newline ? LINE_GROUPS : -1;
            }

            /**
             * @return an overestimate for the number of bytes {@code
             * len} bytes could encode to.
             */
            public int maxOutputSize(int len) {
                return len * 8 / 5 + 10;
            }

            public boolean process(byte[] input, int offset, int len, boolean finish) {
                // Using local variables makes the encoder about 9% faster.
                final byte[] alphabet = this.alphabet;
                final byte[] output = this.output;
                int op = 0;
                int count = this.count;

                int p = offset;
                len += offset;
                int v = -1;

                // First we need to concatenate the tail of the previous call
                // with any input bytes available now and see if we can empty
                // the tail.

                switch (tailLen) {
                    case 0:
                        // There was no tail.
                        break;

                    case 1:
                        if (p + 2 <= len) {
                            // A 1-byte tail with at least 2 bytes of
                            // input available now.
                            v = ((tail[0] & 0xff) << 16) |
                                    ((input[p++] & 0xff) << 8) |
                                    (input[p++] & 0xff);
                            tailLen = 0;
                        }
                        break;

                    case 2:
                        if (p + 1 <= len) {
                            // A 2-byte tail with at least 1 byte of input.
                            v = ((tail[0] & 0xff) << 16) |
                                    ((tail[1] & 0xff) << 8) |
                                    (input[p++] & 0xff);
                            tailLen = 0;
                        }
                        break;
                }

                if (v != -1) {
                    output[op++] = alphabet[(v >> 18) & 0x3f];
                    output[op++] = alphabet[(v >> 12) & 0x3f];
                    output[op++] = alphabet[(v >> 6) & 0x3f];
                    output[op++] = alphabet[v & 0x3f];
                    if (--count == 0) {
                        if (do_cr) output[op++] = '\r';
                        output[op++] = '\n';
                        count = LINE_GROUPS;
                    }
                }

                // At this point either there is no tail, or there are fewer
                // than 3 bytes of input available.

                // The main loop, turning 3 input bytes into 4 output bytes on
                // each iteration.
                while (p + 3 <= len) {
                    v = ((input[p] & 0xff) << 16) |
                            ((input[p + 1] & 0xff) << 8) |
                            (input[p + 2] & 0xff);
                    output[op] = alphabet[(v >> 18) & 0x3f];
                    output[op + 1] = alphabet[(v >> 12) & 0x3f];
                    output[op + 2] = alphabet[(v >> 6) & 0x3f];
                    output[op + 3] = alphabet[v & 0x3f];
                    p += 3;
                    op += 4;
                    if (--count == 0) {
                        if (do_cr) output[op++] = '\r';
                        output[op++] = '\n';
                        count = LINE_GROUPS;
                    }
                }

                if (finish) {
                    // Finish up the tail of the input.  Note that we need to
                    // consume any bytes in tail before any bytes
                    // remaining in input; there should be at most two bytes
                    // total.

                    if (p - tailLen == len - 1) {
                        int t = 0;
                        v = ((tailLen > 0 ? tail[t++] : input[p++]) & 0xff) << 4;
                        tailLen -= t;
                        output[op++] = alphabet[(v >> 6) & 0x3f];
                        output[op++] = alphabet[v & 0x3f];
                        if (do_padding) {
                            output[op++] = '=';
                            output[op++] = '=';
                        }
                        if (do_newline) {
                            if (do_cr) output[op++] = '\r';
                            output[op++] = '\n';
                        }
                    } else if (p - tailLen == len - 2) {
                        int t = 0;
                        v = (((tailLen > 1 ? tail[t++] : input[p++]) & 0xff) << 10) |
                                (((tailLen > 0 ? tail[t++] : input[p++]) & 0xff) << 2);
                        tailLen -= t;
                        output[op++] = alphabet[(v >> 12) & 0x3f];
                        output[op++] = alphabet[(v >> 6) & 0x3f];
                        output[op++] = alphabet[v & 0x3f];
                        if (do_padding) {
                            output[op++] = '=';
                        }
                        if (do_newline) {
                            if (do_cr) output[op++] = '\r';
                            output[op++] = '\n';
                        }
                    } else if (do_newline && op > 0 && count != LINE_GROUPS) {
                        if (do_cr) output[op++] = '\r';
                        output[op++] = '\n';
                    }

                    assert tailLen == 0;
                    assert p == len;
                } else {
                    // Save the leftovers in tail to be consumed on the next
                    // call to encodeInternal.

                    if (p == len - 1) {
                        tail[tailLen++] = input[p];
                    } else if (p == len - 2) {
                        tail[tailLen++] = input[p];
                        tail[tailLen++] = input[p + 1];
                    }
                }

                this.op = op;
                this.count = count;

                return true;
            }
        }

        /**
         * bitmap转为base64
         */
        public static String bitmapToBase64(Bitmap bitmap) {

            String result = null;
            ByteArrayOutputStream baos = null;
            try {
                if (bitmap != null) {
                    baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    baos.flush();
                    baos.close();

                    byte[] bitmapBytes = baos.toByteArray();
                    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (baos != null) {
                        baos.flush();
                        baos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        /**
         * base64转为bitmap
         */
        public static Bitmap base64ToBitmap(String base64Data) {
            byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        private Base64Util() {
        }   // don't instantiate
    }
}
