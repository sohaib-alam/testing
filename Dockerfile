# Intentionally old JDK image for container/runtime scanning.
# Availability of this historical tag has not been verified.
FROM openjdk:8u202-jdk
WORKDIR /fixture
COPY . /fixture
CMD ["java", "-version"]
