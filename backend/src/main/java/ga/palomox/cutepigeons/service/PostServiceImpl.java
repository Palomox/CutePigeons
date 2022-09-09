package ga.palomox.cutepigeons.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ga.palomox.cutepigeons.domain.Pigeon;
import ga.palomox.cutepigeons.domain.query.QPigeon;
import ga.palomox.cutepigeons.model.Post;
import ga.palomox.cutepigeons.security.KetoPermsManager;
import sh.ory.ApiException;

public class PostServiceImpl implements IPostService {
	
	private KetoPermsManager permsManager;
	
	public PostServiceImpl(KetoPermsManager permsManager) {
		this.permsManager = permsManager;
	}
	
	public Optional<Post> getPostById(int id) {
		Optional<Pigeon> pigeon = new QPigeon().id.eq(id).allowed.eq(true).findOneOrEmpty();
		if (pigeon.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(new Post(pigeon.get().getId(), pigeon.get().getUrl(), pigeon.get().getUser().getDisplayName()));
	}

	@Override
	public List<Post> getPosts() {
		List<Pigeon> pigeons = new QPigeon().allowed.eq(true).findList();
		return pigeons.parallelStream().map((pigeon) -> {
			return new Post(pigeon.getId(), pigeon.getUrl(), pigeon.getUser().getDisplayName());
		}).collect(Collectors.toList());

	}

	@Override
	public void addPost(Pigeon pigeon) throws ApiException {
		this.permsManager.allow(pigeon.getUser().getUuid(), "writer", "pigeons", Integer.toString(pigeon.getId()));
		pigeon.setAllowed(true);
		pigeon.save();
	}

	@Override
	public void addPosts(Pigeon... pigeons) throws ApiException {
		for (Pigeon pigeon : pigeons) {
			this.permsManager.allow(pigeon.getUser().getUuid(), "writer", "pigeons", Integer.toString(pigeon.getId()));
			pigeon.save();
		}
	}

	@Override
	public void removePost(Pigeon post) throws ApiException {
		this.permsManager.getWriteApi().deleteRelationTuples("pigeons", Integer.toString(post.getId()), "write", null, null, null, null);
		new QPigeon().id.eq(post.getId()).delete();
	}

	@Override
	public void addPostToModQueue(Pigeon post) throws ApiException {
		this.permsManager.allow(post.getUser().getUuid(), "writer", "pigeons", Integer.toString(post.getId()));
		post.setAllowed(false);
		post.save();
	}

	@Override
	public List<Post> getModQueue() {
		return new QPigeon().allowed.eq(false).findList()
				.stream()
				.map(pigeon -> {return new Post(pigeon.getId(), 
						pigeon.getUrl(), 
						pigeon.getUser().getDisplayName());})
				.collect(Collectors.toList());
	}

	@Override
	public Optional<Pigeon> getPigeonById(int id) {
		return new QPigeon().id.eq(id).findOneOrEmpty();
	}
}
