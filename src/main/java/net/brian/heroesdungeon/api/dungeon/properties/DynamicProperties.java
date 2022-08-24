package net.brian.heroesdungeon.api.dungeon.properties;

import lombok.Builder;
import lombok.Getter;

@Builder
public class DynamicProperties {

    @Getter
    @Builder.Default
    private int difficulty = 0;


}
