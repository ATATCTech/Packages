package com.atatctech.packages.data;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public final class DataCheck {
    public static <E> boolean lengthCheck(@Nullable List<E> list, int minLength, int maxLength) {
        return list != null && list.size() >= minLength && list.size() <= maxLength;
    }

    public static <E> boolean lengthCheck(@Nullable Set<E> set, int minLength, int maxLength) {
        return set != null && set.size() >= minLength && set.size() <= maxLength;
    }

    public static <E> boolean lengthCheck(@Nullable List<E> list, int maxLength) {
        return lengthCheck(list, 0, maxLength);
    }

    public static <E> boolean lengthCheck(@Nullable Set<E> set, int maxLength) {
        return lengthCheck(set, 0, maxLength);
    }

    public static boolean lengthCheck(byte @Nullable [] bytes, int minLength, int maxLength) {
        return bytes != null && bytes.length >= minLength && bytes.length <= maxLength;
    }

    public static boolean lengthCheck(byte[] bytes, int maxLength) {
        return lengthCheck(bytes, 0, maxLength);
    }

    public static boolean lengthCheck(@Nullable String string, int minLength, int maxLength) {
        return string != null && string.length() >= minLength && string.length() <= maxLength;
    }

    public static boolean lengthCheck(@Nullable String string, int maxLength) {
        return lengthCheck(string, 0, maxLength);
    }

    public static boolean emailCheck(@Nullable String email) {
        return lengthCheck(email, 5, 320) && email.matches("^[\\w!#$%&'*+\\-/=?^`{|}~]+(\\.[\\w!#$%&'*+\\-/=?^`{|}~]+)*@[a-zA-Z0-9\\-]+(\\.[a-zA-Z0-9\\-]+)*\\.[a-zA-Z]+$");
    }

    public static boolean displayableCheck(@Nullable String string) {
        return string != null && string.matches("^[\\S ]*$");
    }

    public static boolean displayNameCheck(@Nullable String displayName, int minLength, int maxLength) {
        return lengthCheck(displayName, minLength, maxLength) && displayableCheck(displayName);
    }

    public static boolean displayNameCheck(@Nullable String displayName, int maxLength) {
        return displayNameCheck(displayName, 0, maxLength);
    }

    public static boolean displayNameCheck(@Nullable String displayName) {
        return displayNameCheck(displayName, 3, 36);
    }

    public static boolean nameCheck(@Nullable String name, int minLength, int maxLength) {
        return lengthCheck(name, minLength, maxLength) && name.matches("^[A-Za-z]+(-?[A-Za-z0-9]+)*$");
    }

    public static boolean nameCheck(@Nullable String name, int maxLength) {
        return nameCheck(name, 0, maxLength);
    }

    public static boolean nameCheck(@Nullable String name) {
        return nameCheck(name, 3, 36);
    }

    public static boolean urlCheck(@Nullable String url, @Nullable String prefix) {
        return prefix != null && lengthCheck(url, prefix.length() + 1, 8182) && url.matches("^" + prefix + "[\\w!#$%&'*+\\-/=?^`{|}~.]+$");
    }

    public static boolean urlCheck(@Nullable String url) {
        return urlCheck(url, "https://");
    }

    public static boolean mobileCheck(@Nullable String mobile) {
        return lengthCheck(mobile, 7, 16) && mobile.matches("^\\+?[0-9]+$");
    }
}
