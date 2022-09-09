package ga.palomox.cutepigeons.service;

import java.util.List;
import java.util.Optional;

import ga.palomox.cutepigeons.domain.Pigeon;
import ga.palomox.cutepigeons.model.Post;
import sh.ory.ApiException;

public interface IPostService {
	Optional<Post> getPostById(int id);
	List<Post> getPosts();
	Optional<Pigeon> getPigeonById(int id);
	void addPost(Pigeon post) throws ApiException;
	void addPosts(Pigeon...posts) throws ApiException;
	void addPostToModQueue(Pigeon post) throws ApiException;
	void removePost(Pigeon post) throws ApiException;
	List<Post> getModQueue();
}
