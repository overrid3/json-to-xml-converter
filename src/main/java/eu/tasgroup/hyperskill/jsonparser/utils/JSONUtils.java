package eu.tasgroup.hyperskill.jsonparser.utils;

import eu.tasgroup.hyperskill.jsonparser.model.JSONElement;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JSONUtils {

    public static final String JSON_STRING_CANNOT_BE_NULL = "Json string cannot be null";
    public static final String INVALID_JSON_VALUE_MESSAGE = "String '%s' passed as parameter is not a valid Json value";

    //PATTERN DA RISPETTARE PER IL SET TEXT DEL FUTURO ELEMENTO XML
    private static Pattern stringPattern = Pattern.compile("^\"(.*)\"$");
    private static Pattern integerPattern = Pattern.compile("^\\d+$");
    private static Pattern doublePattern = Pattern.compile("^\\d+\\.\\d+$");
    private static Pattern booleanPattern = Pattern.compile("^(?>true|false)$");
    private static Pattern nullPattern = Pattern.compile("^null$");

    /**
     * - ""stringa"" -> String
     * - "2" -> Integer
     * - "2.2" -> Double
     * - "true|false" -> Boolean
     * - "null" -> null
     *
     * @param jsonValue
     * @return
     */

    public static Object convertValue(String jsonValue) {
        Objects.requireNonNull(jsonValue, JSON_STRING_CANNOT_BE_NULL);

        jsonValue = jsonValue.trim();
        // String case
        Matcher stringMatcher = stringPattern.matcher(jsonValue);
        if (stringMatcher.find())
            return stringMatcher.group(1);
        // Integer case
        Matcher integerMatcher = integerPattern.matcher(jsonValue);
        if (integerMatcher.find())
            return new Integer(integerMatcher.group(0));
        // Double case
        Matcher doubleMatcher = doublePattern.matcher(jsonValue);
        if (doubleMatcher.find())
            return new Double(doubleMatcher.group(0));
        // Boolean case
        Matcher booleanMatcher = booleanPattern.matcher(jsonValue);
        if (booleanMatcher.find())
            return new Boolean(booleanMatcher.group(0));
        // Null case
        Matcher nullMatcher = nullPattern.matcher(jsonValue);
        if (nullMatcher.find())
            return null;

        throw new InvalidParameterException(String.format(INVALID_JSON_VALUE_MESSAGE, jsonValue));
    }

    public static boolean isValidKey(String key) {

        Objects.requireNonNull(key, JSON_STRING_CANNOT_BE_NULL);

        if (key.equals("@"))
            return false;
        if (key.equals(""))
            return false;
        if (key.equals("#"))
            return false;
        return true;
    }

    public static String getNormalizedKey(String originalKey) {
        if (Objects.isNull(originalKey))
            return null;
        return originalKey.replaceFirst("^[@#]", "");
    }

    public static boolean hasNoChildren(JSONElement jsonElement) {
        return jsonElement.getChildren().isEmpty();
    }

    public static boolean hasExactlyOneChildWithHashKey(JSONElement jsonElement) {

        List<JSONElement> l = jsonElement.getChildren().stream().filter(child -> child.getKey().startsWith("#")).collect(Collectors.toList()); //si può fare con anymatch()

        return l.size() == 1 && JSONUtils.getNormalizedKey(l.get(0).getKey()).equals(jsonElement.getKey());
    }

    public static boolean hasChildWithoutSpecialCharacter(JSONElement jsonElement) {

        return jsonElement.getChildren().stream()
                .anyMatch(jsonChild -> !jsonChild.getKey().startsWith("@") && !jsonChild.getKey().startsWith("#"));
    }

    //DUBBIO SU CICLARE DIRETTAMENTE QUI O PASSARE UNA LISTA DI VALORI ALLA CLASSE, MAGARI VEDO DOPO...
    public static boolean hasEmptyOrIllegalKey(JSONElement jsonElement) {
        return jsonElement.getChildren().stream().anyMatch(child ->illegalInitialAndOnlyCharacter(child.getKey()));
    }

    public static boolean atKeyElementHasNotEmptyChildren(JSONElement e) {
        return e.getChildren().stream().anyMatch(child -> !hasNoChildren(child) && child.getKey().startsWith("@"));
    }

    public static boolean illegalInitialAndOnlyCharacter(String key) {
        return key.equals("@") || key.equals("#") || key.isEmpty(); //TENIAMO L'ISEMPTY() PER SALVARE L'OGGETTO XML CON KEY VUOTA O NO?
    }

    public static JSONElement getHashChild(JSONElement el){
        Optional<JSONElement> nuccio= el.getChildren().stream().filter(child -> child.getKey().startsWith("#")).findFirst();

        return nuccio.get();
    }

}
