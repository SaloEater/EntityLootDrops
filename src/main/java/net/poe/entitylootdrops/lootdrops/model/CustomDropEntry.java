package net.poe.entitylootdrops.lootdrops.model;

import java.util.ArrayList;
import java.util.List;

public class CustomDropEntry {
    private String itemId;              // The Minecraft item ID (e.g., "minecraft:diamond")
    private float dropChance;           // Percentage chance to drop (0-100)
    private int minAmount;              // Minimum number of items to drop
    private int maxAmount;              // Maximum number of items to drop
    private String nbtData;             // Custom NBT data for the item (for enchantments, names, etc.)
    private String requiredAdvancement; // Player must have this advancement to get the drop
    private String requiredEffect;      // Player must have this potion effect to get the drop
    private String requiredEquipment;   // Player must have this item equipped to get the drop
    private String requiredWeather;     // "clear", "rain", "thunder"
    private String requiredTime;        // "day", "night", "dawn", "dusk"
    private String requiredDimension;   // Player must be in this dimension to get the drop
    private String requiredBiome;       // Player must be in this biome to get the drop
    private String command;             // Command to execute when the drop occurs
    private float commandChance;        // Percentage chance to execute the command (0-100)
    private String dropCommand;         // Command to execute only when the item actually drops
    private float dropCommandChance;    // Percentage chance to execute the drop command (0-100)
    private int commandCoolDown = 0;    // Cooldown in seconds before command can be executed again
    private String _comment;            // Comment for documentation in the JSON file
    private boolean requirePlayerKill = true; // Default to true for backward compatibility
    private boolean allowDefaultDrops = true; // Default to true for backward compatibility
    private List<String> allowModIDs = new ArrayList<>(); // List of mod IDs that are allowed to drop items
    private float extraDropChance = 0.0f;    // Percentage chance for extra vanilla drops (0-100)
    private int extraAmountMin = 1;          // Minimum amount of extra vanilla drops
    private int extraAmountMax = 1;          // Maximum amount of extra vanilla drops

    /**
     * Default constructor for Gson deserialization.
     */
    public CustomDropEntry() {
        this.commandChance = 100.0f; // Default to 100% if command is specified
        this.dropCommandChance = 100.0f; // Default to 100% if drop command is specified
        this.requirePlayerKill = true; // Default to true for backward compatibility
        this.allowDefaultDrops = true; // Default to true for backward compatibility
        this.allowModIDs = new ArrayList<>();
        this.extraDropChance = 0.0f;
        this.extraAmountMin = 1;
        this.extraAmountMax = 1;
        this.commandCoolDown = 0; // Default to no cooldown
    }

    /**
     * Constructor for a basic drop without NBT data.
     */
    public CustomDropEntry(String itemId, float dropChance, int minAmount, int maxAmount) {
        this(itemId, dropChance, minAmount, maxAmount, null);
    }

    /**
     * Constructor with NBT data.
     */
    public CustomDropEntry(String itemId, float dropChance, int minAmount, int maxAmount, String nbtData) {
        this.itemId = itemId;
        this.dropChance = dropChance;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.nbtData = nbtData;
        this.commandChance = 100.0f; // Default to 100% if command is specified
        this.dropCommandChance = 100.0f; // Default to 100% if drop command is specified
        this.requirePlayerKill = true; // Default to true for backward compatibility
        this.allowDefaultDrops = true; // Default to true for backward compatibility
        this.allowModIDs = new ArrayList<>();
        this.extraDropChance = 0.0f;
        this.extraAmountMin = 1;
        this.extraAmountMax = 1;
        this.commandCoolDown = 0; // Default to no cooldown
    }

    // Getters
    public String getItemId() { return itemId; }
    public float getDropChance() { return dropChance; }
    public int getMinAmount() { return minAmount; }
    public int getMaxAmount() { return maxAmount; }
    public String getNbtData() { return nbtData; }
    public String getRequiredAdvancement() { return requiredAdvancement; }
    public String getRequiredEffect() { return requiredEffect; }
    public String getRequiredEquipment() { return requiredEquipment; }
    public String getRequiredWeather() { return requiredWeather; }
    public String getRequiredTime() { return requiredTime; }
    public String getRequiredDimension() { return requiredDimension; }
    public String getRequiredBiome() { return requiredBiome; }
    public String getCommand() { return command; }
    public float getCommandChance() { return commandChance; }
    public String getDropCommand() { return dropCommand; }
    public float getDropCommandChance() { return dropCommandChance; }
    public int getCommandCoolDown() { return commandCoolDown; }
    public boolean isRequirePlayerKill() { return requirePlayerKill; }
    public boolean isAllowDefaultDrops() { return allowDefaultDrops; }
    public List<String> getAllowModIDs() { return allowModIDs; }
    public float getExtraDropChance() { return extraDropChance; }
    public int getExtraAmountMin() { return extraAmountMin; }
    public int getExtraAmountMax() { return extraAmountMax; }

    // Setters
    public void setItemId(String itemId) { this.itemId = itemId; }
    public void setDropChance(float dropChance) { this.dropChance = dropChance; }
    public void setMinAmount(int minAmount) { this.minAmount = minAmount; }
    public void setMaxAmount(int maxAmount) { this.maxAmount = maxAmount; }
    public void setNbtData(String nbtData) { this.nbtData = nbtData; }
    public void setRequiredAdvancement(String requiredAdvancement) { this.requiredAdvancement = requiredAdvancement; }
    public void setRequiredEffect(String requiredEffect) { this.requiredEffect = requiredEffect; }
    public void setRequiredEquipment(String requiredEquipment) { this.requiredEquipment = requiredEquipment; }
    public void setRequiredWeather(String requiredWeather) { this.requiredWeather = requiredWeather; }
    public void setRequiredTime(String requiredTime) { this.requiredTime = requiredTime; }
    public void setRequiredDimension(String requiredDimension) { this.requiredDimension = requiredDimension; }
    public void setRequiredBiome(String requiredBiome) { this.requiredBiome = requiredBiome; }
    public void setCommand(String command) { this.command = command; }
    public void setCommandChance(float commandChance) { this.commandChance = commandChance; }
    public void setDropCommand(String dropCommand) { this.dropCommand = dropCommand; }
    public void setDropCommandChance(float dropCommandChance) { this.dropCommandChance = dropCommandChance; }
    public void setCommandCoolDown(int commandCoolDown) { this.commandCoolDown = commandCoolDown; }
    public void setRequirePlayerKill(boolean requirePlayerKill) { this.requirePlayerKill = requirePlayerKill; }
    public void setAllowDefaultDrops(boolean allowDefaultDrops) { this.allowDefaultDrops = allowDefaultDrops; }
    public void setAllowModIDs(List<String> allowModIDs) { this.allowModIDs = allowModIDs != null ? allowModIDs : new ArrayList<>(); }
    public void setComment(String comment) { this._comment = comment; }
    public void setExtraDropChance(float extraDropChance) { this.extraDropChance = extraDropChance; }
    public void setExtraAmountMin(int extraAmountMin) { this.extraAmountMin = extraAmountMin; }
    public void setExtraAmountMax(int extraAmountMax) { this.extraAmountMax = extraAmountMax; }

    // Utility methods
    public boolean hasCommand() { return command != null && !command.isEmpty(); }
    public boolean hasDropCommand() { return dropCommand != null && !dropCommand.isEmpty(); }
    public boolean hasNbtData() { return nbtData != null && !nbtData.isEmpty(); }
    public boolean hasRequiredAdvancement() { return requiredAdvancement != null && !requiredAdvancement.isEmpty(); }
    public boolean hasRequiredEffect() { return requiredEffect != null && !requiredEffect.isEmpty(); }
    public boolean hasRequiredEquipment() { return requiredEquipment != null && !requiredEquipment.isEmpty(); }
    public boolean hasRequiredWeather() { return requiredWeather != null && !requiredWeather.isEmpty(); }
    public boolean hasRequiredTime() { return requiredTime != null && !requiredTime.isEmpty(); }
    public boolean hasRequiredDimension() { return requiredDimension != null && !requiredDimension.isEmpty(); }
    public boolean hasRequiredBiome() { return requiredBiome != null && !requiredBiome.isEmpty(); }
    public boolean hasCommandCoolDown() { return commandCoolDown > 0; }

    public boolean hasItem() {
        return itemId != null && !itemId.isEmpty();
    }
}