package oose.dea.spotitube.domain;

public class Track
{
	private int id;
	private String title;
	private String performer;
	private int duration;
	private String album;
	private int playcount;
	private String publicationDate;
	private String description;
	private boolean offlineAvailable;

	public Track(int id, String title, String performer, int duration, String album, int playcount, String publicationDate, String description, boolean offlineAvailable) {
		this.id = id;
		this.title = title;
		this.performer = performer;
		this.duration = duration;
		this.album = album;
		this.playcount = playcount;
		this.publicationDate = publicationDate;
		this.description = description;
		this.offlineAvailable = offlineAvailable;
	}

	public Track(int id, String title, String performer, int duration, String album, int playcount, String publicationDate, String description) {
		this.id = id;
		this.title = title;
		this.performer = performer;
		this.duration = duration;
		this.album = album;
		this.playcount = playcount;
		this.publicationDate = publicationDate;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getPerformer() {
		return performer;
	}

	public int getDuration() {
		return duration;
	}

	public String getAlbum() {
		return album;
	}

	public int getPlaycount() {
		return playcount;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public String getDescription() {
		return description;
	}

	public boolean isOfflineAvailable() {
		return offlineAvailable;
	}

}
