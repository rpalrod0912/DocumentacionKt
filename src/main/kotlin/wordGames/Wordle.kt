package wordGames

import WordGamesConfig.WordGameConfig
import WordGamesConfig.WordGameImporExport
import WordGamesConfig.WordGameJsonImportExport
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import wordCatalog.ListWordCatalog
import wordCatalog.WordCatalog

class Wordle: WordGame
{
    private var guessResult: MutableMap<Int,Pair<Char, letterSquareState> > = mutableMapOf()

    private lateinit var imporExport:WordGameImporExport


    constructor(maxTrials:Int=6,
                wordLength: Int=5,
                dictionary: String = "Spanish.dic",
                wordCatalog: WordCatalog = ListWordCatalog()
               )
               : super("Wordle",maxTrials,wordLength,dictionary,wordCatalog)
    {
        this.wordLength = wordLength
        gameDescription = "Wordle is a word game where you try to form words from the letters you draw."
        gameInstructions = "Draw a word by clicking on the letters.  The letters will be removed from the board."
    }

    fun checkWord(guessWord: String): wordleGuess
    {
        for(i in 0 until wordLength)
        {
            if(word[i]==guessWord[i])
               {  guessResult[i] = Pair(word[i], letterSquareState.IN_SPOT)  }
            else
               {
                    if(lettersPositions.containsKey(guessWord[i]))
                       { guessResult[i] = Pair(guessWord[i], letterSquareState.WRONG_SPOT) }
                    else
                       {  guessResult[i] = Pair(guessWord[i], letterSquareState.NOT_IN_WORD) }
               }
        }
        return guessResult
    }

    fun correctGuess(guessWord: String): Boolean
    {
        checkWord(guessWord)
        guessResult.forEach {
            if(it.value.second != letterSquareState.IN_SPOT)
            {
                return false
            }
        }
        return true
    }

    fun jsonSerialize(): String
    {
        imporExport= WordGameJsonImportExport(WordGameConfig(gameName, wordLength, maxTrials))
        val json = imporExport.serialize()
        return json
    }

    fun yamlSerialize():String
    {
        val yamlTextval  = Yaml.default.encodeToString(
            WordGameConfig.serializer(),
            WordGameConfig(gameName, wordLength, maxTrials)
        )
        return yamlTextval
    }

}