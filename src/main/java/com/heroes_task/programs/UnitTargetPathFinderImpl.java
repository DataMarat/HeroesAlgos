package main.java.com.heroes_task.programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.EdgeDistance;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

/**
 * Класс для нахождения пути между юнитами на игровом поле.
 * Реализация интерфейса UnitTargetPathFinder.
 */
public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {
    private static final int WIDTH = 27; // Ширина игрового поля
    private static final int HEIGHT = 21; // Высота игрового поля
    private static final int[][] DIRECTIONS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Возможные направления движения

    /**
     * Находит путь от атакующего юнита до целевого, избегая занятых клеток.
     *
     * @param attackUnit      Юнит, начинающий движение.
     * @param targetUnit      Целевой юнит.
     * @param existingUnitList Список всех юнитов на поле.
     * @return Список клеток (Edge), составляющих путь.
     * @complexity Временная сложность: O(WIDTH * HEIGHT * log(WIDTH * HEIGHT)).
     * Пространственная сложность: O(WIDTH * HEIGHT).
     */
    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        System.out.println("Начало поиска пути для юнита: " + attackUnit.getName());
        int[][] distance = initializeDistanceArray();
        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        Edge[][] previous = new Edge[WIDTH][HEIGHT];
        Set<String> occupiedCells = getOccupiedCells(existingUnitList, attackUnit, targetUnit);

        PriorityQueue<EdgeDistance> queue = new PriorityQueue<>(Comparator.comparingInt(EdgeDistance::getDistance));
        initializeStartPoint(attackUnit, distance, queue);

        while (!queue.isEmpty()) {
            EdgeDistance current = queue.poll();
            if (visited[current.getX()][current.getY()]) continue;
            visited[current.getX()][current.getY()] = true;

            if (isTargetReached(current, targetUnit)) {
                System.out.println("Цель достигнута. Завершение поиска");
                break;
            }

            exploreNeighbors(current, occupiedCells, distance, previous, queue);
        }

        List<Edge> path = constructPath(previous, attackUnit, targetUnit);
        if (path.isEmpty()) {
            System.out.println("Путь не найден. Полностью заблокировано");
        } else {
            System.out.println("Путь найден. Количество шагов: " + path.size());
        }
        return path;
    }

    /**
     * Инициализирует массив расстояний.
     *
     * @return Массив, заполненный значениями Integer.MAX_VALUE.
     * @complexity Временная сложность: O(WIDTH * HEIGHT).
     * Пространственная сложность: O(WIDTH * HEIGHT).
     */
    private int[][] initializeDistanceArray() {
        int[][] distance = new int[WIDTH][HEIGHT];
        for (int[] row : distance) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        System.out.println("Инициализация массива расстояний...");
        return distance;
    }

    /**
     * Определяет занятые клетки на поле.
     *
     * @param existingUnitList Список юнитов на поле.
     * @param attackUnit       Атакующий юнит.
     * @param targetUnit       Целевой юнит.
     * @return Множество строковых координат занятых клеток.
     * @complexity Временная сложность: O(N), где N — количество юнитов.
     * Пространственная сложность: O(N).
     */
    private Set<String> getOccupiedCells(List<Unit> existingUnitList, Unit attackUnit, Unit targetUnit) {
        Set<String> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                occupiedCells.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }
        System.out.println("Определение занятых клеток...");
        return occupiedCells;
    }

    /**
     * Инициализирует начальную точку.
     *
     * @param attackUnit Атакующий юнит.
     * @param distance   Массив расстояний.
     * @param queue      Очередь с приоритетом для обработки вершин.
     * @complexity Временная сложность: O(1).
     * Пространственная сложность: O(1).
     */
    private void initializeStartPoint(Unit attackUnit, int[][] distance, PriorityQueue<EdgeDistance> queue) {
        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        distance[startX][startY] = 0;
        queue.add(new EdgeDistance(startX, startY, 0));
        System.out.println("Инициализация стартовой точки...");
    }

    /**
     * Проверяет, достигнута ли цель.
     *
     * @param current    Текущая позиция.
     * @param targetUnit Целевой юнит.
     * @return true, если цель достигнута.
     * @complexity Временная сложность: O(1).
     */
    private boolean isTargetReached(EdgeDistance current, Unit targetUnit) {
        int currentX = current.getX(); // Текущая координата X
        int currentY = current.getY(); // Текущая координата Y
        int targetX = targetUnit.getxCoordinate(); // Целевая координата X
        int targetY = targetUnit.getyCoordinate(); // Целевая координата Y

        return currentX == targetX && currentY == targetY;
    }

    /**
     * Изучает соседние клетки для текущей позиции.
     *
     * @param current        Текущая позиция.
     * @param occupiedCells  Занятые клетки.
     * @param distance       Массив расстояний.
     * @param previous       Предыдущие клетки.
     * @param queue          Очередь с приоритетом.
     * @complexity Временная сложность: O(4) для каждой клетки (константа).
     */
    private void exploreNeighbors(EdgeDistance current, Set<String> occupiedCells, int[][] distance, Edge[][] previous, PriorityQueue<EdgeDistance> queue) {
        for (int[] dir : DIRECTIONS) {
            int neighborX = current.getX() + dir[0];
            int neighborY = current.getY() + dir[1];
            if (isValid(neighborX, neighborY, occupiedCells)) {
                int newDistance = distance[current.getX()][current.getY()] + 1;
                if (newDistance < distance[neighborX][neighborY]) {
                    distance[neighborX][neighborY] = newDistance;
                    previous[neighborX][neighborY] = new Edge(current.getX(), current.getY());
                    queue.add(new EdgeDistance(neighborX, neighborY, newDistance));
                }
            }
        }
    }

    /**
     * Проверяет, является ли клетка валидной для движения.
     *
     * @param x             Координата X.
     * @param y             Координата Y.
     * @param occupiedCells Занятые клетки.
     * @return true, если клетка валидна.
     * @complexity Временная сложность: O(1).
     */
    private boolean isValid(int x, int y, Set<String> occupiedCells) {
        boolean withinBoundsX = x >= 0 && x < WIDTH; // Проверка границ по X
        boolean withinBoundsY = y >= 0 && y < HEIGHT; // Проверка границ по Y
        boolean isCellFree = !occupiedCells.contains(x + "," + y); // Проверка, занята ли клетка

        return withinBoundsX && withinBoundsY && isCellFree;
    }

    /**
     * Строит путь от начальной до целевой клетки.
     *
     * @param previous   Массив предыдущих клеток.
     * @param attackUnit Атакующий юнит.
     * @param targetUnit Целевой юнит.
     * @return Список клеток пути.
     * @complexity Временная сложность: O(WIDTH * HEIGHT) в худшем случае.
     * Пространственная сложность: O(WIDTH * HEIGHT).
     */
    private List<Edge> constructPath(Edge[][] previous, Unit attackUnit, Unit targetUnit) {
        List<Edge> path = new ArrayList<>();
        int pathX = targetUnit.getxCoordinate();
        int pathY = targetUnit.getyCoordinate();

        while (pathX != attackUnit.getxCoordinate() || pathY != attackUnit.getyCoordinate()) {
            path.add(new Edge(pathX, pathY));
            Edge prev = previous[pathX][pathY];
            if (prev == null) return Collections.emptyList();
            pathX = prev.getX();
            pathY = prev.getY();
        }
        path.add(new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate()));
        Collections.reverse(path);
        return path;
    }
}
