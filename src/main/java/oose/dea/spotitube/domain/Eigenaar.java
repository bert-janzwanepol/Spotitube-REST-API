package oose.dea.spotitube.domain;

public class Eigenaar
{

    private String username;
    private String token;
    private String firstname;
    private String lastname;

    public Eigenaar(String username, String token, String firstname, String lastname) {
        this.username = username;
        this.token = token;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFullname()
    {
        return firstname + " " + lastname;
    }

    public String getToken()
    {
        return token;
    }
}
