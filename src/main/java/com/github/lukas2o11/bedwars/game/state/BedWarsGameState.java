package com.github.lukas2o11.bedwars.game.state;

import com.github.lukas2o11.bedwars.game.BedWarsGame;
import com.github.lukas2o11.bedwars.game.countdown.BedWarsCountdown;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
@Getter
public abstract class BedWarsGameState {

    private final BedWarsGame game;
    private final BedWarsCountdown countdown;

    public void enter() {
        game.setGameState(this);
    }

    public void leave()  {
        game.setGameState(null);
    }

    public abstract boolean canStart();

    public void check() {
        if (countdown.isStarted()) {
            if (!canStart()) {
                countdown.cancel();
                return;
            }
        }

        if (!countdown.isStarted()) {
            if (canStart()) {
                countdown.start();
            }
        }
    }

    public abstract Optional<BedWarsGameState> getNextState();
}
