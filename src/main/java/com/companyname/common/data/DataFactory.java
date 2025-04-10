package com.companyname.common.data;

import com.companyname.common.data.login.User;
import com.companyname.common.data.login.UserList;
import com.companyname.exceptions.DataException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class DataFactory {

    private static final Logger LOG = LogManager.getLogger(DataFactory.class);

    // data filepath constants
    public static final String BASE_DATA_PATH = "src/test/resources/data";
    public static final String METADATA = "metadata";

    // Common file names
    public static final String DATAGATHER_YAML = "DataGather.yaml";
    public static final String DASH_DATAGATHER_PREFILL_YAML = "DataGatherPrefill.yaml";
    public static final String METADATA_YAML = "Metadata.yaml";
    public static final String ISSUE_YAML = "Issue.yaml";

    // Payment functions
    public static final String MIN_DUE = "/due";
    public static final String TOTAL_DUE = "/all";

    public static final String OFFSET_TEMPLATE = "$<today+%s>";

    protected String appDataPath;


    protected DataFactory() {
    }

    /**
     * Retrieve Login data for a given User
     * @param user String value for desired login user (Use constants/User)
     * @return User object
     */
    public User getDefaultLoginData(String user) {
        UserList login = (UserList) new DataSupplier(BASE_DATA_PATH + "/", "Login.yaml", UserList.class).getDataObject();
        LOG.info("Retrieved default login data for " + user + " user");
        return login.get(user);
    }

    /**
     * Returns an empty data object with all fields which are objects instantiated recursively
     *
     * Boolean fields are initialized to 'false'
     * String fields remain null, and can be set as needed
     * Lists are initialized to size = 1 for the specific Data type, with that Data object initialized using these rules recursively
     * Data types are initialized with above rules recursively
     *
     * @param clazz Data class to instantiate
     * @return Data object of the specified type with all fields instantiated recursively
     * @param <T> of type Data
     */
    @SuppressWarnings("unchecked")
    public <T> T getEmptyData(Class<?> clazz) {
        try {
            T emptyData = (T) clazz.getConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getType().isAssignableFrom(Boolean.class)) {
                    field.set(emptyData, Boolean.FALSE);
                } else if (field.getType().isAssignableFrom(List.class)) {
                    List<?> list = new ArrayList<>();
                    field.set(emptyData, list);
                } else if (!(field.getType().isAssignableFrom(String.class))) {
                    field.set(emptyData, getEmptyData(field.getType()));
                }
            }
            return emptyData;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOG.error(e.getMessage(), e);
            throw new DataException("Unable to instantiate object of type: " + clazz.getSimpleName());
        }
    }

    /**
     * Recursively copies a Data object
     *
     * @param dataToCopy Data object to copy from.
     * @param <T> of type Data
     * @return A copy of the specified data.
     */
    @SuppressWarnings("unchecked")
    public <T> T getCopyOf(T dataToCopy) {
        if (dataToCopy == null) return null;

        if (dataToCopy instanceof String || dataToCopy instanceof Boolean) {
            return dataToCopy;
        }

        if (dataToCopy instanceof List) {
            List<?> list = (List<?>) dataToCopy;
            ArrayList<Object> copy = new ArrayList<>(list.size());
            for (Object item : list) {
                copy.add(getCopyOf(item));
            }
            return (T) copy;
        }

        try {
            T copy = (T) dataToCopy.getClass().getDeclaredConstructor().newInstance();
            for (Field field : dataToCopy.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(dataToCopy);
                field.set(copy, getCopyOf(value));
            }
            return copy;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LOG.error(e.getMessage(), e);
            throw new DataException("Unable to create copy of {} Data object " + dataToCopy.getClass().getSimpleName());
        }
    }

    /**
     * Builds out a string value representing a relative path.
     * For example, if I pass in the following String parameters: ("src/test/resources", "default", "pas")
     * I will get this returned - "src/test/resources/default/pas/"
     * @param parts String vararg representing the bits of the path to construct.  Should not include the actual filename.
     * @return String value of the path constructed
     */
    public static String buildPath(String... parts) {
        String path = "";
        for (int i = 0; i < parts.length; i++) {
            if (parts[i] != null && !parts[i].isBlank()) {
                if (i > 0 && !path.endsWith("/")) {
                    path = path.concat("/");
                }
                path = path.concat(parts[i]);
            }
        }
        path = path.endsWith("/") ? path : path.concat("/");
        return path;
    }

//    public static String prepareDataValue(String value) {
//        if (value != null) {
//            value = value.trim();
//            if (value.contains(DataFunctionConstants.FUNCTION_START)) {
//                String precedingChars = value.split("\\$").length == 2 ? value.split("\\$")[0] : "";
//                String followingChars = value.split(FUNCTION_END).length == 2 ? value.split(FUNCTION_END)[1] : "";
//                value = DataFactory.processDataFunction(value.substring(value.indexOf("$"), value.indexOf(FUNCTION_END) + 1));
//                value = precedingChars + value + followingChars;
//            }
//        }
//        return value;
//    }
//
//    public static boolean isFunction(String dataValue) {
//        return dataValue != null && dataValue.startsWith(FUNCTION_START) && dataValue.endsWith(FUNCTION_END);
//    }
//
//    /**
//     * Extracts the function name, and it's args into a String array.
//     * @param dataValue the String input to extract from
//     * @return String array, where [0] is the function name and [1], [2], etc. are the arguments
//     *         For time functions (today) the array returns as ['today', timeDiffValue, dateFormatString]
//     */
//    public static String[] extractFunctionArray(String dataValue) {
//        String[] array = null;
//        if (isFunction(dataValue)) {
//            String function = dataValue.replace(FUNCTION_START, "").replace(FUNCTION_END, "");
//            String name = null;
//            String arg = null;
//            if (function.contains(ARG_DELIMITER)) {
//                array = function.split(ARG_DELIMITER);
//                name = array[0].trim();
//                arg = array[1].trim();
//                if (name.contains(TODAY)) {
//                    array = name.split(TODAY).length == 0 ? new String[]{TODAY, null, arg} : new String[]{TODAY, name.split(TODAY)[1].trim(), arg};
//                }
//            } else if (function.contains(TODAY)) {
//                array = function.split(TODAY).length == 0 ? new String[]{TODAY, null, null} : new String[]{TODAY, function.split(TODAY)[1].trim(), null};
//            }
//        }
//        return array;
//    }
//
//    /**
//     * Performs the requested function/modification for a data value
//     * @param function String value which starts with "$<" and ends with ">".  Format is checked in calling method and can be assumed here.
//     * @return new String value representing desired change via provided function
//     */
//    protected static String processDataFunction(String function) {
//        if (function != null && !function.contains(FUNCTION_START)) {
//            LOG.trace(function + " is not a data function, done processing");
//            return function;
//        }
//        String newVal = null;
//        String arg = null;
//        String dateFormatString = null;
//        LOG.trace("Processing function " + function);
//        String[] functionArray = extractFunctionArray(function);
//        String functionName = functionArray[0];
//        if (functionArray.length > 1) {
//            arg = functionArray[1];
//            LOG.trace("Found specifier '" + arg + "' for function " + functionName);
//        }
//        if (functionName.equalsIgnoreCase(TODAY) && functionArray.length > 2) {
//            dateFormatString = functionArray[2];
//            LOG.trace("Found date format specifier for 'today' function: %s" + dateFormatString);
//        }
//        switch (functionName) {
//            case NULL:
//                return null;
//            case GENEREX:
//                if (arg == null) {
//                    LOG.error("Regex must be specified to generate a random character sequence!");
//                    throw new DataException("Regex not specified for function");
//                }
//                newVal = new Generex(arg).random();
//                break;
//            case TODAY:
//                DateTimeFormatter formatter;
//                LocalDateTime today = TimeSetterUtil.getInstance().getCurrentDateTime();
//                if (dateFormatString == null) {
//                    formatter = DateTimeUtil.DATE_FORMATTER_UI;
//                } else {
//                    formatter = DateTimeFormatter.ofPattern(dateFormatString);
//                }
//                if (arg == null) {
//                    return formatter.format(today);
//                }
//                String strAmt = arg.split("[+-]")[1];
//                TemporalAmount tempAmt = Period.parse("P" + strAmt);
//                if (arg.startsWith("+")) {
//                    newVal = formatter.format(today.plus(tempAmt));
//                } else if (arg.startsWith("-")) {
//                    newVal = formatter.format(today.minus(tempAmt));
//                }
//        }
//        return newVal;
//    }

    /**
     * Returns Data object from src/test/resources/data/{app}/{filePath}
     * The returned object must be cast to the appropriate data class to be used in tests
     * @param filePath relative path of the file inside data/{app} folder
     * @param fileName name of file, including extension '.yaml'
     * @param dataType Data type the object represents and should be instantiated as (for example, AutoPolicy.class)
     * @return Data object representing the yaml data.
     */
    protected Data getDataFromFile(String filePath, String fileName, Class<? extends Data> dataType) {
        LOG.debug("Retrieving " + dataType.getSimpleName() + " data from " + filePath + fileName);
        String path = buildPath(appDataPath, filePath);
        return (Data) new DataSupplier(path, fileName, dataType).getDataObject();
    }

    /**
     * !!!!!!!!!!!!!!!                                                                                         !!!!!!!!!!!!!!!
     * !!!!!!!!!!!!!!!  THIS SHOULD NOT BE MODIFIED WITHOUT PRIOR DISCUSSION AND APPROVAL FROM QA ARCHITECTURE !!!!!!!!!!!!!!!
     * !!!!!!!!!!!!!!!                                                                                         !!!!!!!!!!!!!!!
     *
     * This method merges data objects such that fields in the defaultData are preserved if a new value is not existing
     * in the deltaData object.  If a new value for a given field or object does exist in the deltaData object, it will
     * be overwritten in the merged data object.  The resulting object will contain all values from the deltaData, and all
     * values from the defaultData object which were not overwritten from deltaData.  The data types for the parameters
     * MUST match.
     *
     * IMPORTANT: If you wish to clear out a value from the defaultData object, you must pass in '$<null>' as the object value
     * in the deltaData object, and it will return it null .  If you do not do this, the defaultData will persist!  Exception is
     * for Boolean data values; set to the opposite boolean value.
     *
     * ONE EXCEPTION: For Lists of Objects you must specify all values of all list values in the deltaData object,
     * as the entire list will be overwritten (not just the delta fields within each list object).
     *
     * @param <T> Generic data type - 'Data' (see interface)
     * @param defaultData the original data object to merge into
     * @param deltaData the delta data object to merge data from
     * @return Merged data object of type T (object implementing the Data interface)
     */
    @SuppressWarnings("unchecked")
    protected <T> T mergeDataObjects(T defaultData, T deltaData) {
        LOG.debug("Merging data objects recursively:  defaultData of type " + defaultData.getClass().getSimpleName() +
                "; deltaData of type " + deltaData.getClass().getSimpleName());
        Class<?> clazz = defaultData.getClass();
        T mergedData;
        try {
            mergedData = (T) clazz.getConstructor().newInstance();
            for (Field field : FieldUtils.getAllFields(clazz)) {
                field.setAccessible(true);
                Object defaultValue = field.get(defaultData);
                Object deltaValue = field.get(deltaData);
                if (deltaValue == null) {
                    field.set(mergedData, defaultValue);
                } else {
                    if (deltaValue instanceof String[]) {
                        String[] deltaArray = (String[]) deltaValue;
                        deltaArray = deltaArray.length == 1 && deltaArray[0].equals("$<null>") ? null : deltaArray;
                        field .set(mergedData, deltaArray);
                    } else if (deltaValue instanceof String) {
                        deltaValue = (deltaValue).equals("$<null>") ? null : deltaValue;
                        field.set(mergedData, deltaValue);
                    } else if (deltaValue instanceof Boolean) {
                        field.set(mergedData, deltaValue);
                    } else if (deltaValue instanceof List) {
                        int size = ((List<?>) deltaValue).size();
                        List<T> list = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            T deltaItem = (T) ((List<?>) deltaValue).get(i);
                            T defaultItem;
                            if (defaultValue == null || ((List<T>) defaultValue).size() - 1 < i) {
                                defaultItem = (T) deltaItem.getClass().getConstructor().newInstance();
                            } else {
                                defaultItem = (T) ((List<?>) defaultValue).get(i);
                            }
                            if (deltaItem instanceof List) {
                                list.add(mergeDataObjects(defaultItem, deltaItem));
                            } else {
                                for (Field itemField : deltaItem.getClass().getDeclaredFields()) {
                                    itemField.setAccessible(true);
                                    if (itemField.get(deltaItem)!= null && itemField.get(deltaItem).equals("$<null>")) {
                                        itemField.set(deltaItem, null);
                                    }
                                }
                                list.add(deltaItem);
                            }
                        }
                        field.set(mergedData, list);
                    } else {
                        if (defaultValue == null && field.getType() != String.class) {
                            defaultValue = field.getType().getConstructor().newInstance();
                        }
                        field.set(mergedData, mergeDataObjects(defaultValue, deltaValue));
                    }
                }
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOG.error(e.getMessage(), e);
            throw new DataException("Unable to merge data objects");
        }
        return mergedData;
    }

}
