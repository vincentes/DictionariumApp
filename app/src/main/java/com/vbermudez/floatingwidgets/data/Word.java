package com.vbermudez.floatingwidgets.data;

import java.util.Arrays;
import java.util.Objects;

public class Word {
    public String word;
    private String language;
    public String[] definitions;
    public String[] tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(word, word1.word) &&
                Arrays.equals(tags, word1.tags);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(word, language);
        result = 31 * result + Arrays.hashCode(definitions);
        return result;
    }

    public String getFormattedDefinitions() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < definitions.length; i++) {
            String newLine = "";
            if(i == definitions.length - 1) {
                newLine = (i + 1) + ". " + definitions[i];
            } else {
                newLine = (i + 1) + ". " + definitions[i] + "\n\n";
            }
            builder.append(newLine);
        }
        return builder.toString();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String[] getDefinitions() {
        return definitions;
    }

    public void setDefinitions(String[] definitions) {
        this.definitions = definitions;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
