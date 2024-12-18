package org.acme;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.TestInstance;

@QuarkusIntegrationTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameResourceTestIT extends GameResourceTest {
    // Execute the same tests but in packaged mode.
}
