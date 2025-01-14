package main.java.com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Реализация интерфейса GeneratePreset для создания армии.
 * Данный класс генерирует армию на основе доступных юнитов и заданного лимита очков.
 */
public class GeneratePresetImpl implements GeneratePreset {

    /**
     * Генерирует армию на основе списка доступных юнитов и лимита очков.
     * Итоговая сложность алгоритма O(n*log(n))
     *
     * @param unitList  Список доступных юнитов.
     * @param maxPoints Максимальное количество очков для армии.
     * @return Сгенерированная армия.
     */
    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        String message;
        message = "Начало генерации армии...";
        System.out.println(message);

        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();

        message = "Сортировка юнитов по эффективности (атака/стоимость)...";
        System.out.println(message);
        // Сложность сортировки юнитов O(n*log(n))
        unitList.stream()
                .sorted(Comparator.comparingDouble((Unit unit) -> unit.getBaseAttack() / (double) unit.getCost()).reversed())
                .forEach(unit -> {
                    System.out.println("Рассматривается юнит: " + unit.getName() +
                            " (атака: " + unit.getBaseAttack() +
                            ", стоимость: " + unit.getCost() + ")");
                    // Сложность вычисления максимального количества юнитов O(1)
                    int unitsToAdd = calculateMaxUnitsToAdd(unit, maxPoints, computerArmy.getPoints());
                    System.out.println("Можно добавить " + unitsToAdd + " экземпляров этого юнита.");
                    // Сложность добавления юнитов в армию O(1), т.к. количество юнитов ограничено
                    addUnitsToArmy(unit, unitsToAdd, selectedUnits);
                    computerArmy.setPoints(computerArmy.getPoints() + unitsToAdd * unit.getCost());
                });

        message = "Назначение случайных координат юнитам...";
        System.out.println(message);
        // Сложность назначения координат O(n)
        assignCoordinates(selectedUnits);
        computerArmy.setUnits(selectedUnits);

        message = "Генерация армии завершена. Общее количество юнитов: " +
                selectedUnits.size() + ". Использовано очков: " +
                computerArmy.getPoints() + "/" + maxPoints + ".";
        System.out.println(message);

        return computerArmy;
    }

    /**
     * Вычисляет максимальное количество экземпляров юнита, которые можно добавить в армию.
     *
     * @param unit          Юнит.
     * @param maxPoints     Лимит очков для армии.
     * @param currentPoints Текущие использованные очки.
     * @return Максимальное количество юнитов.
     */
    private int calculateMaxUnitsToAdd(Unit unit, int maxPoints, int currentPoints) {
        return Math.min(11, (maxPoints - currentPoints) / unit.getCost());
    }

    /**
     * Добавляет указанный юнит в армию.
     *
     * @param unit          Юнит.
     * @param unitsToAdd    Количество экземпляров.
     * @param selectedUnits Список выбранных юнитов.
     */
    private void addUnitsToArmy(Unit unit, int unitsToAdd, List<Unit> selectedUnits) {
        for (int i = 0; i < unitsToAdd; i++) {
            Unit newUnit = createNewUnit(unit, i);
            selectedUnits.add(newUnit);
            String message = "Добавлен юнит: " + newUnit.getName() + ".";
            System.out.println(message);
        }
    }

    /**
     * Создает новый экземпляр юнита с уникальным именем.
     *
     * @param unit  Прототип юнита.
     * @param index Индекс юнита.
     * @return Новый юнит.
     */
    private Unit createNewUnit(Unit unit, int index) {
        Unit newUnit = new Unit(unit.getName(), unit.getUnitType(), unit.getHealth(),
                unit.getBaseAttack(), unit.getCost(), unit.getAttackType(),
                unit.getAttackBonuses(), unit.getDefenceBonuses(), -1, -1);
        newUnit.setName(unit.getUnitType() + " " + index);
        return newUnit;
    }

    /**
     * Назначает случайные координаты всем юнитам из списка.
     *
     * @param units Список юнитов.
     */
    private void assignCoordinates(List<Unit> units) {
        Set<String> occupiedCoords = new HashSet<>();
        Random random = new Random();

        for (Unit unit : units) {
            assignRandomCoordinates(unit, occupiedCoords, random);
        }
    }

    /**
     * Назначает случайные координаты конкретному юниту, избегая занятых координат.
     *
     * @param unit           Юнит.
     * @param occupiedCoords Занятые координаты.
     * @param random         Генератор случайных чисел.
     */
    private void assignRandomCoordinates(Unit unit, Set<String> occupiedCoords, Random random) {
        int coordX, coordY;
        do {
            coordX = random.nextInt(3);
            coordY = random.nextInt(21);
        } while (occupiedCoords.contains(coordX + "," + coordY));
        occupiedCoords.add(coordX + "," + coordY);
        String message = "Юнит " + unit.getName() + " получил координаты (" + coordX + ", " + coordY + ").";
        System.out.println(message);
        unit.setxCoordinate(coordX);
        unit.setyCoordinate(coordY);
    }
}
