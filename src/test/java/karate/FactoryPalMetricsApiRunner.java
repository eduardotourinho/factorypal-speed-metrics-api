package karate;

import com.factorypal.speedmetrics.FactoryPalSpeedMetricsApplication;
import com.intuit.karate.junit5.Karate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles(profiles = "test")
@SpringBootTest(classes = { FactoryPalSpeedMetricsApplication.class})
public class FactoryPalMetricsApiRunner {

    @Karate.Test
    Karate testMachineParametersApi() {
        return Karate.run("machine-parameters")
                .tags("~@skip")
                .relativeTo(getClass());
    }
}
