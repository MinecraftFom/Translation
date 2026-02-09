package com.fomdev.translation.api;

import com.fomdev.sasm.api.PluginClassUtil;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class LangUtil {
    private static String current;
    private static Map<String, Map<String, String>> dictionary;

    public static Map<String, String> buildDictionary(Map<String, String> original, String json) {
        String[] values = json.split(",");
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].strip();
        }

        return buildDictionaryFromLines(original, values);
    }

    public static Map<String, String> buildDictionaryFromLines(Map<String, String> original, String... jsons) {
        for (String l: jsons) {
            String[] jparts = l.split(":");
            if (jparts.length != 2) {
                return original;
            }

            jparts[0] = jparts[0].strip();
            jparts[1] = jparts[1].strip();
            jparts[0] = jparts[0].replaceAll("'", "");
            jparts[1] = jparts[1].replaceAll("'", "");
            original.put(jparts[0], jparts[1]);
        }

        return original;
    }

    public static String getTranslation(String key, String... language) {
        String lang = language.length != 1? current: language[0];
        if (!dictionary.containsKey(lang) || !dictionary.get(lang).containsKey(key)) {
            return key;
        }
        return dictionary.get(lang).get(key);
    }

    public static void applyDictionary(LanguageProvider provider, String lang) {
        if (!hasLanguage(lang)) {
            dictionary.put(lang, new HashMap<>());

        }
        dictionary.get(lang).putAll(provider.getTranslation());
    }

    private static void applyDictionary(LanguageProvider provider) {
        if (provider.getClass().getAnnotation(Translatable.class) == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Caught A Broken Lang File At Class: " + provider.getClass().getName());
            return;
        }
        String lang = provider.getClass().getAnnotation(Translatable.class).lang() == null? "en_us": provider.getClass().getAnnotation(Translatable.class).lang();
        applyDictionary(provider, lang);
    }

    public static void syncDictionaryFromPackages() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Class<?>> classes = PluginClassUtil.getAllMatch(Translatable.class);
        for (Class<?> c: classes) {
            applyDictionary((LanguageProvider) c.getConstructor().newInstance());
            Bukkit.getLogger().info("Registered a language class of language @0 at @1"
                    .replace("@0", c.getAnnotation(Translatable.class).lang())
                    .replace("@1", c.getName()));
        }
    }

    public static Set<String> getAvailableLanguages() {
        return dictionary.keySet();
    }

    public static boolean hasLanguage(String lang) {
        return dictionary.containsKey(lang);
    }

    public static void setLanguage(String lang) {
        current = lang;
    }

    public static String getCurrentLanguage() {
        return current;
    }

    static {
        current    = "en_us"; // Default in startup
        dictionary = new HashMap<>();
    }
}