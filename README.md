# Translation
A plugin which fixes the problem that the original Bukkit api cannot localize languages or requires complex steps.


**IMPORTANT NOTICE**


Requires dependency 'SpigotASM' for collection @Translatable annotation data


**USAGES**


*In Game*


    - /language disable -> disable the given language (cannot be the current language)
    - /language enable  -> enable the given language
    - /language reload  -> reloads the language cache (requires SpigotASM)
    - /language switch  -> switches the current language into given (must exist)


*Java*
- How to add languages

*Example*


    @Translatable(lang="ex_pl") /* Should pass in the language the class is in. Default is "en_us" */
    class ExampleLang implements LanguageProvider /* REMEMBER THIS STEP, if the LanguageProvider isn't implemented, this will cause ClassCastException which will lead to the crash of the plugin */
    {
      @Override
      public Map<String, String> getTranslation() 
      {
          /* ... */
      }
    }



Here is a built-in api for building the language dependency map from the given json-like code

LangUtil.buildDictionaryFromLines(Map<String, String>, String... json) ('tile.eg.translation': '...')


*WARNING*
If you wanna register a brand new language, which you are sure that plugins won't register that, you should remember to add 'language':'..name..' at the very beginning of the file (which ..name.. represents to its real name, language represents its language id) in order to made the translation of the language.


Notice: there are three languages built-in inside this plugin: zh_cn (Chinese), en_us(English, default on every load), ne_ko(???)
