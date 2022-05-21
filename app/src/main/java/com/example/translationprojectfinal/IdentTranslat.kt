package com.example.translationprojectfinal

import android.content.ContentValues.TAG
import android.content.Context
import android.nfc.Tag
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import kotlinx.android.synthetic.main.activity_ident_translat.*
import java.util.*

class IdentTranslat : AppCompatActivity() {
    private var langCode = ArrayList<String>()
    private var languageFinal =HashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ident_translat)
        readWordsFiles()
    }

    /*
    Reads a word file that contains the country codes. Then places them into a hashmap
     */
    private fun readWordsFiles(){
        val reader = Scanner(resources.openRawResource(R.raw.lang2))
        while(reader.hasNextLine()){
            val line = reader.nextLine()
            Log.d("WordFile","The line is $line")
            val parts = line.split("*")
            langCode.add(parts[0])
            languageFinal.put(parts[0],parts[1])
        }

    }
    /*
    Identification function
     */

    fun ident( v: View)
    {
        val text = etlang.text.toString() //Takes the inputed text
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(text) //Inputs the text that was recieved from line 51 into the api
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {
                    Log.i(TAG, "Can't identify language.")
                } else {
                    Log.i(TAG, "Language: $languageCode")
                    val language = languageCode //Recieves language code
                    Log.d(TAG, "Language Code = " + language)
                    val finalLangToD = languageFinal[language] //Find out the language name from the hashmap earlier and langauge code that was recieved in line 57
                    tvlang.text= finalLangToD //Changes text view into the language we got from line 59


                }
            }
            .addOnFailureListener {
                // Model couldn’t be loaded or other internal error.
                // ...
            }
    }
}