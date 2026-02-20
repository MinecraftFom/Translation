package com.fomdev.translation.api;

import com.fomdev.dbs.api.JsonFileCache;
import com.fomdev.dbs.api.YamlFileCache;
import com.fomdev.sasm.api.PluginClassUtil;
import com.fomdev.translation.event.TranslationEvent;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LangUtil {
    private static String current;
    private static Map<String, Map<String, String>> dictionary;

    public static void applyDictionary(LanguageProvider provider, String lang) {
        if (!hasLanguage(lang)) {
            dictionary.put(lang, new HashMap<>());

        }
        dictionary.get(lang).putAll(provider.getTranslation());
    }

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

    public static Set<String> getAvailableLanguages() {
        return dictionary.keySet();
    }

    public static String getCurrentLanguage() {
        return current;
    }

    public static String getTranslation(String key, String... language) {
        String lang = language.length != 1? current: language[0];
        if (!dictionary.containsKey(lang) || !dictionary.get(lang).containsKey(key)) {
            return key;
        }
        return dictionary.get(lang).get(key);
    }

    public static boolean hasLanguage(String lang) {
        return dictionary.containsKey(lang);
    }

    public static void initLanguage() {
        YamlFileCache cache = new YamlFileCache(".", "lang");
        current = cache.get("lang") == null? "en_us": (String) cache.get("lang");
        saveLanguage();
    }

    public static void setLanguage(String lang) {
        String original = current;
        current = lang;
        saveLanguage();
        Bukkit.getPluginManager().callEvent(new TranslationEvent(original, lang));
    }

    public static void syncDictionaryFromFolder() {
        Path languages = Paths.get("languages");

        if (!Files.exists(languages)) {
            try {
                Files.createDirectory(languages);
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.WARNING, e.getMessage() + e.getCause());
            }
        }

        try {
            File[] files = new File(languages.toUri()).listFiles();
            for (File f : files) {
                if (f.isDirectory()) {
                    File[] langs = f.listFiles();
                    for (File l: langs) {
                        if (!l.getName().endsWith(".json")) {
                            Bukkit.getLogger().warning("Caught an invalid unrecognizable file in language pack");
                        }

                        JsonFileCache cache = new JsonFileCache(l);
                        Object lo = cache.get("lang");

                        if (lo == null) {
                            Bukkit.getLogger().warning("Caught an broken or invalid file in language pack");
                        }

                        String lang = (String) lo;

                        if (!hasLanguage(lang)) {
                            dictionary.put(lang, new HashMap<>());
                        }

                        Map<String, String> dict = new HashMap<>();
                        for (String k: cache.get().keySet()) {
                            dict.put(k, dict.get(k));
                        }

                        dictionary.get(lang).putAll(dict);
                        Bukkit.getLogger().info("Registered a language file of language @0 at @1"
                                .replace("@0", lang)
                                .replace("@1", f.getName())
                        );
                    }
                } else {
                    Bukkit.getLogger().warning("Caught an invalid unrecognizable file in language path");
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, e.getMessage() + e.getCause());
        }
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

    private static void applyDictionary(LanguageProvider provider) {
        if (provider.getClass().getAnnotation(Translatable.class) == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Caught A Broken Lang File At Class: " + provider.getClass().getName());
            return;
        }
        String lang = provider.getClass().getAnnotation(Translatable.class).lang() == null? "en_us": provider.getClass().getAnnotation(Translatable.class).lang();
        applyDictionary(provider, lang);
    }

    private static void saveLanguage() {
        YamlFileCache cache = new YamlFileCache(".", "lang");
        cache.append("lang", current);
        cache.write();
    }

    static {
        dictionary = new HashMap<>();
    }
}