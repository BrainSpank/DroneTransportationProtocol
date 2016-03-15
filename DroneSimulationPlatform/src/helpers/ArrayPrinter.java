package helpers;

public class ArrayPrinter {
    public static String print(Integer[] intArray){
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(intArray[0].toString());

        for(int j = 1; j <intArray.length; j++){
            sb.append(", " + intArray[j].toString());
        }

        sb.append("}");

        return sb.toString();
    }
}