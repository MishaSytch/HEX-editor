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
        index++;
        if (index >= list.size()) {
            index = 0;
        }
    }
    public void getPrevious() {
        index--;
        if (index < 0) {
            index = list.size() - 1;
        }
    }

    public Position getCurrent() {
        return list.get(index);
    }

    public void getNext(int size) {
        for (int i = 0; i < size; i++) index++;
        if (index >= list.size()) {
            index = 0;
        }
    }
    public void getPrevious(int size) {
        for (int i = 0; i < size; i++) index--;
        if (index < 0) {
            index = list.size() - 1;
        }
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
}