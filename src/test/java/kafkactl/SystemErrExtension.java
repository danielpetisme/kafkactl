package kafkactl;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SystemErrExtension implements BeforeEachCallback, AfterEachCallback {

    private final PrintStream original = System.err;

    private final ByteArrayOutputStream stream = new ByteArrayOutputStream();


    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        System.setOut(new PrintStream(stream));
    }

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        System.setOut(original);
    }

    @Override
    public String toString() {
        return stream.toString();
    }


}
