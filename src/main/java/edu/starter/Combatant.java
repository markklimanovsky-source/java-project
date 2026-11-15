package edu.starter;

/**
 * Represents a combatant (Player or Enemy) with encapsulated state,
 * resource management, defensive capabilities, and upgradable attack power.
 */
class Combatant {
    private String name;
    private int hp;
    private final int maxHp;
    private int resource;
    private final int maxResource;
    private int pendingDefense;
    private int attackDamage;

    public Combatant(String name, int hp, int resource, int maxResource, int attackDamage) {
        this.name = name;
        this.maxHp = hp;
        this.hp = hp;
        this.resource = resource;
        this.maxResource = maxResource;
        this.pendingDefense = 0;
        this.attackDamage = attackDamage;
    }

    // Getters
    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getResource() { return resource; }
    public int getPendingDefense() { return pendingDefense; }
    public int getAttackDamage() { return attackDamage; }
    public boolean isAlive() { return hp > 0; }

    /** Applies incoming damage, factoring in pending defense and clamping health.
     * @pre amount >= 0
     * @post hp >= 0 && hp <= maxHp
     * @post pendingDefense == 0
     */
    public void takeDamage(int amount) {
        int actualDamage = amount;
        if (pendingDefense > 0) {
            actualDamage = Math.max(0, amount - pendingDefense);
            pendingDefense = 0;
        }
        hp -= actualDamage;
        hp = Math.max(0, Math.min(hp, maxHp));
    }

    /** Restores health safely without exceeding the maximum limit.
     * @pre amount >= 0
     * @post hp == Math.min(maxHp, old(hp) + amount)
     */
    public void restoreHp(int amount) {
        this.hp += amount;
        this.hp = Math.max(0, Math.min(this.hp, maxHp));
    }

    /** Permanently increases basic attack damage.
     * @pre bonus >= 0
     * @post attackDamage == old(attackDamage) + bonus
     */
    public void upgradeAttack(int bonus) {
        this.attackDamage += bonus;
    }

    /** Sets the temporary pending defense for the next incoming hit.
     * @pre defense >= 0
     * @post pendingDefense == defense
     */
    public void setPendingDefense(int defense) {
        this.pendingDefense = defense;
    }

    /** Clears temporary defense between encounters or turns.
     * @pre true
     * @post pendingDefense == 0
     */
    public void clearDefense() {
        this.pendingDefense = 0;
    }

    /** Attempts to spend resource points.
     * @pre amount >= 0
     * @post $ret == (old(resource) >= amount)
     * @post $ret ==> (resource == old(resource) - amount)
     * @post !$ret ==> (resource == old(resource))
     */
    public boolean spendResource(int amount) {
        if (resource >= amount) {
            resource -= amount;
            return true;
        }
        return false;
    }

    /** Recovers resource points up to a given cap.
     * @pre amount >= 0 && cap >= 0
     * @post resource >= 0 && resource <= cap
     */
    public void recoverResource(int amount, int cap) {
        resource += amount;
        resource = Math.max(0, Math.min(resource, cap));
    }
}

/**
 * Reward Enum representing valid choices between encounters.
 */
enum Reward {
    RECOVERY, 
    POWER
}
