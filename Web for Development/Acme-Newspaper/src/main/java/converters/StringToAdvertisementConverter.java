
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import repositories.AdvertisementRepository;
import domain.Advertisement;

@Component
@Transactional
public class StringToAdvertisementConverter implements Converter<String, Advertisement> {

	@Autowired
	private AdvertisementRepository	repository;


	@Override
	public Advertisement convert(final String str) {
		Advertisement result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.repository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
