package hex.editor.model;

import java.util.LinkedList;
import java.util.List;

public class Positions {
    private static int index = 0;
    private static final List<Position> list = new LinkedList<>();


    public Positions() {
    }

    public Position getNext() {
        return list.get(index < list.size() - 1 ? ++index : index);
    }
    public Position getPrevious() {
        return list.get(index > 0 ? --index : index);
    }

    public Position getCurrent() {
        return list.get(index);
    }

    public void add(Position pos) {
        list.add(pos);
    }

    public void remove(Position pos) {
        list.remove(pos);
    }

    public void removeAll() {
        list.forEach(list::remove);
    }
}