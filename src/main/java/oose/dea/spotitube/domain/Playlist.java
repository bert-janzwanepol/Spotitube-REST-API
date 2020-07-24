package oose.dea.spotitube.domain;

public class Playlist
{
	private int id;
	private String name;
	private boolean owner;

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(boolean owner) {
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isOwner() {
		return owner;
	}
}
