#!/usr/bin/env groovy

@GrabResolver(name='jitpack', root='https://jitpack.io', m2Compatible='true')
@Grab(group='org.testcontainers', module='testcontainers', version='1.7.2')
@Grab('com.github.testcontainers:testcontainers-groovy-script:1.4.2')
@Grab(group='io.rest-assured', module='rest-assured', version='3.0.7', scope='test')
@GrabExclude('org.codehaus.groovy:groovy-xml')
@groovy.transform.BaseScript(TestcontainersScript)
import io.restassured.*
import org.testcontainers.containers.*
import org.junit.*
import static io.restassured.RestAssured.*
import static org.hamcrest.Matchers.*

@Test
void "I will fail"() {
    // setup
    Network testNetwork = Network.newNetwork()
    GenericContainer apache = new GenericContainer("httpd:2.4")
            .withExposedPorts(80)
            .withNetwork(testNetwork)
            .withNetworkAliases("apache")

    GenericContainer nginx = new GenericContainer("nginx:1.9.4")
            .withClasspathResourceMapping("default.conf", "/etc/nginx/conf.d/default.conf", BindMode.READ_ONLY)
            .withExposedPorts(80)
            .withNetwork(testNetwork)


    apache.start()
    nginx.start()

    RestAssured.baseURI = "http://${nginx.containerIpAddress}:${nginx.firstMappedPort}"

    // test
    when()
            .get("/")
    .then()
            .statusCode(200)
            .body(containsString("It works!"))


    // cleanup
    apache.stop()
    nginx.stop()
}