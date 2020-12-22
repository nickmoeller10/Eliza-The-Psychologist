
//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: Eliza
// Files: Eliza, Config, ElizaTests and Eliza.rsp
// Course: CS 200, FALL, 2018
//
// Author: Nicholas Moeller
// Email: njmoeller@wisc.edu
// Lecturer's Name: Marc Renault
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates
// strangers, etc do. If you received no outside help from either type of
// source, then please explicitly indicate NONE.
//
// Persons: (None)
// Online Sources: (None)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.io.FileOutputStream;

/**
 * The Eliza class holds the user input and response formation for a system that collects user input
 * and responds appropriately. Eliza is based off of a computer program written at MIT in the 1960's
 * by Joseph Weizenbaum. Eliza uses keyword matching to respond to users in a way that displays
 * interest in the users and continues the conversation until instructed otherwise.
 */
public class Eliza {

    /*
     * This method does input and output with the user. It calls supporting methods to read and
     * write files and process each user input.
     * 
     * @param args (unused)
     */
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in); // initalize scanner and rand num gen
        Random randGen = new Random(Config.SEED);

        String name = "Eliza" + Config.RESPONSE_FILE_EXTENSION; // Decides which name the comp will
        if (args.length == 0) { // be
            name = "Eliza" + Config.RESPONSE_FILE_EXTENSION;
        } else if (args.length == 1) {
            name = args[0] + Config.RESPONSE_FILE_EXTENSION;
        } else {
            System.out.println("Would you like to speak with ");
            for (int i = 0; i < args.length - 1; i++) {
                System.out.println(args[i] + Config.RESPONSE_FILE_EXTENSION + ", ");
            }
            System.out.println("or " + args[args.length - 1] + Config.RESPONSE_FILE_EXTENSION);
            name = scnr.next() + Config.RESPONSE_FILE_EXTENSION;
        }

        // name prompt
        System.out.println("Hi I'm " + name + ", what is your name?"); // Obtains user name
        String userName = scnr.nextLine();
        System.out.println("Hello " + userName + ". How are you?");

        // preparing conversation loop
        boolean quit = false;
        String userInput = "";
        String response = "";
        String fileName = "";
        ArrayList<String> dialog = new ArrayList<String>();

        while (quit == false) { // starts loop for convo
            userInput = scnr.nextLine();
            dialog.add(userInput);
            String[] sepWords = prepareInput(userInput); // preps user input
            if (sepWords == null) {
                quit = true;
                break;
            } else { // if there is a valid input loads response
                response = prepareResponse(sepWords, randGen, loadResponseTable("Eliza.rsp"));
                System.out.println(response);
                dialog.add(response);
            }
        }
        System.out.println("Goodbye " + userName); // goodbye prompt
        try { // checks to see if wants to save
            System.out.println("Would you like to save our conversation?");
            if (scnr.next().trim().equals("y") || scnr.next().trim().equals("Y")) {
                System.out.println("Which file?");
                fileName = scnr.nextLine();
                saveDialog(dialog, fileName);
                System.out
                    .println("Thanks again for talking! Our conversation is saved in: " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Unable to save conversation to: " + fileName);

        } finally {
            scnr.close(); // closes scanner
        }



        // Milestone 2
        // create a scanner for reading user input and a random number
        // generator with Config.SEED as the seed



        // Milestone 3
        // How the program starts depends on the command-line arguments.
        // Command-line arguments can be names of therapists for example:
        // Eliza Joe Laura
        // If no command-line arguments then the therapists name is Eliza
        // and this reads a file with that name and the Config.RESPONSE_FILE_EXTENSION.
        // Example filename: Eliza.rsp
        // If only one command-line argument, then read the responses from
        // the corresponding file with Config.RESPONSE_FILE_EXTENSION.
        // If there is more than one command-line argument then offer them
        // as a list of people to talk with. For the three therapists above the prompt is
        // "Would you like to speak with Eliza, Joe, or Laura?"
        // When a user types a name then read the responses from the file which
        // is the selected name and Config.RESPONSE_FILE_EXTENSION extension.
        // Whatever name the user types has the extension appended and
        // is read using loadResponseTable. If loadResponseTable can't load
        // the file then it will report an error.


        // Milestone 2
        // name prompt



        // Milestone 2
        // welcome prompt


        // Milestone 2
        // begin conversation loop

        // Milestone 2
        // obtain user input


        // Milestone 2
        // prepareInput

        // Milestone 3
        // if no quit words then prepareResponse


        // Milestone 2
        // end loop if quit word


        // Milestone 2
        // ending prompt


        // Milestone 3
        // Save all conversation (user and system responses) starting
        // with this program saying "Hi I'm..." and concludes with
        // "Goodbye <name>.".
        // Always prompt the user to see if they would like to save a
        // record of the conversation. If the user enters a y or Y as the
        // first non-whitespace character then prompt for filename and save,
        // otherwise don't save dialog. After successfully saving a dialog
        // print the message "Thanks again for talking! Our conversation is saved in: <filename>".
        // If saveDialog throws an IOException then catch it, print out the error:
        // "Unable to save conversation to: " <name of file>
        // Repeat the code prompting the user if they want to save the dialog.

    }

    /**
     * This method processes the user input, returning an ArrayList containing Strings, where each
     * String is a phrase from the user's input. This is done by removing leading and trailing
     * whitespace, making the user's input all lower case, then going through each character of the
     * user's input. When going through each character this keeps all digits, alphabetic characters
     * and ' (single quote). The characters ? ! , . signal the end of a phrase, and possibly the
     * beginning of the next phrase, but are not included in the result. All other characters such
     * as ( ) - " ] etc. should be replaced with a space. This method makes sure that every phrase
     * has some visible characters but no leading or trailing whitespace and only a single space
     * between words of a phrase. If userInput is null then return null, if no characters then
     * return a 0 length list, otherwise return a list of phrases. Empty phrases and phrases with
     * just invalid/whitespace characters should NOT be added to the list.
     * 
     * Example userInput: "Hi, I am! a big-fun robot!!!" Example returned: "hi", "i am", "a big fun
     * robot"
     * 
     * @param userInput text the user typed
     * @return the phrases from the user's input
     */
    public static ArrayList<String> separatePhrases(String userInput) {
        ArrayList<String> separate = new ArrayList<String>();
        if (userInput == null) { // if input isnt there returns null
            return null;
        }
        if (userInput.trim().length() == 0) { // if only whitespace returns white space
            return separate;
        }
        userInput = userInput.trim();
        userInput = userInput.toLowerCase(); // for loop goes letter by letter to build
        String subPhrase = ""; // words and put in to string
        for (int j = 0; j < userInput.length(); j++) {
            if (Character.isLetterOrDigit(userInput.charAt(j)) || userInput.charAt(j) == '\'') {
                subPhrase = subPhrase + userInput.charAt(j);
            } else if (userInput.charAt(j) == ' ' && userInput.charAt(j - 1) != ' ') {
                subPhrase = subPhrase + ' ';
            } else if (userInput.charAt(j) == '?' || userInput.charAt(j) == ','
                || userInput.charAt(j) == '.' || userInput.charAt(j) == '!') {
                subPhrase = subPhrase.trim();
                if (subPhrase.length() > 0) {
                    separate.add(subPhrase);
                }
                subPhrase = "";
            }
        }
        if (subPhrase.length() > 0) {
            separate.add(subPhrase.trim());
        }
        return separate;
    }

    /**
     * Checks whether any of the phrases in the parameter match a quit word from Config.QUIT_WORDS.
     * Note: complete phrases are matched, not individual words within a phrase.
     * 
     * @param phrases List of user phrases
     * @return true if any phrase matches a quit word, otherwise false
     */
    public static boolean foundQuitWord(ArrayList<String> phrases) { // searches for quit word and
        for (int i = 0; i < phrases.size(); i++) { // returns true is is there
            for (int j = 0; j < Config.QUIT_WORDS.length; j++) {
                if (phrases.get(i).equals(Config.QUIT_WORDS[j])) {
                    return true;
                }
            }
        }
        return false;

    }

    /**
     * Iterates through the phrases of the user's input, finding the longest phrase to which to
     * respond. If two phrases are the same length, returns whichever has the lower index in the
     * list. If phrases parameter is null or size 0 then return "" (Update 11/15/18).
     * 
     * @param phrases List of user phrases
     * @return the selected phrase
     */
    public static String selectPhrase(ArrayList<String> phrases) {
        String longPhrase = null;
        for (String part : phrases) { // selects the phrase that will be responded to
            if (longPhrase == null || part.length() > longPhrase.length()) {
                longPhrase = part;
                if (phrases == null || phrases.size() == 0) {
                    return "";
                }
            }
        }
        return longPhrase;
    }

    /**
     * Looks for a replacement word for the word parameter and if found, returns the replacement
     * word. Otherwise if the word parameter is not found then the word parameter itself is
     * returned. The wordMap parameter contains rows of match and replacement strings. On a row, the
     * element at the 0 index is the word to match and if it matches return the string at index 1 in
     * the same row. Some example word maps that will be passed in are Config.INPUT_WORD_MAP and
     * Config.PRONOUN_MAP.
     * 
     * If word is null return null. If wordMap is null or wordMap length is 0 simply return word
     * parameter. For this implementation it is reasonable to assume that if wordMap length is >= 1
     * then the number of elements in each row is at least 2.
     * 
     * @param word The word to look for in the map
     * @param wordMap The map of words to look in
     * @return the replacement string if the word parameter is found in the wordMap otherwise the
     *         word parameter itself.
     */
    public static String replaceWord(String word, String[][] wordMap) {
        if (word == null) { // replaces a word if in word map
            return null;
        }
        if (wordMap == null || wordMap.length == 0) {
            return word;
        }
        for (int i = 0; i < wordMap.length; i++) {
            if (word.equals(wordMap[i][0])) {
                return wordMap[i][1];
            }


        }
        return word;
    }

    /**
     * Concatenates the elements in words parameter into a string with a single space between each
     * array element. Does not change any of the strings in the words array. There are no leading or
     * trailing spaces in the returned string.
     * 
     * @param words a list of words
     * @return a string containing all the words with a space between each.
     */
    public static String assemblePhrase(String[] words) {
        String space = " ";
        String full = String.join(space, words); // puts the phrase back together
        full = full.trim();
        return full;
    }

    /**
     * Replaces words in phrase parameter if matching words are found in the mapWord parameter. A
     * word at a time from phrase parameter is looked for in wordMap which may result in more than
     * one word. For example: i'm => i am Uses the replaceWord and assemblePhrase methods. Example
     * wordMaps are Config.PRONOUN_MAP and Config.INPUT_WORD_MAP. If wordMap is null then phrase
     * parameter is returned. Note: there will Not be a case where a mapping will itself be a key to
     * another entry. In other words, only one pass through swapWords will ever be necessary.
     * 
     * @param phrase The given phrase which contains words to swap
     * @param wordMap Pairs of corresponding match & replacement words
     * @return The reassembled phrase
     */
    public static String swapWords(String phrase, String[][] wordMap) {
        if (wordMap == null || wordMap.length == 0) { // checks to see if word map exists
            return phrase;
        }

        String[] words = phrase.split(" "); // splits phrase in to string array
        for (int i = 0; i < wordMap.length; i++) {
            for (int j = 0; j < words.length; j++) { // checks word = word map
                if (words[j].equals(wordMap[i][0])) {
                    words[j] = replaceWord(wordMap[i][0], wordMap);
                }
            }
        }
        phrase = "";
        for (int i = 0; i < words.length; i++) { // replaces phrase
            phrase = phrase + " " + words[i];
        }
        phrase = phrase.trim();
        return phrase;
    }

    // I am happy

    // I - > you
    // you - > your
    // You am happy

    // Your am happy

    /**
     * This prepares the user input. First, it separates input into phrases (using separatePhrases).
     * If a phrase is a quit word (foundQuitWord) then return null. Otherwise, select a phrase
     * (selectPhrase), swap input words (swapWords with Config.INPUT_WORD_MAP) and return an array
     * with each word its own element in the array.
     * 
     * @param input The input from the user
     * @return words from the selected phrase
     */
    public static String[] prepareInput(String input) {
        ArrayList<String> separateInput = new ArrayList<String>();
        separateInput = separatePhrases(input); // calls on separatePhrases method
        if (foundQuitWord(separateInput) == true) { // returns null if has quit word
            return null;
        }
        String phrase = selectPhrase(separateInput); // selests phrase
        phrase = swapWords(phrase, Config.INPUT_WORD_MAP); // swaps words if nesesary
        return phrase.split(" "); // turns phrase in to String[]
    }

    /**
     * Reads a file that contains keywords and responses. A line contains either a list of keywords
     * or response, any blank lines are ignored. All leading and trailing whitespace on a line is
     * ignored. A keyword line begins with "keywords" with all the following tokens on the line, the
     * keywords. Each line that follows a keyword line that is not blank is a possible response for
     * the keywords. For example (the numbers are for our description purposes here and are not in
     * the file):
     * 
     * 1 keywords computer 2 Do computers worry you? 3 Why do you mention computers? 4 5 keywords i
     * dreamed 6 Really, <3>? 7 Have you ever fantasized <3> while you were awake? 8 9 Have you ever
     * dreamed <3> before?
     *
     * In line 1 is a single keyword "computer" followed by two possible responses on lines 2 and 3.
     * Line 4 and 8 are ignored since they are blank (contain only whitespace). Line 5 begins new
     * keywords that are the words "i" and "dreamed". This keyword list is followed by three
     * possible responses on lines 6, 7 and 9.
     * 
     * The keywords and associated responses are each stored in their own ArrayList. The response
     * table is an ArrayList of the keyword and responses lists. For every keywords list there is an
     * associated response list. They are added in pairs into the list that is returned. There will
     * always be an even number of items in the returned list.
     * 
     * Note that in the event an IOException occurs when trying to read the file then an error
     * message "Error reading <fileName>", where <fileName> is the parameter, is printed and a
     * non-null reference is returned, which may or may not have any elements in it.
     * 
     * @param fileName The name of the file to read
     * @return The response table
     */
    public static ArrayList<ArrayList<String>> loadResponseTable(String fileName) {
        File file = new File(fileName);
        ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
        Scanner fileScnr = null;
        String line = "";
        String word = "";
        Scanner lineScnr = null; // insert if statement??
        ArrayList<String> keyword = null;
        ArrayList<String> response = null;

        try { // searches for keywords in the file
            fileScnr = new Scanner(file); // separates the keywords with the responses
            while (fileScnr.hasNextLine()) { // creates a table of keywords and responses
                line = fileScnr.nextLine();
                line = line.trim();
                lineScnr = new Scanner(line);

                if (line.length() == 0) {
                    continue;
                } else if (line.startsWith("keywords")) {
                    if (response != null) {
                        table.add(response);
                    }
                    keyword = new ArrayList<>();
                    response = new ArrayList<>();

                    lineScnr.next();

                    while (lineScnr.hasNext()) {
                        word = lineScnr.next();
                        keyword.add(word);
                    }
                    table.add(keyword);
                } else {
                    response.add(line);
                }
            }
        } catch (IOException e) {
            ArrayList<ArrayList<String>> expt = new ArrayList<ArrayList<String>>();
            System.out.println("Error reading " + fileName); // catches if IOException
            return expt;
        } finally {
            if (fileScnr != null || lineScnr != null) { // closes scanners
                fileScnr.close();
                lineScnr.close();
            }
        }
        table.add(response);
        return table;
    }

    /**
     * Checks to see if the keywords match the sentence. In other words, checks to see that all the
     * words in the keyword list are in the sentence and in the same order. If all the keywords
     * match then this method returns an array with the unmatched words before, between and after
     * the keywords. If the keywords do not match then null is returned.
     * 
     * When the phrase contains elements before, between, and after the keywords, each set of the
     * three is returned in its own element String[] keywords = {"i", "dreamed"}; String[] phrase =
     * {"do", "you", "know", that", "i", "have", "dreamed", "of", "being", "an", "astronaut"};
     * 
     * toReturn[0] = "do you know that" toReturn[1] = "have" toReturn[2] = "of being an astronaut"
     * 
     * In an example where there is a single keyword, the resulting List's first element will be the
     * the pre-sequence element and the second element will be everything after the keyword, in the
     * phrase String[] keywords = {"always"}; String[] phrase = {"I", "always", "knew"};
     * 
     * toReturn[0] = "I" toReturn[1] = "knew"
     * 
     * In an example where a keyword is not in the phrase in the correct order, null is returned.
     * String[] keywords = {"computer"}; String[] phrase = {"My","dog", "is", "lost"};
     * 
     * return null
     * 
     * @param keywords The words to match, in order, in the sentence.
     * @param phrase Each word in the sentence.
     * @return The unmatched words before, between and after the keywords or null if the keywords
     *         are not all matched in order in the phrase.
     */
    public static String[] findKeyWordsInPhrase(ArrayList<String> keywords, String[] phrase) {
        String[] findWords = new String[keywords.size() + 1];
        for (int i = 0; i < keywords.size() + 1; i++) { // assigns all indexes to ""
            findWords[i] = "";
        }
        String notKey = "";
        int pos = 0;
        int numKey = 0;
        for (int i = 0; i < phrase.length; i++) { // goes through phrase word by word
            if (numKey < keywords.size() && phrase[i].equals(keywords.get(numKey))) {
                if (notKey.length() > 0) {
                    findWords[pos] = notKey;
                    notKey = ""; // if keyword is present, gets rid of it and
                } // parses previous part and leaves as par of array
                notKey = "";
                pos += 1;
                numKey += 1;
            } else {
                if (notKey.length() > 0) {
                    notKey += " " + phrase[i];
                } else if (notKey.length() == 0) {
                    notKey = phrase[i];
                }
            }
        }
        if (notKey.length() > 0) { // if there is more text after keyword, adds it
            findWords[pos] = notKey;
        }
        if (numKey != keywords.size()) { // if all key words arent used
            return null;
        }

        return findWords;
    }


    /**
     * Selects a randomly generated response within the list of possible responses using the
     * provided random number generator where the number generated corresponds to the index of the
     * selected response. Use Random nextInt( responseList.size()) to generate the random number. If
     * responseList is null or 0 length then return null.
     * 
     * @param rand A random number generator.
     * @param responseList A list of responses to choose from.
     * @return A randomly selected response
     */
    public static String selectResponse(Random rand, ArrayList<String> responseList) {
        if (responseList == null || responseList.size() == 0) { // if there are no responses
            return null;
        }
        int index = rand.nextInt(responseList.size()); // picks random response
        String response = responseList.get(index);
        return response;
    }

    /**
     * This method takes processed user input and forms a response. This looks through the response
     * table in order checking to see if each keyword pattern matches the userWords. The first
     * matching keyword pattern found determines the list of responses to choose from. A keyword
     * pattern matches the userWords, if all the keywords are found, in order, but not necessarily
     * contiguous. This keyword matching is done by findKeyWordsInPhrase method. See the
     * findKeyWordsInPhrase algorithm in the Eliza.pdf.
     * 
     * If no keyword pattern matches then Config.NO_MATCH_RESPONSE is returned. Otherwise one of
     * possible responses for the matched keywords is selected with selectResponse method. The
     * response selected is checked for the replacement symbol <n> where n is 1 to the length of
     * unmatchedWords array returned by findKeyWordsInPhrase. For each replacement symbol the
     * corresponding unmatched words element (index 0 for <1>, 1 for <2> etc.) has its pronouns
     * swapped with swapWords using Config.PRONOUN_MAP and then replaces the replacement symbol in
     * the response.
     * 
     * @param userWords using input after preparing.
     * @param rand A random number generator.
     * @param responseTable A table containing a list of keywords and response pairs.
     * @return The generated response
     */
    public static String prepareResponse(String[] userWords, Random rand,
        ArrayList<ArrayList<String>> responseTable) {
        String[] unMatched = null;
        int j = 0;
        for (j = 0; j < responseTable.size(); j += 2) { // goes through and compares keywords in
            ArrayList<String> keywords = responseTable.get(j); // table to key words in phrase
            unMatched = findKeyWordsInPhrase(keywords, userWords);
            if (unMatched != null) {
                break;
            }

        }
        if (unMatched == null) { // if there are no unmatched words
            return Config.NO_MATCH_RESPONSE;
        }
        String response = selectResponse(rand, responseTable.get(j + 1));
        String fixed = "";
        for (int i = 1; i <= unMatched.length; i++) { // for all of the unmatched words
            String search = "<" + i + ">";
            if (response.contains(search)) {
                fixed = swapWords(unMatched[i - 1], Config.PRONOUN_MAP);
                response = response.replace(search, fixed);
            }
        }
        return response;
    }


    // Iterate through the response table.
    // The response table has paired rows. The first row is a list of key
    // words, the next a list of corresponding responses. The 3rd row another
    // list of keywords and 4th row the corresponding responses.

    // checks to see if the current keywords match the user's words
    // using findKeyWordsInPhrase.

    // if no keyword pattern was matched, return Config.NO_MATCH_RESPONSE
    // else, select a response using the appropriate list of responses for the keywords

    // Look for <1>, <2> etc in the chosen response. The number starts with 1 and
    // there won't be more than the number of elements in unmatchedWords returned by
    // findKeyWordsInPhrase. Note the number of elements in unmatchedWords will be
    // 1 more than the number of keywords.
    // For each <n> found, find the corresponding unmatchedWords phrase (n-1) and swap
    // its pronoun words (swapWords using Config.PRONOUN_MAP). Then use the
    // result to replace the <n> in the chosen response.

    // in the selected echo, swap pronouns

    // inserts the new phrase with pronouns swapped, into the response


    /**
     * Creates a file with the given name, and fills that file line-by-line with the tracked
     * conversation. Every line ends with a newline. Throws an IOException if a writing error
     * occurs.
     * 
     * @param dialog the complete conversation
     * @param fileName The file in which to write the conversation
     * @throws IOException
     */
    public static void saveDialog(ArrayList<String> dialog, String fileName) throws IOException {
        File file = new File(fileName);
        PrintWriter outW = new PrintWriter(file);
        for (int i = 0; i < dialog.size(); i++) { // goes through each line of dialog and
            outW.println(dialog.get(i)); // sends them to the file
        }
        outW.close();
    }
}

