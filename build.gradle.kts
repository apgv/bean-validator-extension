plugins {
    java
}

group = "codes.foobar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile("org.hibernate.validator:hibernate-validator:6.0.2.Final")
    compile("org.glassfish:javax.el:3.0.1-b08")
    testCompile("org.junit.jupiter:junit-jupiter-api:5.0.0")
    testCompile("org.assertj:assertj-core:3.8.0")
}