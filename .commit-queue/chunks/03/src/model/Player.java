package model;

import exceptions.InvalidPlayerException;

public class Player {

    private final String   name;
    private final int      number;
    private final Position position;

    private final int pace;
    private final int shooting;
    private final int passing;
    private final int defending;

    private int stamina;

    public Player(String name, int number, Position position,
                  int pace, int shooting, int passing,
                  int defending, int stamina) throws InvalidPlayerException {

        if (name == null || name.isBlank()) {
            throw new InvalidPlayerException(
                "Player name cannot be empty.");
        }
        if (number < 1 || number > 99) {
            throw new InvalidPlayerException(
                "Jersey number must be between 1 and 99.");
        }
        if (position == null) {
            throw new InvalidPlayerException(
                "Position cannot be null.");
        }

        this.name      = name;
        this.number    = number;
        this.position  = position;
        this.pace      = clampSkill(pace);
        this.shooting  = clampSkill(shooting);
        this.passing   = clampSkill(passing);
        this.defending = clampSkill(defending);
        this.stamina   = clampStamina(stamina);
    }

    public int getOverall() {
        return position.calculateOverall(
                pace, shooting, passing, defending, stamina);
    }

    public void drainStamina(int amount) {
        this.stamina = Math.max(0, this.stamina - amount);
    }

    public void restoreStamina(int fullStamina) {
        this.stamina = clampStamina(fullStamina);
    }

    public boolean isFatigued() {
        return stamina < 30;
    }

    public String   getName()       { return name; }
    public int      getNumber()     { return number; }
    public Position getPosition()   { return position; }
    public int      getPace()       { return pace; }
    public int      getShooting()   { return shooting; }
    public int      getPassing()    { return passing; }
    public int      getDefending()  { return defending; }
    public int      getStamina()    { return stamina; }

    @Override
    public String toString() {
        return String.format("#%d %s [%s] OVR:%d STM:%d",
                number, name, position.getLabel(), getOverall(), stamina);
    }

    private int clampSkill(int value) {
        return Math.max(1, Math.min(100, value));
    }

    private int clampStamina(int value) {
        return Math.max(0, Math.min(100, value));
    }
}