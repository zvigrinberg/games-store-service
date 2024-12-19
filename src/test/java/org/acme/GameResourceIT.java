package org.acme;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.transaction.annotations.Rollback;
import org.junit.jupiter.api.TestInstance;

@QuarkusIntegrationTest
class GameResourceTestIT extends GameResourceTest {
    // Execute the same tests but in packaged mode.
}
