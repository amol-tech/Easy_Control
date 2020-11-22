package controls;

/**
 * Connection Detail pojo 
 * @author AKhandek
 */
public class ConnectionDetail
{
    private static final String SEP_COLON = ":";
    private static final String URL_PREFIX = "jdbc:oracle:thin:@";
    private String host;
    private String port;
    private String service;
    private String user;
    private String password;

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getService()
    {
        return service;
    }

    public void setService(String service)
    {
        this.service = service;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

/*    public String getURL()
    {
        if (host != null && port != null && service != null)
        {
            return URL_PREFIX + host + SEP_COLON + port + SEP_COLON + service;
        }
        return null;
    }*/

    public String getURL()
    {
      if ((this.host != null) && (this.port != null) && (this.service != null)) {
        return "jdbc:oracle:thin:@" + this.host + ":" + this.port + "/" + this.service;
      }
      return null;
    }
}
