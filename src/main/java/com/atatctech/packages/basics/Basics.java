package com.atatctech.packages.basics;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Basics {
    public static class NativeHandler {
        @Contract("_ -> new")
        public static @NotNull String readFile(String filename) throws IOException {
            return readFile(filename, true);
        }

        public static @NotNull String readFile(String filename, boolean convertLineSeparator) throws IOException {
            return readFile(new File(filename), convertLineSeparator);
        }

        @Contract("_, _ -> new")
        public static @NotNull String readFile(@NotNull File file, boolean convertLineSeparator) throws IOException {
            String r = new String(Files.readAllBytes(file.toPath()));
            return convertLineSeparator ? r.replaceAll("\r\n", "\n") : r;
        }

        @Contract("_ -> new")
        public static @NotNull String readFile(@NotNull File file) throws IOException {
            return readFile(file, true);
        }

        public static boolean writeFile(String filename, String content, boolean append) {
            try (FileWriter fileWriter = new FileWriter(filename, append)) {
                fileWriter.write(content);
                return true;
            } catch (IOException ignored) {
                return false;
            }
        }

        public static boolean writeFile(String filename, String content) {
            return writeFile(filename, content, false);
        }

        public static boolean writeFile(File file, String content, boolean append) {
            try (FileWriter fileWriter = new FileWriter(file, append)) {
                fileWriter.write(content);
                return true;
            } catch (IOException ignored) {
                return false;
            }
        }

        public static boolean writeFile(File file, String content) {
            return writeFile(file, content, false);
        }
    }
    public static class Conversion {
        public static String BufferedImage2Base64(BufferedImage bufferedImage) throws IOException {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpeg", stream);
            String base64 = Base64.getEncoder().encodeToString(stream.toByteArray());
            stream.flush();
            stream.close();
            return base64;
        }

        public static String @NotNull [] stringArrayList2stringArray(@NotNull ArrayList<String> arrayList) {
            String[] res = new String[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++) {
                res[i] = arrayList.get(i);
            }
            return res;
        }

        public static <T> List<T> castList(Object obj, Class<T> clazz) {
            List<T> result = new ArrayList<>();
            if (obj instanceof List<?>) {
                for (Object o : (List<?>) obj) {
                    result.add(clazz.cast(o));
                }
                return result;
            }
            return null;
        }
    }

    public static class TextClass {
        public static @NotNull String stringToUnicode(@NotNull String unicode) {
            char[] chars = unicode.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char aChar : chars) {
                if (aChar <= 127) {
                    builder.append(aChar);
                } else {
                    builder.append("\\u");
                    String hx = Integer.toString(aChar, 16);
                    if (hx.length() < 4) {
                        builder.append("0000".substring(hx.length())).append(hx);
                    } else {
                        builder.append(hx);
                    }
                }

            }
            return builder.toString();
        }

        public static @NotNull String unicodeToString(@NotNull String s) {
            String[] split = s.split("\\\\");

            StringBuilder builder = new StringBuilder();

            for (String string : split) {

                if (string.startsWith("u")) {
                    builder.append((char) Integer.parseInt(string.substring(1, 5), 16));
                    if (string.length() > 5) {
                        builder.append(string.substring(5));
                    }
                } else {
                    builder.append(string);
                }

            }
            return builder.toString();
        }

        public static boolean isBase64(String str) {
            return Pattern.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$", str);
        }

        public static @NotNull String chinese2spell(String chinese) {
            if (chinese == null || chinese.isEmpty()) {
                return "";
            }
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            format.setVCharType(HanyuPinyinVCharType.WITH_V);
            char[] input = chinese.trim().toCharArray();
            StringBuilder output = new StringBuilder();
            try {
                for (char c : input) {
                    if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                        String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                        if (temp != null && temp.length != 0) {
                            output.append(temp[0]);
                        }
                    } else if (Character.isAlphabetic(c)) {
                        output.append(Character.toLowerCase(c));
                    }
                }
            } catch (BadHanyuPinyinOutputFormatCombination ignored) {
                return "";
            }
            return output.toString();
        }

        public static float similarity(String a, String b) {
            if (a == null || b == null) {
                return 0f;
            }
            if (a.length() != b.length()) {
                return 0f;
            }

            int disCount = 0;
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    disCount++;
                }
            }
            return (float) disCount / (float) a.length();
        }

        public static float matchingSimilarity(String content, String keyword) {
            if (content == null || keyword == null) {
                return 0;
            }
            int matchingCases = 0;
            int totalCases = 0;
            for (int i = 0; i < keyword.length(); i++) {
                for (int j = 0; j < keyword.length() - i; j++) {
                    totalCases++;
                    if (content.contains(keyword.substring(j, j + i))) {
                        matchingCases++;
                    }
                }
            }

            return (float) matchingCases / totalCases;
        }

        public static String getSHA256(@NotNull String str) {
            MessageDigest messageDigest;
            String shaString;
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
                shaString = byte2hex(messageDigest.digest());
            } catch (NoSuchAlgorithmException ignored) {
                return "";
            }
            return shaString;
        }

        public static String getMD5(@NotNull String str) {
            MessageDigest messageDigest;
            String md5String;
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
                md5String = byte2hex(messageDigest.digest());
            } catch (NoSuchAlgorithmException ignored) {
                return "";
            }
            return md5String;
        }

        private static @NotNull String byte2hex(byte @NotNull [] bytes) {
            StringBuilder stringBuilder = new StringBuilder();
            String temp;
            for (byte aByte : bytes) {
                temp = Integer.toHexString(aByte & 0xFF);
                if (temp.length() == 1) {
                    stringBuilder.append("0");
                }
                stringBuilder.append(temp);
            }
            return stringBuilder.toString();
        }

        public static String[] split(@NotNull String content, String separator) {
            if (content.isEmpty()) return new String[]{""};
            LinkedList<String> res = new LinkedList<>();
            int from_index = 0;
            int target_len = separator.length();
            int content_len = content.length();
            for (int i = 0; i < content_len - target_len + 1; i++) {
                if (content.substring(i, i + target_len).equals(separator)) {
                    res.addLast(content.substring(from_index, i));
                    from_index = i + target_len;
                }
            }
            if (from_index != content_len) {
                res.addLast(content.substring(from_index));
            }

            return res.toArray(String[]::new);
        }

        public static String[] split(@NotNull String content, char separator) {
            LinkedList<String> res = new LinkedList<>();
            int from_index = 0;
            int content_len = content.length();
            for (int i = 0; i < content_len; i++) {
                if (content.charAt(i) == separator) {
                    res.addLast(content.substring(from_index, i));
                    from_index = i + 1;
                }
            }
            if (from_index != content_len) {
                res.addLast(content.substring(from_index));
            }

            return res.toArray(String[]::new);
        }
    }
}
