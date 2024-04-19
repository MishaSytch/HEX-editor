package hex.editor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestApp {
    @Test
    void testappExecuted() {
        App app = new App();
        
        Assertions.assertNotNull(app);
    }
}
