package converters;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.MyMessage;
import repositories.MyMessageRepository;

@Component
@Transactional
public class StringToMyMessageConverter implements Converter<String, MyMessage> {

	@Autowired
	private MyMessageRepository	myMessageRepository;


	@Override
	public MyMessage convert(final String str) {
		MyMessage result;
		Integer id;
		try {
			if (StringUtils.isEmpty(str))
				result = null;
			else {
				id = Integer.valueOf(str);
				result = this.myMessageRepository.findOne(id);
			}

		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
