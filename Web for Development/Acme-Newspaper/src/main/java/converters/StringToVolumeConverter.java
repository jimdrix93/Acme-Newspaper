
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Volume;
import repositories.VolumeRepository;

@Component
@Transactional
public class StringToVolumeConverter implements Converter<String, Volume> {

	@Autowired
	private VolumeRepository	repository;


	@Override
	public Volume convert(final String str) {
		Volume result;
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
