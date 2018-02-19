#!/usr/bin/env groovy

@GrabResolver(name='jitpack', root='https://jitpack.io', m2Compatible='true')
@Grab('com.github.testcontainers:testcontainers-groovy-script:1.4.2')
@Grab(group='io.rest-assured', module='rest-assured', version='3.0.7', scope='test')
@GrabExclude('org.codehaus.groovy:groovy-xml')
@groovy.transform.BaseScript(TestcontainersScript)
import io.restassured.*
import org.testcontainers.containers.*
import org.junit.*
import static io.restassured.RestAssured.*
import static org.hamcrest.Matchers.*
import static org.testcontainers.containers.Network.newNetwork;

class TestNetwork {
    static Network testNetwork = Network.newNetwork()
}

@groovy.transform.Field
static public GenericContainer nginx = new GenericContainer("nginx:1.9.4")
        .withClasspathResourceMapping("default.conf", "/etc/nginx/conf.d/default.conf", BindMode.READ_ONLY)
        .withExposedPorts(80)
        .withNetwork(TestNetwork.testNetwork)

@groovy.transform.Field
static public GenericContainer apache = new GenericContainer("httpd:2.4")
        .withExposedPorts(80)
        .withNetwork(TestNetwork.testNetwork)
        .withNetworkAliases("apache")


@BeforeClass
static void setup() {
    apache.start()
    nginx.start()

    RestAssured.baseURI = "http://${nginx.containerIpAddress}:${nginx.firstMappedPort}"
}

@Test
void "I will fail"() {
    when()
            .get("/")
    .then()
            .statusCode(200)
            .body(containsString("It works!"))
}