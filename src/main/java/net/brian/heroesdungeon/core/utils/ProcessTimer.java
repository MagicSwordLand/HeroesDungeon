package net.brian.heroesdungeon.core.utils;

import java.util.function.Consumer;

public class ProcessTimer {

    public static void process(Runnable runnable, Consumer<Long> processedTime){
        long currentTime = System.currentTimeMillis();
        runnable.run();
        processedTime.accept(System.currentTimeMillis()-currentTime);
    }

}
