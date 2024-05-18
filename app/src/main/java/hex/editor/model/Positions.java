package hex.editor.model;

import java.util.LinkedList;
import java.util.List;

public class Positions {
    private static int index = 0;
    private static final List<Position> list = new LinkedList<>();


    public boolean isEmpty() {
        return list.isEmpty();
    };


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