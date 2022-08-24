package net.brian.heroesdungeon.core.utils.scheduler;

import net.brian.heroesdungeon.api.dungeon.DungeonInstance;

public class DungeonScheduler {

    private final Node firstNode;
    private Node nodePtr;

    public DungeonScheduler(SchedulerAction firstAction){
        firstNode = new Node(firstAction);
        nodePtr = firstNode;
    }

    public DungeonScheduler add(SchedulerAction action){
        nodePtr.nextNode = new Node(action);
        nodePtr = nodePtr.nextNode;
        return this;
    }

    public void run(DungeonInstance dungeon){
        firstNode.action.run(dungeon, () -> nextNode(dungeon,firstNode));
    }

    private void nextNode(DungeonInstance dungeonInstance,Node node){
        node = node.nextNode;
        if(node != null){
            Node finalNode = node;
            node.action.run(dungeonInstance,()->nextNode(dungeonInstance,finalNode));
        }
    }

    static class Node{

        private Node(SchedulerAction action){
            this.action = action;
        }
        Node nextNode = null;
        final SchedulerAction action;
    }

}
