FROM openjdk:21
WORKDIR /app

# wait-for-it.sh betiğini kopyala ve çalıştırılabilir yap
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

# JAR dosyanızı kopyala
COPY target/appointment-service-0.0.1-SNAPSHOT.jar /app/app.jar

# MySQL ayağa kalkana kadar bekle, sonra uygulamayı başlat
ENTRYPOINT ["/app/wait-for-it.sh", "appointment-mysql:3306", "--timeout=30", "--strict", "--"]
CMD ["java", "-jar", "/app/app.jar"]
