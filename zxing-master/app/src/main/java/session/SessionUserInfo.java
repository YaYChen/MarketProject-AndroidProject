package session;

public class SessionUserInfo {

    public int userId = 0;
    public String userName = "";
    public String token = "";
    public String code = "";

    private static volatile SessionUserInfo instance;

    private SessionUserInfo(){
    }

    public static SessionUserInfo getInstance(){
        if(instance == null){
            instance = new SessionUserInfo();
        }
        return instance;
    }
}
