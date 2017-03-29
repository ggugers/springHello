package kr.ac.hansung.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import kr.ac.hansung.HomeController;

/**
 * Handles requests for the application home page.
 */
@Controller
public class CommonController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private static final String ROOT_UPLOAD_DIR = "/dev/upload";
	
	@RequestMapping(value = "/common/fileUploadAjax.do", method = RequestMethod.POST)
    public ModelAndView fileUploadAjax(MultipartHttpServletRequest request, ModelAndView mav ) throws Exception {
		Map<String, Object> rtnMap = new HashMap<String, Object>();

		String mode = request.getParameter("mode");
		String name = request.getParameter("name");
		String childFolder = "temp";
		if("stamp".equals(mode)){
			childFolder = "stamp";
		}else if("course".equals(mode)){
			childFolder = "course";
		}else if("test".equals(mode)){
			childFolder = "test";
		}else if("excel".equals(mode)){
			childFolder = "excel";
		}
		
		//folder mkdir
		File saveFolder = new File(ROOT_UPLOAD_DIR+File.separator+childFolder);
		if(!saveFolder.exists() || saveFolder.isFile()){
			saveFolder.mkdirs();
		}
		
		//iterator
		Iterator<String> files = request.getFileNames();
		MultipartFile multipartFile;
		String filePath;
		List<Map<String, Object>> fileInfoList = new ArrayList<Map<String,Object>>();
		
		try{
			
			//while
			while(files.hasNext()){			
				
				//file����
				String uuid = UUID.randomUUID().toString().replaceAll("-", "");
				multipartFile = request.getFile((String)files.next());
				String fieldName = multipartFile.getName();
				String extName = multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".")+1 );
				String filename = uuid+"."+extName;
				filePath = ROOT_UPLOAD_DIR+File.separator+childFolder+File.separator+filename;
				if( filename != null && !"".equals(multipartFile.getOriginalFilename()) ){
					multipartFile.transferTo(new File(filePath));

					//file���� ���
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("fieldName", fieldName);
					map.put("FilePath", filePath);
					map.put("OriginalFilename", multipartFile.getOriginalFilename());
					map.put("extName", extName);
					map.put("FileSize", multipartFile.getSize());
					map.put("FileSavedName", filename);
					fileInfoList.add(map);
				}
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
		int sizeCheck = 0;
		if(fileInfoList != null && fileInfoList.size() > sizeCheck){
			rtnMap = fileInfoList.get(0);
		}
		mav.setView(new JSONView());
		mav.addObject("JSON_OBJECT",  fileInfoList);
		return mav;
    }
	
	
	@RequestMapping(value = "/common/fileUpload.do", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView fileUpload(HttpServletRequest request, ModelAndView mav ) throws Exception {

		return mav;
    }
	
}
