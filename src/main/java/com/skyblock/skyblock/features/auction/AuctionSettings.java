package com.skyblock.skyblock.features.auction;

import com.skyblock.skyblock.SkyblockPlayer;
import com.skyblock.skyblock.enums.Rarity;
import lombok.Getter;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

@Getter
public class AuctionSettings implements ConfigurationSerializable {

    public enum AuctionSort {
        HIGHEST,
        LOWEST,
        ENDING,
        MOST;
    }

    public enum BinFilter {
        ALL,
        BIN,
        AUCTIONS,
    }

    private AuctionCategory category;
    private AuctionSort sort;
    private Rarity teir;
    private BinFilter binFilter;

    public AuctionSettings(AuctionCategory category, AuctionSort sort, Rarity teir, BinFilter binFilter) {
        this.category = category;
        this.sort = sort;
        this.teir = teir;
        this.binFilter = binFilter;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("category", category.name());
        map.put("sort", sort.name());
        map.put("teir", (teir == null ? "null" : teir.name()));
        map.put("binFilter", binFilter.name());

        return map;
    }

    private SkyblockPlayer skyblockPlayer;

    public void setPlayer(SkyblockPlayer skyblockPlayer) {
        this.skyblockPlayer = skyblockPlayer;
    }

    public void incrementSort() {
        List<AuctionSort> sorts = Arrays.asList(AuctionSort.values());

        int newIndex = sorts.indexOf(sort) + 1;

        if (newIndex == sorts.size()) newIndex = 0;

        sort = sorts.get(newIndex);

        update();
    }

    public void incrementBin() {
        List<BinFilter> binFilters = Arrays.asList(BinFilter.values());

        int newIndex = binFilters.indexOf(binFilter) + 1;

        if (newIndex == binFilters.size()) newIndex = 0;

        binFilter = binFilters.get(newIndex);

        update();
    }

    public void incrementRarity() {
        List<String> rarities = new ArrayList<>(Arrays.asList("null"));
        List<Rarity> values = new ArrayList<>(Arrays.asList(Rarity.values()));

        Collections.reverse(values);

        values.forEach((v) -> {
            rarities.add(v.name());
        });

        int newIndex = rarities.indexOf((teir != null ? teir.name() : "null")) + 1;

        if (newIndex == rarities.size()) newIndex = 0;

        teir = (rarities.get(newIndex).equals("null") ? null : Rarity.valueOf(rarities.get(newIndex)));

        update();
    }

    public void setCategory(AuctionCategory category) {
        this.category = category;

        update();
    }

    public void update() {
        this.skyblockPlayer.setValue("auctions.auctionSettings", serialize());
        this.skyblockPlayer.setAuctionSettings(this);
    }

    public static AuctionSettings deserialize(Map<String, Object> value) {
        return new AuctionSettings(AuctionCategory.valueOf((String) value.get("category")),
                AuctionSort.valueOf((String) value.get("sort")), (!value.get("teir").equals("null") ? Rarity.valueOf((String) value.get("teir")) : null),
                BinFilter.valueOf((String) value.get("binFilter")));
    }
}
