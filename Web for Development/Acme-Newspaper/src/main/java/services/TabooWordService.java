
package services;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.TabooWordRepository;
import domain.Administrator;
import domain.Advertisement;
import domain.Article;
import domain.Chirp;
import domain.Newspaper;
import domain.TabooWord;

@Service
@Transactional
public class TabooWordService {

	// Managed repositories ------------------------------------------------
	@Autowired
	private TabooWordRepository		tabooWordRepository;

	//Services
	@Autowired
	private AdministratorService	administratorService;

	// Constructor ----------------------------------------------------------
	public TabooWordService() {
		super();
	}

	// Methods CRUD ---------------------------------------------------------

	public TabooWord create() {
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);

		final TabooWord tabooWord = new TabooWord();

		return tabooWord;
	}

	public TabooWord findOne(final int tabooWordId) {
		TabooWord result;
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);

		result = this.tabooWordRepository.findOne(tabooWordId);
		Assert.notNull(result);

		return result;
	}

	public Collection<TabooWord> findAll() {

		Collection<TabooWord> result;

		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);

		result = this.tabooWordRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Newspaper> findAllNewspapersWithTabooWord() {

		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);

		final Collection<Newspaper> ret = new LinkedList<Newspaper>();

		final Collection<Object[]> source = this.tabooWordRepository.findAllNewspaperWithTabooWord();

		for (final Object obj[] : source) {
			final Newspaper n = new Newspaper();

			n.setId((Integer) obj[0]);
			n.setDescription((String) obj[2]);
			n.setIsPrivate((Boolean) obj[3]);
			n.setIsPublicated((Boolean) obj[4]);
			n.setPicture((String) obj[5]);
			n.setPublicationDate((java.util.Date) obj[6]);
			n.setTitle((String) obj[7]);

			ret.add(n);
		}

		return ret;

	}

	public Collection<Advertisement> findAllAdvertisementWithTabooWord() {
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);

		final Collection<Advertisement> ret = new LinkedList<Advertisement>();

		final Collection<Object[]> source = this.tabooWordRepository.findAllAdvertisementWithTabooWord();

		for (final Object[] obj : source) {
			final Advertisement n = new Advertisement();

			n.setId((Integer) obj[0]);
			n.setBannerURL((String) obj[2]);
			n.setTargetURL((String) obj[3]);
			n.setTitle((String) obj[4]);

			ret.add(n);
		}

		return ret;

	}

	public Collection<Article> findAllArticlesWithTabooWord() {
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);

		final Collection<Article> ret = new LinkedList<Article>();

		final Collection<Object[]> source = this.tabooWordRepository.findAllArticleWithTabooWord();

		for (final Object[] obj : source) {
			final Article n = new Article();

			n.setId((Integer) obj[0]);
			n.setBody((String) obj[2]);
			n.setFinalMode((Boolean) obj[3]);
			n.setPublicationMoment((Date) obj[4]);
			n.setSummary((String) obj[5]);
			n.setTitle((String) obj[6]);

			ret.add(n);
		}
		return ret;

	}

	public Collection<Chirp> findAllChirpsWithTabooWord() {
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);

		final Collection<Chirp> ret = new LinkedList<Chirp>();

		final Collection<Object[]> source = this.tabooWordRepository.findAllChirpWithTabooWord();

		for (final Object[] obj : source) {
			final Chirp n = new Chirp();

			n.setId((Integer) obj[0]);
			n.setDescription((String) obj[2]);
			n.setTitle((String) obj[4]);

			ret.add(n);
		}

		return ret;
	}

	public TabooWord save(final TabooWord tabooWord) {

		Assert.notNull(tabooWord);

		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);

		final TabooWord saved = this.tabooWordRepository.save(tabooWord);

		return saved;
	}

	public void delete(final TabooWord tabooWord) {
		Assert.notNull(tabooWord);
		final Administrator admin = this.administratorService.findByPrincipal();
		Assert.notNull(admin);
		this.tabooWordRepository.delete(tabooWord);
	}

	public void flush() {
		this.tabooWordRepository.flush();

	}

	public Collection<TabooWord> getTabooWordFromMyMessageSubjectAndBody(final String subject, final String body) {

		Assert.notNull(subject);
		Assert.notNull(body);

		return this.tabooWordRepository.getTabooWordFromMyMessageSubjectAndBody(subject, body);

	}

}
