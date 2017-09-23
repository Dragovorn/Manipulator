import com.dragovorn.manipulator.module.ModuleManager;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TestModuleInfo {

    @Test
    public void testModuleInfo() throws IOException {
        ModuleManager.loadModuleInfo(new File("modules", "test.jar"));
    }
}