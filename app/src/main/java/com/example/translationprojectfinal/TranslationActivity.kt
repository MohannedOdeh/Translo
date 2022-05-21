package com.example.translationprojectfinal

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.MutableLiveData
import android.widget.Spinner
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.android.synthetic.main.activity_translation.*
import com.google.mlkit.nl.translate.*
import kotlinx.android.synthetic.main.activity_ident_translat.*
import java.util.*

class TranslationActivity : AppCompatActivity() {
    private var langCode = ArrayList<String>()
    private var languageFinal =HashMap<String,String>()
    var languageCode2 = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translation)
        readWordsFiles()
        populateSpinner()
    }
    //Reads word files with language country codes and language name to place in hashmap
    private fun readWordsFiles(){
        val reader = Scanner(resources.openRawResource(R.raw.lang3))
        while(reader.hasNextLine()){
            val line = reader.nextLine()
            Log.d("WordFile","The line is $line")
            val parts = line.split("*")
            langCode.add(parts[0])
            languageFinal.put(parts[1],parts[0])
        }
        var targetCode = languageFinal["English"]
        Log.d(TAG, "TArget Code = " +targetCode)
    }
    /*
    Populates the two spinners. They are both recieved from an array that is setup in strings in resource file
     */
    fun populateSpinner()
    {
        val spinner2: Spinner = findViewById(R.id.targetLang)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.lang,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner2.adapter = adapter
        }

    }
    /*
    Translate function main use is api
     */
    fun translate(v: View)
    {


        val text = etEnterText.text.toString() //Takes the inputed text
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(text) //Inputs the text that was recieved from line 51 into the api
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {
                    Log.i(TAG, "Can't identify language.")
                } else {
                    Log.i(TAG, "Language: $languageCode")
                    languageCode2 = languageCode
                    Log.d(TAG, "Language2: $languageCode2")


                }
            }
            .addOnFailureListener {
                // Model couldn’t be loaded or other internal error.
                // ...
            }
        Log.d(TAG, "Language2: $languageCode2")

        val spinner2: Spinner = findViewById(R.id.targetLang)
        val targetLangCode = spinner2.selectedItem.toString() //Recieves the selected target Langauge from spinner
        val target = languageFinal[targetLangCode] //Converts the selected plaintext language to the country code
        Log.d(TAG, "TArget Code = " +target)
       val options = TranslatorOptions.Builder() //Builds the API with selected source and target
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(languageCode2!!).toString())
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(target!!).toString())
            .build()
        val englishGermanTranslator = Translation.getClient(options)
        val modelManager = RemoteModelManager.getInstance() //Builds the model manager to download languages
        val model = TranslateRemoteModel.Builder(TranslateLanguage.fromLanguageTag(target)!!).build()
        modelManager.download(model, DownloadConditions.Builder().build())

        /*
        Downloads the model from the built remotedownloader above
         */
        var conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        englishGermanTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                // Model downloaded successfully. Okay to start translating.
                // (Set a flag, unhide the translation UI, etc.)
            }
            .addOnFailureListener { exception ->
                // Model couldn’t be downloaded or other internal error.
                // ...
            }


        //Translates the inputed text that was recieved from line
        englishGermanTranslator.translate(text)
            .addOnSuccessListener { translatedText ->
                textView.text = translatedText
                Log.d(TAG, "Correctlasda")
            }
            .addOnFailureListener { exception ->
                // Error.
                // ...
            }

    }
}