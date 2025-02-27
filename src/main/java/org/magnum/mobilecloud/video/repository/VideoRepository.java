package org.magnum.mobilecloud.video.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface VideoRepository extends CrudRepository<Video, Long>{	
	public Video findById(long id);
	
	public List<Video> findByName(String name);
	
	public List<Video> findByDurationLessThan(long duration);
}
