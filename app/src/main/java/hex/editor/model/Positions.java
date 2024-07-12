package hex.editor.model;

import java.util.LinkedList;
import java.util.List;

public class Positions {
    private static int index = 0;
    private static final List<Position> list = new LinkedList<>();


    public boolean isEmpty() {
        return list.isEmpty();
    }


    public Positions() {
    }

    public void getNext() {
        index = Math.min(list.size() - 1, index + 1);
    }
    
    public void getPrevious() {
        index = Math.max(0, index - 1);
    }

    public Position getCurrent() {
        return list.get(index);
    }

    public void getNext(int size) {
        index = Math.min(list.size() - 1, index + size);
    }
    public void getPrevious(int size) {
        index = Math.max(0, index - size);
    }

    public Position[] getCurrent(int size) {
        Position[] positions = new Position[size];
        for (int i = index; i < index + size; i++) {
            positions[i - index] = list.get(i);
        }
        return positions;
    }

    public void add(Position pos) {
        index = 0;
        list.add(pos);
    }

    public void remove(Position pos) {
        index = 0;
        list.remove(pos);
    }

    public void removeAll() {
        index = 0;
        list.clear();
    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }    
}