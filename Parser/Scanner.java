import java.io.BufferedReader;

import java.io.FileReader;
import java.util.*;

import static java.lang.Character.*;

class Scanner {
    private static final Core[] CORE_KEYWORDS = {Core.PROCEDURE, Core.BEGIN, Core.IS, Core.END, Core.IF, Core.ELSE,
            Core.IN, Core.INTEGER, Core.RETURN, Core.DO, Core.NEW, Core.NOT, Core.AND, Core.OR, Core.OUT,
            Core.OBJECT, Core.THEN, Core.WHILE};
    private static final Core[] CORE_SPECIAL_SYMBOLS = {Core.ADD, Core.SUBTRACT, Core.MULTIPLY, Core.DIVIDE, Core.ASSIGN, Core.EQUAL,
            Core.LESS, Core.COLON, Core.SEMICOLON, Core.PERIOD, Core.COMMA, Core.LPAREN, Core.RPAREN, Core.LBRACE, Core.RBRACE};
    private static final String[] TEXT_SPECIAL_SYMBOLS = {"+", "-", "*", "/", "=", "==", "<", ":", ";", ".", ",",
            "(", ")", "[", "]"};
    private final BufferedReader br;
    private static List<String> tokens;
    private static List<Core> coreTokens;
    private static Map<String, Core> textToCore;
    private static Set<String> specialSymbolSet;
    private static List<String> data;
    public Core currentToken;
    private int index = 0;
    private int dataIndex = 0;

        // Initialize the scanner
    Scanner(String filename) throws Exception {
        br = new BufferedReader(new FileReader(filename));
        tokens = new ArrayList<>();
        textToCore = textToEnums();
        specialSymbolSet = new HashSet<>();
        Set<Core> coreKeywords = new HashSet<>();
        data = new ArrayList<>();
        coreTokens = new ArrayList<>();

        initializeSpecialCharSet(specialSymbolSet, coreKeywords);
        tokenizeFile();
        stringToCoreTokens();

        currentToken = coreTokens.get(index);

    }

    /**
     * Creates a Map of strings to their Core enumeration counterparts.
     * @return map String -> Core
     */
    private static Map<String, Core> textToEnums() {
        Map<String, Core> enumMap = new HashMap<>();
        for (Core coreKeyword : CORE_KEYWORDS) {
            enumMap.put(coreKeyword.toString().toLowerCase(), coreKeyword);
        }

        for (int i = 0; i < CORE_SPECIAL_SYMBOLS.length; i++){
            enumMap.put(TEXT_SPECIAL_SYMBOLS[i], CORE_SPECIAL_SYMBOLS[i]);
        }
        return enumMap;
    }

    public static void initializeSpecialCharSet(Set<String> s, Set<Core> keywords) {
        Collections.addAll(s, TEXT_SPECIAL_SYMBOLS);
        Collections.addAll(keywords, CORE_KEYWORDS);

    }

    /**
     * Fills a list with Cores from the string tokenized file.
     */
    public static void stringToCoreTokens() {
        for (String currentToken : tokens) {
            // if the current token is a special symbol or keyword, can simply do a get
            // from the map and add that
            if (textToCore.containsKey(currentToken.toLowerCase())) {
                coreTokens.add(textToCore.get(currentToken.toLowerCase()));
            } else if (currentToken.equals("ID")) {
                //add ID in the place of any data
                coreTokens.add(Core.ID);
            } else if (currentToken.equals("CONST")) {
                //same as id, can access the data from a different list
                coreTokens.add(Core.CONST);
            }

        }

        // after adding all the tokens from the file, add the end token
        coreTokens.add(Core.EOS);

    }

    /**
     * In this approach, the entire file is tokenized and put into a list.
     * @throws Exception if the character read is invalid in the language
     */
    public void tokenizeFile() throws Exception {
        // go character by character
        StringBuilder sb = new StringBuilder();
        char currChar = (char) br.read();
        // while there are still characters to be read
        while (br.ready()) {
            //if we see a letter, we go until we see a letter or a digit
            if (isLetter(currChar)) {
                while (isLetterOrDigit(currChar)) {
                    sb.append(currChar);
                    currChar = (char) br.read();
                }

                String str = sb.toString();
                // if the map contains the key, then it is a keyword
                if (textToCore.containsKey(str)){
                    tokens.add(textToCore.get(str).toString());
                } else {
                    // if it's not then add id to the token list and add the data to the data list
                    tokens.add("ID");
                    data.add(str);
                }
                // reset the string builder
                sb.setLength(0);
            } else if (isDigit(currChar)){
                //if we see a digit, keep going until we do not see a digit
                while (isDigit(currChar)) {
                    sb.append(currChar);
                    currChar = (char) br.read();
                }
                String str = sb.toString();
                // add constant to the token list and its data to the data list
                tokens.add("CONST");
                data.add(str);
                sb.setLength(0);
            } else if (specialSymbolSet.contains(String.valueOf(currChar))){
                sb.append(currChar);
                // we want to check for == so mark the current character
                br.mark(1);
                if ((char) br.read() == '=') {
                    // if it is =, then append it but if not then we can reset and keep going
                    sb.append("=");
                } else {
                    br.reset();
                }
                tokens.add(sb.toString());
                currChar = (char) br.read();
                sb.setLength(0);
            } else if (isWhitespace(currChar)) {
                // if whitespace we can go through
                currChar = (char) br.read();
            } else {
                // last case, if the character is illegal
                throw new Exception("Invalid character '" + currChar + "' detected");
            }
        }
    }


    // Advance to the next token
    public void nextToken() {
        // we already have a full list, so incrementing the index is sufficient
        index++;
    }

    // Return the current token
    public Core currentToken() {
        return coreTokens.get(index);
    }

	// Return the identifier string
    public String getId() throws Exception {
        // check first if the current token is an identifier
        if (coreTokens.get(index).equals(Core.ID)) {
            // get the identifier from the data list and increment the data index, so it is ready for the next call
            String id = data.get(dataIndex);
            dataIndex++;
            return id;
        } else {
            // if not, throw an error
            throw new Exception("ERROR: Current token not an identifier.");
        }

    }

	// Return the constant value
    public int getConst() throws Exception{
        // first check if current token is a constant
        if (coreTokens.get(index).equals(Core.CONST)) {
            // get data at that index and increment data index
            String constant = data.get(dataIndex);
            dataIndex++;
            return Integer.parseInt(constant);
        } else {
            // else, throw an error
            throw new Exception("ERROR: Current token not a constant.");
        }
    }

}
