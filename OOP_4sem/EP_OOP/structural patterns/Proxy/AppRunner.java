package Proxy;
/** клиент **/
public class AppRunner {
    public static void main(String[] args) {
        /** загрузка данных производится не при запуске проекта, а при запросе на соединение **/
        Database database = new ProxyDatabaseConnector("connect to database");

        database.connect();
    }
}
