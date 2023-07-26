/* Licensed under GNU GPL v3.0 (C) 2023 */
package at.iver.bop_it.network;

import java.io.Serializable;

public class PlayerStats implements Serializable {
    private String playerName;
    private int playerHealth;
    private int turnDamage;
    private int turnHealing;
    private int turnCoin;

    public PlayerStats(
            String playerName, int playerHealth, int turnDamage, int turnHealing, int turnCoin) {
        this.playerName = playerName;
        this.playerHealth = playerHealth;
        this.turnDamage = turnDamage;
        this.turnHealing = turnHealing;
        this.turnCoin = turnCoin;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(int playerHealth) {
        this.playerHealth = playerHealth;
    }

    public int getTurnDamage() {
        return turnDamage;
    }

    public void setTurnDamage(int turnDamage) {
        this.turnDamage = turnDamage;
    }

    public int getTurnHealing() {
        return turnHealing;
    }

    public void setTurnHealing(int turnHealing) {
        this.turnHealing = turnHealing;
    }

    public int getTurnCoin() {
        return turnCoin;
    }

    public void setTurnCoin(int turnCoin) {
        this.turnCoin = turnCoin;
    }
}
