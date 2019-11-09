package com.vbermudez.floatingwidgets.service;

import android.content.Context;
import android.content.res.Resources;
import android.util.Pair;
import android.widget.ScrollView;

import com.vbermudez.floatingwidgets.R;
import com.vbermudez.floatingwidgets.data.Word;
import com.vbermudez.floatingwidgets.exceptions.InitialNotSupportedException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class WordBrain {
    private static final String csvFileEnding = "_words_dict";
    private LinkedList<Word> words;
    private HashMap<String, String> initialTranslation;
    private final Set<String> acceptedInitials;
    private Resources resources;
    private Context context;

    public WordBrain(Resources resources, Context context) {
        assert resources != null;
        assert context != null;
        this.resources = resources;
        this.context = context;

        initialTranslation = new HashMap<>();
        initialTranslation.put("a", null);
        initialTranslation.put("b", null);
        initialTranslation.put("c", null);
        initialTranslation.put("d", null);
        initialTranslation.put("e", null);
        initialTranslation.put("f", null);
        initialTranslation.put("g", null);
        initialTranslation.put("h", null);
        initialTranslation.put("j", null);
        initialTranslation.put("k", null);
        initialTranslation.put("l", null);
        initialTranslation.put("m", null);
        initialTranslation.put("n", null);
        initialTranslation.put("o", null);
        initialTranslation.put("p", null);
        initialTranslation.put("q", null);
        initialTranslation.put("r", null);
        initialTranslation.put("s", null);
        initialTranslation.put("t", null);
        initialTranslation.put("u", null);
        initialTranslation.put("v", null);
        initialTranslation.put("w", null);
        initialTranslation.put("x", null);
        initialTranslation.put("y", null);
        initialTranslation.put("z", null);
        initialTranslation.put("zero", null);
        initialTranslation.put("one", null);
        initialTranslation.put("two", null);
        initialTranslation.put("three", null);
        initialTranslation.put("four", null);
        initialTranslation.put("five", null);
        initialTranslation.put("six", null);
        initialTranslation.put("seven", null);
        initialTranslation.put("eight", null);
        initialTranslation.put("nine", null);
        initialTranslation.put("-", "dash");
        initialTranslation.put("_", "longdash");

        Set<String> keys = initialTranslation.keySet();
        Object[] keysObjArr = keys.toArray();
        String[] keysArr = Arrays.copyOf(keysObjArr, keysObjArr.length, String[].class);
        acceptedInitials = new HashSet<String>(Arrays.asList(keysArr));
    }

    public List<Word> loadSearchTerm(String term) throws InitialNotSupportedException {
        term = term.trim();
        String initial = String.valueOf(term.charAt(0));
        loadPackage(initial);
        return searchDefinition(term);
    }

    public List<Word> searchDefinition(String word) {
        // indices por ultima palabra buscada
        List<Word> definitions = new LinkedList<>();
        for(Word w : words) {
            if(w.getWord().toLowerCase().equals(word.toLowerCase())) {
                definitions.add(w);
            }
        }
        return definitions;
    }

    public void loadPackage(String letterInitial) throws InitialNotSupportedException {
        String letter = letterInitial.toLowerCase();
        if(!acceptedInitials.contains(letter)) {
            // TODO: Change throw method to conventional standards.
            throw new InitialNotSupportedException();
        }

        words = new LinkedList<>();
        int identifier = resources.getIdentifier(letter + csvFileEnding, "raw", context.getPackageName());
        InputStream in_s = resources.openRawResource(identifier);
        try (InputStream un_gz = new GZIPInputStream(in_s)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(un_gz));
            // Skip CSV headers
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                String tokWord = tokens[0];
                String tokLang = tokens[1];
                String[] tmpDefinitions = tokens[2]
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .split(",(?=(?:[^\']*\'[^\']*\')*[^\']*$)");
                LinkedList<String> tokDefinitions = new LinkedList<String>();
                for(int i = 0; i < tmpDefinitions.length; i++) {
                    // /'def1, def2'/ > /def1, def2/
                    String tempDefinition = tmpDefinitions[i].replace("'", "").trim();
                    tokDefinitions.add(tempDefinition);
                }
                String[] tokTags = tokens[3]
                        .replace("[", "")
                        .replace("]", "")
                        .replace("'", "")
                        .split(",");
                if(tokTags[0].equals("") && tokTags.length == 1) {
                    tokTags = null;
                } else {
                    for(int i = 0; i < tokTags.length; i++) {
                        tokTags[i] = tokTags[i].trim();
                    }
                }
                Object[] tokDefinitionsArr = tokDefinitions.toArray();
                Word word = new Word();
                word.setWord(tokWord);
                word.setLanguage(tokLang);
                word.setDefinitions(Arrays.copyOf(tokDefinitionsArr, tokDefinitionsArr.length, String[].class));
                word.setTags(tokTags);
                words.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Word> x = this.searchDefinition("action");
    }


}
