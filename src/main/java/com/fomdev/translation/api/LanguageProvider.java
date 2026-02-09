package com.fomdev.translation.api;

import java.util.Map;

public interface LanguageProvider {
    Map<String, String> getTranslation();
}