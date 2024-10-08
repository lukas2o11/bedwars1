package com.github.lukas2o11.bedwars.game;

import com.github.lukas2o11.bedwars.BedWars;
import com.github.lukas2o11.bedwars.game.commands.BedWarsPauseCommand;
import com.github.lukas2o11.bedwars.game.commands.BedWarsStartCommand;
import com.github.lukas2o11.bedwars.game.listeners.PlayerJoinListener;
import com.github.lukas2o11.bedwars.game.listeners.PlayerQuitListener;
import com.github.lukas2o11.bedwars.game.map.BedWarsGameMap;
import com.github.lukas2o11.bedwars.game.state.BedWarsGameState;
import com.github.lukas2o11.bedwars.game.state.BedWarsLobbyGameState;
import com.github.lukas2o11.bedwars.game.user.UserRegistry;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class BedWarsGame {

    public static final int STATE_LOBBY_MIN_PLAYERS = 1;
    public static final int[] STATE_LOBBY_SPECIAL_TICKS = new int[]{60, 30, 10, 5, 4, 3, 2, 1};
    public static final int COMMAND_START_THRESHOLD = 10;

    private final BedWars bedWars;
    private final UserRegistry userRegistry;
    private Optional<BedWarsGameState> gameState;
    private Optional<BedWarsGameMap> gameMap;

    public BedWarsGame(final BedWars bedWars) {
        this.bedWars = bedWars;
        this.userRegistry = new UserRegistry();
        this.gameState = Optional.of(new BedWarsLobbyGameState(this));
        gameState.get().enter();

        registerListeners();
    }

    private void registerListeners() {
        new PlayerJoinListener(this);
        new PlayerQuitListener(this);
    }

    private void registerCommands() {
        new BedWarsStartCommand(this);
        new BedWarsPauseCommand(this);
    }
}
