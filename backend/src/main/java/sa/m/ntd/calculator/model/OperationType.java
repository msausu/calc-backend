package sa.m.ntd.calculator.model;

import java.util.ArrayList;
import java.util.List;

public enum OperationType {
    ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, SQUARE_ROOT, RANDOM_STRING;
    public static List<OperationType> find(String type) {
        List<OperationType> types = new ArrayList<>();
        if (ADDITION.name().contains(type.toUpperCase())) types.add(ADDITION);
        if (SUBTRACTION.name().contains(type.toUpperCase())) types.add(SUBTRACTION);
        if (MULTIPLICATION.name().contains(type.toUpperCase())) types.add(MULTIPLICATION);
        if (DIVISION.name().contains(type.toUpperCase())) types.add(DIVISION);
        if (SQUARE_ROOT.name().contains(type.toUpperCase())) types.add(SQUARE_ROOT);
        if (RANDOM_STRING.name().contains(type.toUpperCase())) types.add(RANDOM_STRING);
        return types;
    }
}
