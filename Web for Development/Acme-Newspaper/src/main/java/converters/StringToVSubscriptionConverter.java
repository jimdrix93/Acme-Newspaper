
package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.VSubscription;
import repositories.VSubscriptionRepository;

@Component
@Transactional
public class StringToVSubscriptionConverter implements Converter<String, VSubscription> {

	@Autowired
	private VSubscriptionRepository	repository;


	@Override
	public VSubscription convert(final String str) {
		VSubscription result;
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
