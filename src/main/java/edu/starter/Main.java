package edu.starter;
import java.util.Scanner;


public class Main {

    /**
     * Executes an interactive encounter between the player and an enemy using a console loop.
     * Returns true if the player wins, false if the player is defeated.
     */
    public static boolean playBattle(Combatant player, Combatant enemy, Scanner scanner) {
        System.out.println("\n========================================");
        System.out.println("ENCOUNTER START: " + player.getName() + " vs " + enemy.getName());
        System.out.println("========================================");

        while (player.isAlive() && enemy.isAlive()) {
            System.out.println("\n----------------------------------------");
            System.out.println("STATUS -> " + player.getName() + " HP: " + player.getHp() + "/" + player.getMaxHp() 
                + " | Resource: " + player.getResource() + " | Attack Dmg: " + player.getAttackDamage());
            System.out.println("        -> " + enemy.getName() + " HP: " + enemy.getHp() + "/" + enemy.getMaxHp());
            System.out.println("----------------------------------------");
            System.out.println("Choose your action:");
            System.out.println("1. Attack  (Deals " + player.getAttackDamage() + " damage)");
            System.out.println("2. Defend  (Prepares 5 defense against next hit)");
            System.out.println("3. Special (Costs 3 resource, deals 15 damage)");
            System.out.println("4. Recover (Restores 3 resource, max 10)");
            System.out.print("Enter choice (1-4): ");

            String input = scanner.nextLine().trim();
            boolean validActionTaken = false;
            boolean enemyCounterattacks = true;

            switch (input) {
                case "1":
                    enemy.takeDamage(player.getAttackDamage());
                    System.out.println("\n> You attack " + enemy.getName() + " for " + player.getAttackDamage() + " damage!");
                    validActionTaken = true;
                    break;

                case "2":
                    player.setPendingDefense(5);
                    System.out.println("\n> You brace yourself, preparing 5 pending defense.");
                    validActionTaken = true;
                    break;

                case "3":
                    if (player.spendResource(3)) {
                        enemy.takeDamage(15);
                        System.out.println("\n> You unleash a Special attack dealing 15 damage!");
                        validActionTaken = true;
                    } else {
                        System.out.println("\n[Invalid] Not enough resource (need 3, have " + player.getResource() + ")! Action rejected.");
                        enemyCounterattacks = false;
                    }
                    break;

                case "4":
                    player.recoverResource(3, 10);
                    System.out.println("\n> You recover 3 resource!");
                    validActionTaken = true;
                    break;

                default:
                    System.out.println("\n[Invalid] Unknown choice. Please enter a number between 1 and 4.");
                    enemyCounterattacks = false;
                    break;
            }

            if (!validActionTaken) {
                continue;
            }

            if (!enemy.isAlive()) {
                System.out.println("\n> " + enemy.getName() + " has been defeated! No counterattack occurs.");
                return true;
            }

            if (enemyCounterattacks && enemy.isAlive()) {
                int enemyDamage = 10;
                player.takeDamage(enemyDamage);
                System.out.println("> " + enemy.getName() + " counterattacks for " + enemyDamage + " damage!");
            }

            if (!player.isAlive()) {
                System.out.println("\n> " + player.getName() + " has been defeated!");
                return false;
            }
        }

        return player.isAlive();
    }

    /**
     * Handles the reward selection gate between encounters.
     * @pre player.isAlive()
     * @post true
     */
    public static void promptAndApplyReward(Combatant player, Scanner scanner) {
        boolean rewardClaimed = false;

        System.out.println("\n========================================");
        System.out.println("VICTORY REWARD SELECTION");
        System.out.println("========================================");
        System.out.println("Choose your reward:");
        System.out.println("1. RECOVERY (+25 HP, capped at maximum)");
        System.out.println("2. POWER    (Permanently increases basic attack damage by 2)");

        while (!rewardClaimed) {
            System.out.print("Enter choice (1 or 2): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    player.restoreHp(25);
                    System.out.println("\n> Reward Claimed: RECOVERY! Current HP: " + player.getHp() + "/" + player.getMaxHp());
                    rewardClaimed = true;
                    break;
                case "2":
                    player.upgradeAttack(5);
                    System.out.println("\n> Reward Claimed: POWER! Basic attack damage is now: " + player.getAttackDamage());
                    rewardClaimed = true;
                    break;
                default:
                    System.out.println("[Invalid Input] Please enter '1' for Recovery or '2' for Power. Try again.");
                    break;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("OOP Adventure - Week 3 with Formalized Method Rules");
        Scanner scanner = new Scanner(System.in);

        Combatant player = new Combatant("Hero", 100, 5, 10, 10);

        Combatant enemyA = new Combatant("Goblin Scout", 30, 5, 5, 10);
        boolean encounter1Won = playBattle(player, enemyA, scanner);

        if (!encounter1Won) {
            System.out.println("\n=== ADVENTURE OVER ===");
            System.out.println("You were defeated in Encounter 1. No reward granted.");
            scanner.close();
            return;
        }

        System.out.println("\n*** ENCOUNTER 1 VICTORY! ***");

        promptAndApplyReward(player, scanner);
        
        player.clearDefense();
        System.out.println("\n--- Transition to Encounter 2 ---");
        System.out.println("Carried State -> Player HP: " + player.getHp() + " | Resource: " + player.getResource() + " | Attack Dmg: " + player.getAttackDamage());

        Combatant enemyB = new Combatant("Orc Commander", 45, 5, 5, 10);
        boolean encounter2Won = playBattle(player, enemyB, scanner);

        System.out.println("\n========================================");
        if (encounter2Won) {
            System.out.println("=== ULTIMATE VICTORY! ===");
            System.out.println("You successfully completed both encounters!");
        } else {
            System.out.println("=== ADVENTURE OVER ===");
            System.out.println("You were defeated in Encounter 2.");
        }
        System.out.println("========================================");

        scanner.close();
    }
}