package main.java.com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация интерфейса SuitableForAttackUnitsFinder для поиска юнитов,
 * подходящих для атаки.
 */
public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    /**
     * Находит подходящие для атаки юниты по строкам.
     *
     * @param unitsByRow      Список строк юнитов.
     * @param isLeftArmyTarget Указывает, является ли целью левая армия.
     * @return Список подходящих для атаки юнитов.
     *
     * Сложность: O(n * m), где n — количество строк, m — количество юнитов в строке.
     */
    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        Map<Integer, List<Unit>> suitableUnitsMap = new HashMap<>();
        System.out.println("Начинается поиск подходящих юнитов...");

        // Обход строк юнитов
        for (int i = 0; i < unitsByRow.size(); i++) {
            List<Unit> row = unitsByRow.get(i);
            System.out.println("Анализ строки " + i);

            List<Unit> suitableUnitsInRow = findSuitableUnitsInRow(row, isLeftArmyTarget);
            if (!suitableUnitsInRow.isEmpty()) {
                System.out.println("Найдены подходящие юниты в строке " + i);
                suitableUnitsMap.put(i, suitableUnitsInRow);
            }
        }

        List<Unit> allSuitableUnits = new ArrayList<>();
        for (List<Unit> units : suitableUnitsMap.values()) {
            allSuitableUnits.addAll(units); // Добавляем все юниты из текущего списка
        }

        System.out.println("Обнаружено " + allSuitableUnits.size() + " подходящих юнитов");
        return allSuitableUnits;
    }

    /**
     * Находит подходящие юниты в конкретной строке.
     *
     * @param row             Строка юнитов.
     * @param isLeftArmyTarget Указывает, является ли целью левая армия.
     * @return Список подходящих юнитов в строке.
     *
     * Сложность: O(m), где m — количество юнитов в строке.
     */
    private List<Unit> findSuitableUnitsInRow(List<Unit> row, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();
        for (int index = 0; index < row.size(); index++) {
            Unit unit = row.get(index);
            if (unit != null && unit.isAlive() &&
                    (isLeftArmyTarget ? isRightmostUnit(row, index) : isLeftmostUnit(row, index))) {
                System.out.println("Подходящий юнит: " + unit.getName() + " в позиции " + index);
                suitableUnits.add(unit);
            }
        }
        return suitableUnits;
    }

    /**
     * Проверяет, является ли юнит самым правым в строке.
     *
     * @param row       Строка юнитов.
     * @param unitIndex Индекс юнита в строке.
     * @return true, если юнит самый правый, иначе false.
     *
     * Сложность: O(m - unitIndex), где m — длина строки.
     */
    private boolean isRightmostUnit(List<Unit> row, int unitIndex) {
        if (unitIndex == row.size() - 1) {
            return true; // Юнит уже самый правый
        }
        for (int i = unitIndex + 1; i < row.size(); i++) {
            if (row.get(i) != null) {
                return false; // Найден не-null элемент справа
            }
        }
        return true; // Все элементы справа null
    }

    /**
     * Проверяет, является ли юнит самым левым в строке.
     *
     * @param row       Строка юнитов.
     * @param unitIndex Индекс юнита в строке.
     * @return true, если юнит самый левый; иначе false.
     *
     * Сложность: O(unitIndex).
     */
    private boolean isLeftmostUnit(List<Unit> row, int unitIndex) {
        if (unitIndex == 0) {
            return true; // Юнит уже самый левый
        }
        for (int i = 0; i < unitIndex; i++) {
            if (row.get(i) != null) {
                return false; // Найден не-null элемент слева
            }
        }
        return true; // Все элементы слева null
    }

}
