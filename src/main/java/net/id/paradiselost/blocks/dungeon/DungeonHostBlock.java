package net.id.paradiselost.blocks.dungeon;

//will be implemented by the dungeon "gates" to allow easier triggering of open/close from the dungeon switch
public interface DungeonHostBlock {

    void acivate();

    void deacivate();
}
