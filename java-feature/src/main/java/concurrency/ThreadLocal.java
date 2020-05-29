package concurrency;

public class ThreadLocal {
}


/**
 * ThreadLocal 并不解决线程间共享数据的问题
 * ThreadLocal 通过隐式的在不同线程内创建独立实例副本避免了实例线程安全的问题
 * 每个线程持有一个 Map 并维护了 ThreadLocal 对象与具体实例的映射，该 Map 由于只被持有它的线程访问，故不存在线程安全以及锁的问题
 * ThreadLocalMap 的 Entry 对 ThreadLocal 的引用为弱引用，避免了 ThreadLocal 对象无法被回收的问题
 * ThreadLocalMap 的 set 方法通过调用 replaceStaleEntry 方法回收键为 null 的 Entry 对象的值（即为具体实例）以及 Entry 对象本身从而防止内存泄漏
 * ThreadLocal 适用于变量在线程间隔离且在方法间共享的场景
 */

class SessionHandler {


    public static class Session {
        private String id;
        private String user;
        private String status;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public Session createSession() {
        return new Session();
    }

    public String getUser(Session session) {
        return session.getUser();
    }

    public String getStatus(Session session) {
        return session.getStatus();
    }

    public void setStatus(Session session, String status) {
        session.setStatus(status);
    }

    public static void main(String[] args) {
        new Thread(() -> {
            SessionHandler handler = new SessionHandler();
            Session session = handler.createSession();
            handler.getStatus(session);
            handler.getUser(session);
            handler.setStatus(session, "close");
            handler.getStatus(session);
        }).start();
    }
}

class SessionHandlerUseThreadLocal {

    public static java.lang.ThreadLocal<Session> session = java.lang.ThreadLocal.withInitial(() -> new Session());

    public static class Session {
        private String id;
        private String user;
        private String status;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public String getUser() {
        return session.get().getUser();
    }

    public String getStatus() {
        return session.get().getStatus();
    }

    public void setStatus(String status) {
        session.get().setStatus(status);
    }


    public static void main(String[] args) {
        new Thread(() -> {
            SessionHandlerUseThreadLocal handler = new SessionHandlerUseThreadLocal();
            handler.getStatus();
            handler.getUser();
            handler.setStatus("close");
            handler.getStatus();
        }).start();
    }
}