package karate;

import com.intuit.karate.junit5.Karate;

public class FactoryPalMetricsApiRunner {

    @Karate.Test
    Karate testMachineParametersApi() {
        return Karate.run("machine-parameters")
                .tags("~@skip")
                .relativeTo(getClass());
    }
}
