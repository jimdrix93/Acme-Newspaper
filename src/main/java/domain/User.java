package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.Valid;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	//Relationships
	private Collection<Newspaper> newspapers;
	private Collection<Article> articles;
	private Collection<Chirp> chirps;
	private Collection<User> followeds;
	
	@Valid
	@OneToMany
	public Collection<Newspaper> getNewspapers() {
		return newspapers;
	}
	public void setNewspapers(Collection<Newspaper> newspapers) {
		this.newspapers = newspapers;
	}
	@Valid
	@OneToMany
	public Collection<Article> getArticles() {
		return articles;
	}
	public void setArticles(Collection<Article> articles) {
		this.articles = articles;
	}
	@Valid
	@OneToMany
	public Collection<Chirp> getChirps() {
		return chirps;
	}
	public void setChirps(Collection<Chirp> chirps) {
		this.chirps = chirps;
	}
//	@Valid
//	@ManyToMany
//	public Collection<User> getFollowers() {
//		return followers;
//	}
//	public void setFollowers(Collection<User> followers) {
//		this.followers = followers;
//	}
	
	@Valid
	@ManyToMany
	public Collection<User> getFolloweds() {
		return followeds;
	}
	public void setFolloweds(Collection<User> followeds) {
		this.followeds = followeds;
	}
	
	

	
}
