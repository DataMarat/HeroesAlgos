package main.java.com.heroes_task.programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.List;

/**
 * Реализация интерфейса SimulateBattle.
 * Симулирует бой между двумя армиями.
 */
public class SimulateBattleImpl implements SimulateBattle {

    private PrintBattleLog printBattleLog;

    /**
     * Устанавливает реализацию интерфейса PrintBattleLog для вывода логов боя.
     *
     * @param printBattleLog Реализация интерфейса PrintBattleLog.
     */
    public void setPrintBattleLog(PrintBattleLog printBattleLog) {
        this.printBattleLog = printBattleLog;
    }

    /**
     * Симулирует бой между двумя армиями.
     *
     * @param playerArmy   Армия игрока.
     * @param computerArmy Армия компьютера.
     * @throws InterruptedException Если симуляция прервана.
     *
     * Сложность: O(n * m), где n — количество юнитов в армии игрока, m — количество юнитов в армии компьютера.
     */
    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        System.out.println("Начало симуляции боя...");
        while (!playerArmy.getUnits().isEmpty() && !computerArmy.getUnits().isEmpty()) {
            executeAttacks(playerArmy.getUnits(), computerArmy.getUnits());
            executeAttacks(computerArmy.getUnits(), playerArmy.getUnits());
        }
        if (playerArmy.getUnits().isEmpty()) {
            System.out.println("Армия игрока проиграла!");
        } else {
            System.out.println("Армия компьютера проиграла!");
        }
    }

    /**
     * Выполняет атаки одной армии на другую.
     *
     * @param attackers Армия, совершающая атаку.
     * @param defenders Армия, принимающая атаку.
     * @throws InterruptedException Если выполнение атаки прерывается.
     *
     * Сложность: O(a * d), где a — количество атакующих, d — количество защитников.
     */
    private void executeAttacks(List<Unit> attackers, List<Unit> defenders) throws InterruptedException {
        String message;
        for (Unit attacker : attackers) {
            if (!defenders.isEmpty()) {
                Unit target = attacker.getProgram().attack(); // O(1)
                if (target != null) {
                    target.setHealth(target.getHealth() - attacker.getBaseAttack()); // O(1)
                    if (printBattleLog != null) {
                        printBattleLog.printBattleLog(attacker, target); // Логирование атаки
                    }
                    message = "Атакующий: " + attacker.getName() + " атакует цель: " + target.getName()
                            + ". Здоровье цели: " + target.getHealth();
                    System.out.println(message);
                    if (target.getHealth() <= 0) {
                        defenders.remove(target); // O(d)
                        message = "Цель " + target.getName() + " была уничтожена";
                        System.out.println(message);
                    }
                }
            } else {
                System.out.println("Армия врага пуста. Атаки завершены");
                break;
            }
        }
    }
}
