package kr.ac.hansung.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.view.AbstractView;

public class JSONView extends AbstractView {
	private static final ObjectMapper jsonMapper = new ObjectMapper ();
	
	public JSONView () {
		super ();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void renderMergedOutputModel (Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding ("UTF-8");
		String result = jsonMapper.writeValueAsString (model.get ("JSON_OBJECT"));
		response.getWriter ().write (result);
	}
}
