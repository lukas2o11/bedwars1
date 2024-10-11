package com.github.lukas2o11.bedwars.game.map;

import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsDirectedGameMapLocation;
import com.github.lukas2o11.bedwars.game.map.serialization.BedWarsGameMapLocation;
import com.github.lukas2o11.bedwars.game.spawners.BedWarsGameResourceSpawnerType;
import com.github.lukas2o11.bedwars.game.teams.BedWarsGameTeamType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DefaultBedWarsGameMap implements BedWarsGameMap {

    private String name;
    private int minPlayers;
    private int teamCount;
    private int teamSize;
    private Set<BedWarsGameTeamType> teams;
    private Map<BedWarsGameTeamType, BedWarsDirectedGameMapLocation> teamSpawnLocations;
    private Map<BedWarsGameTeamType, BedWarsGameMapLocation> teamBedLocations;
    private Map<Integer, BedWarsDirectedGameMapLocation> shopLocations;
    private Map<BedWarsGameResourceSpawnerType, Map<Integer, BedWarsGameMapLocation>> spawnerLocations;
    private BedWarsDirectedGameMapLocation respawnLocation;
    private BedWarsDirectedGameMapLocation spectatorLocation;
    private Set<Material> breakableBlocks;

    @Override
    public boolean addTeam(final BedWarsGameTeamType teamType) {
        this.teams = Optional.ofNullable(teams).orElseGet(HashSet::new);
        return teams.add(teamType);
    }

    @Override
    public boolean removeTeam(final BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teams)
                .map(teams -> teams.remove(teamType))
                .orElse(false);
    }

    @Override
    public void addTeamSpawnLocation(final BedWarsGameTeamType teamType, final Location location) {
        this.teamSpawnLocations = Optional.ofNullable(teamSpawnLocations).orElseGet(HashMap::new);
        teamSpawnLocations.put(teamType, new BedWarsDirectedGameMapLocation(location));
    }

    @Override
    public boolean removeTeamSpawnLocation(final BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teamSpawnLocations)
                .map(teamSpawnLocations -> teamSpawnLocations.remove(teamType) != null)
                .orElse(false);
    }

    @Override
    public void addTeamBedLocation(final BedWarsGameTeamType teamType, final Location location) {
        this.teamBedLocations = Optional.ofNullable(teamBedLocations).orElseGet(HashMap::new);
        teamBedLocations.put(teamType, new BedWarsGameMapLocation(location));
    }

    @Override
    public boolean removeTeamBedLocation(final BedWarsGameTeamType teamType) {
        return Optional.ofNullable(teamBedLocations)
                .map(teamBedLocations -> teamBedLocations.remove(teamType) != null)
                .orElse(false);
    }

    @Override
    public void addShopLocation(final Location location) {
        this.shopLocations = Optional.ofNullable(shopLocations).orElseGet(HashMap::new);
        shopLocations.put(shopLocations.size() + 1, new BedWarsDirectedGameMapLocation(location));
    }

    @Override
    public boolean removeShopLocation(final int id) {
        boolean removed = Optional.ofNullable(shopLocations)
                .map(shopLocations -> shopLocations.remove(id) != null)
                .orElse(false);
        if (removed) {
            shopLocations.keySet().stream()
                    .filter(i -> i > id)
                    .forEach(i -> {
                        final BedWarsDirectedGameMapLocation location = shopLocations.remove(i);
                        shopLocations.put(i - 1, location);
                    });
        }
        return removed;
    }

    @Override
    public void addSpawnerLocation(final BedWarsGameResourceSpawnerType spawnerType, final Location location) {
        this.spawnerLocations = Optional.ofNullable(spawnerLocations).orElseGet(HashMap::new);
        final Map<Integer, BedWarsGameMapLocation> idLocations = spawnerLocations.computeIfAbsent(spawnerType, o -> new HashMap<>());
        idLocations.put(idLocations.size() + 1, new BedWarsGameMapLocation(location));
    }

    @Override
    public boolean removeSpawnerLocation(final BedWarsGameResourceSpawnerType spawnerType, final int id) {
        return Optional.ofNullable(spawnerLocations).map(spawnerLocations ->
                        Optional.ofNullable(spawnerLocations.get(spawnerType))
                                .map(idLocations -> idLocations.remove(id) != null)
                                .orElse(false))
                .orElse(false);
    }

    @Override
    public void setRespawnLocation(final Location location) {
        this.respawnLocation = new BedWarsDirectedGameMapLocation(location);
    }

    @Override
    public void setSpectatorLocation(final Location location) {
        this.spectatorLocation = new BedWarsDirectedGameMapLocation(location);
    }

    @Override
    public boolean addBreakableBlock(final Material material) {
        return breakableBlocks.add(material);
    }

    @Override
    public boolean removeBreakableBlock(final Material material) {
        return breakableBlocks.remove(material);
    }
}
