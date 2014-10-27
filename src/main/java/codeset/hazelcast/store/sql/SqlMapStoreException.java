package codeset.hazelcast.store.sql;

@SuppressWarnings("serial")
public class SqlMapStoreException extends RuntimeException {

    public SqlMapStoreException() {
        super();
    }

    public SqlMapStoreException(String message) {
        super(message);
    }

    public SqlMapStoreException(Throwable throwable) {
        super(throwable);
    }

    public SqlMapStoreException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
