package net.sabercrafts.coursemgmt.service.impl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sabercrafts.coursemgmt.entity.LearningPath;
import net.sabercrafts.coursemgmt.exception.LearningPathServiceException;
import net.sabercrafts.coursemgmt.repository.LearningPathRepository;
import net.sabercrafts.coursemgmt.repository.UserLearningPathProgressRepository;
import net.sabercrafts.coursemgmt.service.LearningPathService;
import net.sabercrafts.coursemgmt.utils.SlugGenerator;

@Service
@Transactional
public class LearningPathServiceImpl implements LearningPathService {

	@Autowired
	private LearningPathRepository learningPathRepository;
	
	@Autowired
	private UserLearningPathProgressRepository userLearningPathProgressRepository;

	@Override
	public LearningPath create(LearningPath learningPath) {

		learningPath.setSlug(SlugGenerator.toSlug(learningPath.getTitle()));

		Optional<LearningPath> result = learningPathRepository.findBySlug(learningPath.getSlug());

		if (result.isPresent()) {
			throw new LearningPathServiceException("LearningPath with slug " + learningPath.getSlug() + " already exists");
		}

		return learningPathRepository.save(learningPath);
	}

	@Override
	public LearningPath getById(Long id) {

		Optional<LearningPath> result = learningPathRepository.findById(id);

		if (result.isEmpty()) {
			throw new LearningPathServiceException("LearningPath with id " + id + " doesn't exist");
		}

		return result.get();

	}

	@Override
	public LearningPath getBySlug(String slug) {

		Optional<LearningPath> result = learningPathRepository.findBySlug(slug);

		if (result.isEmpty()) {
			throw new LearningPathServiceException("LearningPath with slug " + slug + " doesn't exist");

		}

		return result.get();

	}

	@Override
	public List<LearningPath> getAll() {
		return learningPathRepository.findAll();
	}

	@Override
	public LearningPath edit(LearningPath learningPath) {

		Optional<LearningPath> result = learningPathRepository.findById(learningPath.getId());

		if (result.isEmpty()) {
			throw new LearningPathServiceException("LearningPath with id " + learningPath.getId() + " doesn't exist");

		}

		if(result.get().getTitle() != learningPath.getTitle()) {
			
			learningPath.setSlug(SlugGenerator.toSlug(learningPath.getTitle()));
			
			if(learningPathRepository.findBySlug(learningPath.getSlug()).isPresent()) {
				throw new LearningPathServiceException("LearningPath with slug "+ learningPath.getSlug() +" already exists.");
			}
		}
		
		return learningPathRepository.save(learningPath);
	}

	@Override
	public boolean remove(LearningPath learningPath) {

		Optional<LearningPath> result = learningPathRepository.findById(learningPath.getId());

		if (result.isEmpty()) {
			throw new LearningPathServiceException("LearningPath with id " + learningPath.getId() + " doesn't exist");
		}
		userLearningPathProgressRepository.deleteAllByLearningPath(learningPath.getId());
		learningPathRepository.delete(learningPath);
		
		return true;

	}

}
