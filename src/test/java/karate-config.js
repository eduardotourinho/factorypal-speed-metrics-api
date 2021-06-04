function fn() {
    const env = karate.env || 'test'; // get java system property 'karate.env'
    karate.log('karate.env system property was:', env);

    const config = { // base config JSON
        appId: 'com.factorypal',
        baseUrl: 'http://localhost:8080'
    };

    // don't waste time waiting for a connection or if servers don't respond within 5 seconds
    karate.configure('connectTimeout', 5000);
    karate.configure('readTimeout', 5000);
    return config;
}